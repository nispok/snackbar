package com.nispok.snackbar.listeners;

import com.nispok.snackbar.Snackbar;

/**
 * Interface used to notify of all {@link com.nispok.snackbar.Snackbar} display events. Useful if you want
 * to move other views while the Snackbar is on screen.
 */
public interface EventListener {
    /**
     * Called when a {@link com.nispok.snackbar.Snackbar} is about to enter the screen
     *
     * @param snackbar the {@link com.nispok.snackbar.Snackbar} that's being shown
     */
    public void onShow(Snackbar snackbar);

    /**
     * Called when a {@link com.nispok.snackbar.Snackbar} had just been dismissed
     *
     * @param snackbar the {@link com.nispok.snackbar.Snackbar} that's being dismissed
     */
    public void onDismiss(Snackbar snackbar);
}
