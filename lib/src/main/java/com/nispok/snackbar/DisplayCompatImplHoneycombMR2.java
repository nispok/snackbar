package com.nispok.snackbar;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
class DisplayCompatImplHoneycombMR2 extends DisplayCompat.Impl {
    @Override
    void getSize(Display display, Point outSize) {
        display.getSize(outSize);
    }

    @Override
    void getRealSize(Display display, Point outSize) {
        display.getSize(outSize);
    }
}
