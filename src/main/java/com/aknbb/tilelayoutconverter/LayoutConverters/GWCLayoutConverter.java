package com.aknbb.tilelayoutconverter.LayoutConverters;

import java.nio.file.Path;

public class GWCLayoutConverter implements ILayoutConverter {
    private boolean yIncreaseBottomToTop = true;
    private final String ZOOM_SRS_PREFIX = "EPSG_4326_";

    @Override
    public TileInfo getTileInfo(Path file) {
        String fileNameWithExtension = file.getFileName().toString();
        String zoom = file.getParent().getParent().getFileName().toString().replaceAll(ZOOM_SRS_PREFIX, "");
        zoom = String.valueOf(Integer.parseInt(zoom) + 1);
        int underscoreIndex = fileNameWithExtension.indexOf('_');
        String x = fileNameWithExtension.substring(0, underscoreIndex);
        String y = fileNameWithExtension.substring(underscoreIndex + 1, fileNameWithExtension.lastIndexOf("."));
        return new TileInfo(x, y, zoom, yIncreaseBottomToTop);
    }

    @Override
    public String convertLayout(TileInfo input) {
        StringBuilder path = new StringBuilder(256);
        long exponent = ((input.getParsedZoom() - 1) / 2) + 1;
        long half = (long) (Math.pow(2, exponent));
        int digits = 1;
        if (half > 10) {
            digits = (int) (Math.log10(half)) + 1;
        }
        long xc = input.getX() / half;
        long yc = input.getY(yIncreaseBottomToTop) / half;
        path.append(ZOOM_SRS_PREFIX);
        zeroPadder(Long.parseLong(input.getZoom()) - 1, 2, path);
        path.append(separator);
        zeroPadder(xc, digits, path);
        path.append('_');
        zeroPadder(yc, digits, path);
        path.append(separator);
        zeroPadder(input.getX(), 2 * digits, path);
        path.append('_');
        zeroPadder(input.getY(), 2 * digits, path);
        return path.toString();
    }

    private static void zeroPadder(long number, int order, StringBuilder padding) {
        int numberOrder = 1;
        if (number > 9) {
            if (number > 11) {
                numberOrder = (int) Math.ceil(Math.log10(number) - 0.001);
            } else {
                numberOrder = 2;
            }
        }
        int diffOrder = order - numberOrder;

        if (diffOrder > 0) {
            while (diffOrder > 0) {
                padding.append('0');
                diffOrder--;
            }
            padding.append(number);
        } else {
            padding.append(number);
        }
    }
}
