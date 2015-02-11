package com.nispok.snackbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewParent;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
class SnackbarHelperChildViewJB extends View {
    public SnackbarHelperChildViewJB(Context context) {
        super(context);
        setSaveEnabled(false);
        setWillNotDraw(true);
        setVisibility(GONE);
    }

    @Override
    public void onWindowSystemUiVisibilityChanged(int visible) {
        super.onWindowSystemUiVisibilityChanged(visible);

        final ViewParent parent = getParent();
        if (parent instanceof Snackbar) {
            ((Snackbar) parent).dispatchOnWindowSystemUiVisibilityChangedCompat(visible);
        }
    }
}
