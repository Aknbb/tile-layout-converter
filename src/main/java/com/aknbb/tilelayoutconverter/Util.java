package com.aknbb.tilelayoutconverter;

import picocli.CommandLine;

import java.io.File;
import java.util.Arrays;
import java.util.List;

class Util {
    static List<String> tileImageExtensions = Arrays.asList(".png", ".jpeg", ".jpg");
    private static final String INPUT_PATH_TEXT = "Input path: ";
    private static final String OUTPUT_PATH_TEXT = "Output path: ";

    static void showParametersInfo(String inputPath, String outputPath) {
        StringBuilder stringBuilder = new StringBuilder(256);
        for (int index = 0; index < Math.max(inputPath.length(), outputPath.length()) + OUTPUT_PATH_TEXT.length(); index++) {
            stringBuilder.append('=');
        }
        String row = stringBuilder.toString();
        System.out.println(row);
        System.out.println(INPUT_PATH_TEXT + inputPath);
        System.out.println(OUTPUT_PATH_TEXT + outputPath);
        System.out.println(row + "\n");
    }

    static String addSeperatorToEnd(String path) {
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        return path;
    }

    static class LayoutTypeConverter implements CommandLine.ITypeConverter<Constants.LayoutTypes> {
        public Constants.LayoutTypes convert(String value) {
            switch (value.toLowerCase()) {
                case "gwc":
                case "geowebcache":
                    return Constants.LayoutTypes.GWC;
                case "xyz":
                case "slippymap":
                    return Constants.LayoutTypes.XYZ;
                default:
                    return Constants.LayoutTypes.TMS;
            }
        }
    }

}
