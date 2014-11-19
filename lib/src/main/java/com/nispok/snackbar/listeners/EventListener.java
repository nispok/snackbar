package com.nispok.snackbar.listeners;

/**
 * Interface used to notify of all {@link com.nispok.snackbar.Snackbar} display events. Useful if you want
 * to move other views while the Snackbar is on screen.
 */
public interface EventListener {
    /**
     * Called when a {@link com.nispok.snackbar.Snackbar} is about to enter the screen
     *
     * @param height {@link com.nispok.snackbar.Snackbar} height, in DP
     */
    public void onShow(int height);

    /**
     * Called when a {@link com.nispok.snackbar.Snackbar} had just been dismissed
     *
     * @param height {@link com.nispok.snackbar.Snackbar} height, in DP
     */
    public void onDismiss(int height);
}
