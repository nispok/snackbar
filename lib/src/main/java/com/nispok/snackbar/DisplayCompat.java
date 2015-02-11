package com.nispok.snackbar;

import android.graphics.Point;
import android.os.Build;
import android.view.Display;

class DisplayCompat {
    static abstract class Impl {
        abstract void getSize(Display display, Point outSize);

        abstract void getRealSize(Display display, Point outSize);
    }

    private static final Impl IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            IMPL = new DisplayCompatImplJBMR1();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            IMPL = new DisplayCompatImplHoneycombMR2();
        } else {
            IMPL = new DisplayCompatImplPreHoneycombMR2();
        }
    }

    public static void getSize(Display display, Point outSize) {
        IMPL.getSize(display, outSize);
    }

    public static void getRealSize(Display display, Point outSize) {
        IMPL.getRealSize(display, outSize);
    }
}
