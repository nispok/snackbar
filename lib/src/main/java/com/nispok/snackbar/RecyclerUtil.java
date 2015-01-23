package com.nispok.snackbar;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView is a provided dependency, so in order to avoid burdening developers with a
 * potentially unnecessary dependency, we move the RecyclerView-related code here and only call it
 * if we confirm that they've provided it themselves.
 */
class RecyclerUtil {
    static void setScrollListener(final Snackbar snackbar, View view) {
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                snackbar.dismiss();
            }
        });
    }
}
