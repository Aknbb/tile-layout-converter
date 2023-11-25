package com.aknbb.tilelayoutconverter;

import com.aknbb.tilelayoutconverter.LayoutConverters.ILayoutConverter;
import com.aknbb.tilelayoutconverter.LayoutConverters.TileInfo;
import me.tongfei.progressbar.ProgressBar;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@CommandLine.Command(name = "tile-layout-converter", mixinStandardHelpOptions = true, version = "tile-layout-converter 1.0",
        description = "Converts layout of cached tiles. For example from tms to xyz", headerHeading = "@|bold,underline Usage|@:%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description|@:%n%n",
        optionListHeading = "%n@|bold,underline Options|@:%n")
public class Main implements Callable<Integer> {

    @CommandLine.Option(names = {"-i", "--input"}, description = "Path of input directory.", defaultValue = "${sys:user.dir}", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String inputPath;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Path of output directory.", defaultValue = "${sys:user.dir}", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private static String outputPath;

    @CommandLine.Option(names = {"-n", "--outputDirName"}, description = "Name of the created main directory", defaultValue = "converted_result", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private static String outputDirName;

    @CommandLine.Option(names = {"-iL", "--inputLayout"}, converter = Util.LayoutTypeConverter.class, description = "Directory layout type of the input data", defaultValue = "tms", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    Constants.LayoutTypes inputLayout;

    @CommandLine.Option(names = {"-oL", "--outputLayout"}, converter = Util.LayoutTypeConverter.class, description = "Target directory layout type to be converted.", defaultValue = "gwc", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    Constants.LayoutTypes outputLayout;

    private static ProgressBar progressBar;
    private static long totalFileNumber;
    private static double process = 0;
    private static ILayoutConverter inputConverter;
    private static ILayoutConverter outputConverter;

    @Override
    public Integer call() {
        if (inputLayout == outputLayout) {
            System.out.println("Input and Output layout type must be different.");
            return -1;
        }
        inputConverter = LayoutConverterFactory.getLayoutConverter(inputLayout);
        outputConverter = LayoutConverterFactory.getLayoutConverter(outputLayout);
        inputPath = Util.addSeperatorToEnd(inputPath);
        outputPath = Util.addSeperatorToEnd(outputPath) + outputDirName + File.separator;
        Util.showParametersInfo(inputPath, outputPath);
        progressBar = new ProgressBar("Converting:", 100).start();
        try {
            Supplier<Stream<Path>> streamSupplier = () -> {
                try {
                    return Files.walk(Paths.get(inputPath))
                            .parallel()
                            .filter(Files::isRegularFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            };
            totalFileNumber = streamSupplier.get().count();
            streamSupplier.get().parallel().forEach(copyFile);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        progressBar.stepTo(100);
        progressBar.setExtraMessage("Success!");
        progressBar.stop();
        return 0;
    }

    private static Consumer<Path> copyFile = file -> {
        String fileExtension = file.toString().substring(file.toString().lastIndexOf("."));
        if (!Util.tileImageExtensions.contains(fileExtension)) {
            return;
        }
        TileInfo tileInfo = inputConverter.getTileInfo(file);
        String convertedLayout = outputPath + outputConverter.convertLayout(tileInfo);
        Path copyPath = Paths.get(convertedLayout + fileExtension);
        try {
            Files.createDirectories(copyPath.getParent());
            Files.copy(file, copyPath);
            process = process + (100.0 / totalFileNumber);
            progressBar.stepTo(((long) (process)));
            progressBar.setExtraMessage("Copying: " + copyPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}

