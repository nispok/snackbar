package com.nispok.snackbar.listeners;

import com.nispok.snackbar.Snackbar;

/**
 * This adapter class provides empty implementations of the methods from {@link com.nispok.snackbar.listeners.EventListener}.
 * If you are only interested in a subset of the interface methods you can extend this class an override only the methods you need.
 */
public abstract class EventListenerAdapter implements EventListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShow(Snackbar snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShowByReplace(Snackbar snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShown(Snackbar snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismiss(Snackbar snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismissByReplace(Snackbar snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDismissed(Snackbar snackbar) {

    }
}
