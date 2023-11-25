package com.aknbb.tilelayoutconverter.LayoutConverters;

import java.io.File;
import java.nio.file.Path;

public interface ILayoutConverter {
    String separator = File.separator;

    TileInfo getTileInfo(Path file);

    String convertLayout(TileInfo input);
}
