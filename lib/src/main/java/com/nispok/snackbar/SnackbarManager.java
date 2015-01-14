package com.nispok.snackbar;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * A handler for multiple {@link Snackbar}s
 */
public class SnackbarManager {

    private static final String TAG = SnackbarManager.class.getSimpleName();

    private static Snackbar currentSnackbar;

    private SnackbarManager() {
    }

    /**
     * Displays a {@link com.nispok.snackbar.Snackbar} in the current {@link Activity}, dismissing
     * the current Snackbar being displayed, if any. Note that the Activity will be obtained from
     * the Snackbar's {@link android.content.Context}. If the Snackbar was created with
     * {@link android.app.Activity#getApplicationContext()} then you must explicitly pass the target
     * Activity using {@link #show(Snackbar, android.app.Activity)}
     *
     * @param snackbar instance of {@link com.nispok.snackbar.Snackbar} to display
     */
    public static void show(@NonNull Snackbar snackbar) {
        try {
            show(snackbar, (Activity) snackbar.getContext());
        } catch (ClassCastException e) {
            Log.e(TAG, "Couldn't get Activity from the Snackbar's Context. Try calling " +
                    "#show(Snackbar, Activity) instead", e);
        }
    }

    /**
     * Displays a {@link com.nispok.snackbar.Snackbar} in the current {@link Activity}, dismissing
     * the current Snackbar being displayed, if any
     *
     * @param snackbar instance of {@link com.nispok.snackbar.Snackbar} to display
     * @param activity target {@link Activity} to display the Snackbar
     */
    public static void show(@NonNull Snackbar snackbar, @NonNull Activity activity) {
        if (currentSnackbar != null) {
            currentSnackbar.dismiss();
        }
        currentSnackbar = snackbar;
        currentSnackbar.show(activity);
    }

    /**
     * Dismisses the {@link com.nispok.snackbar.Snackbar} shown by this manager.
     */
    public static void dismiss() {
        if (currentSnackbar != null) {
            currentSnackbar.dismiss();
        }
    }
    
    /**
     * Return the current Snackbar
     */
     public static Snackbar getCurrentSnackbar() {
         return currentSnackbar;
     }
}
