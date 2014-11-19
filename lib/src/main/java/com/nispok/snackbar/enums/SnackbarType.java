package com.nispok.snackbar.enums;

public enum SnackbarType {

    SINGLE_LINE(56, 1), MULTI_LINE(80, 2);

    private int height;
    private int maxLines;

    SnackbarType(int height, int maxLines) {
        this.height = height;
        this.maxLines = maxLines;
    }

    public int getHeight() {
        return height;
    }

    public int getHeightInPx(float scale) {
        return (int) (height * scale + 0.5f);
    }

    public int getMaxLines() {
        return maxLines;
    }
}
