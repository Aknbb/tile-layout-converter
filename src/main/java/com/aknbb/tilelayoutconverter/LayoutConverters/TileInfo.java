package com.aknbb.tilelayoutconverter.LayoutConverters;

public class TileInfo {
    private long x;
    private long y;
    private String zoom;
    private boolean yIncreaseBottomToTop;

    TileInfo(String x, String y, String zoom, boolean yIncreaseBottomToTop) {
        this.x = Long.parseLong(x);
        this.y = Long.parseLong(y);
        this.zoom = zoom;
        this.yIncreaseBottomToTop = yIncreaseBottomToTop;
    }

    long getX() {
        return this.x;
    }

    long getY() {
        return this.y;
    }

    long getY(boolean yIncreaseBottomToTop) {
        return this.yIncreaseBottomToTop != yIncreaseBottomToTop ? flipOverY() : this.y;
    }

    String getZoom() {
        return this.zoom;
    }

    long getParsedZoom() {
        String result = this.zoom.contains("_") ? this.zoom.substring(this.zoom.lastIndexOf("_") + 1) : this.zoom;
        return Long.parseLong(result);
    }

    long flipOverY() {
        return (long) Math.floor(Math.pow(2, getParsedZoom()) - this.y - 1);
    }


}
