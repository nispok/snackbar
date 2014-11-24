package com.nispok.snackbar.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SnackbarLayout extends LinearLayout {
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mMaxHeight = Integer.MAX_VALUE;

    public SnackbarLayout(Context context) {
        super(context);
    }

    public SnackbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnackbarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Adjust width as necessary
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mMaxWidth < width) {
            int mode = MeasureSpec.getMode(widthMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, mode);
        }
        // Adjust height as necessary
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mMaxHeight < height) {
            int mode = MeasureSpec.getMode(heightMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, mode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
        requestLayout();
    }

    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        requestLayout();
    }

}
