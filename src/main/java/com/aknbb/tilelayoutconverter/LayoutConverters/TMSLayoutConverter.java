package com.aknbb.tilelayoutconverter.LayoutConverters;

import java.nio.file.Path;

public class TMSLayoutConverter implements ILayoutConverter {
    boolean yIncreaseBottomToTop = true;

    @Override
    public TileInfo getTileInfo(Path file) {
        String fileNameWithExtension = file.getFileName().toString();
        String zoom = file.getParent().getParent().getFileName().toString();
        String x = file.getParent().getFileName().toString();
        String y = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf("."));
        return new TileInfo(x, y, zoom, yIncreaseBottomToTop);
    }

    @Override
    public String convertLayout(TileInfo input) {
        return input.getZoom() +
                separator +
                input.getX() +
                separator +
                input.getY(yIncreaseBottomToTop);
    }
}
