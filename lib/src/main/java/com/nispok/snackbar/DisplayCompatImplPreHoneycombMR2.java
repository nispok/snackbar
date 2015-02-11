package com.nispok.snackbar;

import android.graphics.Point;
import android.view.Display;

@SuppressWarnings("deprecation")
class DisplayCompatImplPreHoneycombMR2 extends DisplayCompat.Impl {
    @Override
    void getSize(Display display, Point outSize) {
        outSize.x = display.getWidth();
        outSize.y = display.getHeight();
    }

    @Override
    void getRealSize(Display display, Point outSize) {
        outSize.x = display.getWidth();
        outSize.y = display.getHeight();
    }
}

