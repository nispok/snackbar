package com.nispok.sample.snackbar.utils;

import android.app.Activity;

import com.nispok.snackbar.Snackbar;

public class SnackbarManager {

    private static SnackbarManager INSTANCE = new SnackbarManager();

    public static SnackbarManager getInstance() {
        return INSTANCE;
    }

    private Snackbar currentSnackbar;

    private SnackbarManager() {
    }

    public void show(Snackbar snackbar) {
        if (currentSnackbar != null) {
            currentSnackbar.dismiss();
        }
        currentSnackbar = snackbar;
        currentSnackbar.show((Activity) currentSnackbar.getContext());
    }
}
