package com.nispok.snackbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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

    public static int getWidthFromPercentage(Activity targetActivity, Float mMaxWidthPercentage) {
        Display display = targetActivity.getWindowManager().getDefaultDisplay();
        Point dispSize = new Point();
        getRealSize(display, dispSize);

        return (int) (dispSize.x * mMaxWidthPercentage);
    }

    public static int convertDpToPixels(Context context, int dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
        return px;
    }
}
