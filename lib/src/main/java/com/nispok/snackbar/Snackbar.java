package com.nispok.snackbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.layouts.SnackbarLayout;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.ActionSwipeListener;
import com.nispok.snackbar.listeners.EventListener;
import com.nispok.snackbar.listeners.SwipeDismissTouchListener;

/**
 * View that provides quick feedback about an operation in a small popup at the base of the screen
 */
public class Snackbar extends SnackbarLayout {

    public enum SnackbarDuration {
        LENGTH_SHORT(2000), LENGTH_LONG(3500), LENGTH_INDEFINITE(-1);

        private long duration;

        SnackbarDuration(long duration) {
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }
    }

    public static enum SnackbarPosition {
        TOP(Gravity.TOP), BOTTOM(Gravity.BOTTOM);

        private int layoutGravity;

        SnackbarPosition(int layoutGravity) {
            this.layoutGravity = layoutGravity;
        }

        public int getLayoutGravity() {
            return layoutGravity;
        }
    }

    private int mUndefinedColor = -10000;
    private int mUndefinedDrawable = -10000;

    private SnackbarType mType = SnackbarType.SINGLE_LINE;
    private SnackbarDuration mDuration = SnackbarDuration.LENGTH_LONG;
    private CharSequence mText;
    private TextView snackbarText;
    private int mColor = mUndefinedColor;
    private int mTextColor = mUndefinedColor;
    private int mOffset;
    private SnackbarPosition mPosition = SnackbarPosition.BOTTOM;
    private int mDrawable = mUndefinedDrawable;
    private int mMarginTop = 0;
    private int mMarginBottom = 0;
    private int mMarginLeft = 0;
    private int mMarginRight = 0;
    private long mSnackbarStart;
    private long mSnackbarFinish;
    private long mTimeRemaining = -1;
    private CharSequence mActionLabel;
    private int mActionColor = mUndefinedColor;
    private boolean mAnimated = true;
    private boolean mIsReplacePending = false;
    private boolean mIsShowingByReplace = false;
    private long mCustomDuration = -1;
    private ActionClickListener mActionClickListener;
    private ActionSwipeListener mActionSwipeListener;
    private boolean mShouldAllowMultipleActionClicks;
    private boolean mActionClicked;
    private boolean mShouldDismissOnActionClicked = true;
    private EventListener mEventListener;
    private Typeface mTextTypeface;
    private Typeface mActionTypeface;
    private boolean mIsShowing = false;
    private boolean mCanSwipeToDismiss = true;
    private boolean mIsDismissing = false;
    private Rect mWindowInsets = new Rect();
    private Rect mDisplayFrame = new Rect();
    private Point mDisplaySize = new Point();
    private Point mRealDisplaySize = new Point();
    private Activity mTargetActivity;
    private Float mMaxWidthPercentage = null;
    private boolean mUsePhoneLayout;
    private Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };
    private Runnable mRefreshLayoutParamsMarginsRunnable = new Runnable() {
        @Override
        public void run() {
            refreshLayoutParamsMargins();
        }
    };

    private Snackbar(Context context) {
        super(context);

        // inject helper view to use onWindowSystemUiVisibilityChangedCompat() event
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            addView(new SnackbarHelperChildViewJB(getContext()));
        }
    }

    public static Snackbar with(Context context) {
        return new Snackbar(context);
    }

    /**
     * Sets the type of {@link Snackbar} to be displayed.
     *
     * @param type the {@link SnackbarType} of this instance
     * @return
     */
    public Snackbar type(SnackbarType type) {
        mType = type;
        return this;
    }

    /**
     * Sets the text to be displayed in this {@link Snackbar}
     *
     * @param text
     * @return
     */
    public Snackbar text(CharSequence text) {
        mText = text;
        if (snackbarText != null) {
            snackbarText.setText(mText);
        }
        return this;
    }

    /**
     * Sets the text to be displayed in this {@link Snackbar}
     *
     * @param resId
     * @return
     */
    public Snackbar text(@StringRes int resId) {
        return text(getContext().getText(resId));
    }

    /**
     * Sets the background color of this {@link Snackbar}
     *
     * @param color
     * @return
     */
    public Snackbar color(int color) {
        mColor = color;
        return this;
    }

    /**
     * Sets the background color of this {@link Snackbar}
     *
     * @param resId
     * @return
     */
    public Snackbar colorResource(@ColorRes int resId) {
        return color(getResources().getColor(resId));
    }

    /**
     * Sets the background drawable of this {@link Snackbar}
     *
     * @param resId
     * @return
     */
    public Snackbar backgroundDrawable(@DrawableRes int resId) {
        mDrawable = resId;
        return this;
    }

    /**
     * Sets the text color of this {@link Snackbar}
     *
     * @param textColor
     * @return
     */
    public Snackbar textColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    /**
     * Sets the text color of this {@link Snackbar}
     *
     * @param resId
     * @return
     */
    public Snackbar textColorResource(@ColorRes int resId) {
        return textColor(getResources().getColor(resId));
    }

    /**
     * Sets the action label to be displayed, if any. Note that if this is not set, the action
     * button will not be displayed
     *
     * @param actionButtonLabel
     * @return
     */
    public Snackbar actionLabel(CharSequence actionButtonLabel) {
        mActionLabel = actionButtonLabel;
        return this;
    }

    /**
     * Sets the action label to be displayed, if any. Note that if this is not set, the action
     * button will not be displayed
     *
     * @param resId
     * @return
     */
    public Snackbar actionLabel(@StringRes int resId) {
        return actionLabel(getContext().getString(resId));
    }

    /**
     * Set the position of the {@link Snackbar}. Note that if this is not set, the default is to
     * show the snackbar to the bottom of the screen.
     *
     * @param position
     * @return
     */
    public Snackbar position(SnackbarPosition position) {
        mPosition = position;
        return this;
    }

    /**
     * Sets all the margins of the {@link Snackbar} to the same value, in pixels
     *
     * @param margin
     * @return
     */
    public Snackbar margin(int margin) {
        return margin(margin, margin, margin, margin);
    }

    /**
     * Sets the margins of the {@link Snackbar} in pixels such that the left and right are equal, and the top and bottom are equal
     *
     * @param marginLR
     * @param marginTB
     * @return
     */
    public Snackbar margin(int marginLR, int marginTB) {
        return margin(marginLR, marginTB, marginLR, marginTB);
    }

    /**
     * Sets all the margin of the {@link Snackbar} individually, in pixels
     *
     * @param marginLeft
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     * @return
     */
    public Snackbar margin(int marginLeft, int marginTop, int marginRight, int marginBottom) {
        mMarginLeft = marginLeft;
        mMarginTop = marginTop;
        mMarginBottom = marginBottom;
        mMarginRight = marginRight;

        return this;
    }

    /**
     * Sets the color of the action button label. Note that you must set a button label with
     * {@link Snackbar#actionLabel(CharSequence)} for this button to be displayed
     *
     * @param actionColor
     * @return
     */
    public Snackbar actionColor(int actionColor) {
        mActionColor = actionColor;
        return this;
    }

    /**
     * Sets the color of the action button label. Note that you must set a button label with
     * {@link Snackbar#actionLabel(CharSequence)} for this button to be displayed
     *
     * @param resId
     * @return
     */
    public Snackbar actionColorResource(@ColorRes int resId) {
        return actionColor(getResources().getColor(resId));
    }

    /**
     * Determines whether this {@link Snackbar} should dismiss when the action button is touched
     *
     * @param shouldDismiss
     * @return
     */
    public Snackbar dismissOnActionClicked(boolean shouldDismiss) {
        mShouldDismissOnActionClicked = shouldDismiss;
        return this;
    }

    /**
     * Sets the listener to be called when the {@link Snackbar} action is
     * selected. Note that you must set a button label with
     * {@link Snackbar#actionLabel(CharSequence)} for this button to be displayed
     *
     * @param listener
     * @return
     */
    public Snackbar actionListener(ActionClickListener listener) {
        mActionClickListener = listener;
        return this;
    }


    /**
     * Sets the listener to be called when the {@link Snackbar} is dismissed by swipe.
     *
     * @param listener
     * @return
     */
    public Snackbar swipeListener(ActionSwipeListener listener) {
        mActionSwipeListener = listener;
        return this;
    }

    /**
     * Determines whether this {@link Snackbar} should allow the action button to be
     * clicked multiple times
     *
     * @param shouldAllow
     * @return
     */
    public Snackbar allowMultipleActionClicks(boolean shouldAllow) {

        mShouldAllowMultipleActionClicks = shouldAllow;
        return this;
    }

    /**
     * Sets the listener to be called when the {@link Snackbar} is dismissed.
     *
     * @param listener
     * @return
     */
    public Snackbar eventListener(EventListener listener) {
        mEventListener = listener;
        return this;
    }

    /**
     * Sets on/off animation for this {@link Snackbar}
     *
     * @param withAnimation
     * @return
     */
    public Snackbar animation(boolean withAnimation) {
        mAnimated = withAnimation;
        return this;
    }

    /**
     * Determines whether this {@link com.nispok.snackbar.Snackbar} can be swiped off from the screen
     *
     * @param canSwipeToDismiss
     * @return
     */
    public Snackbar swipeToDismiss(boolean canSwipeToDismiss) {
        mCanSwipeToDismiss = canSwipeToDismiss;
        return this;
    }

    /**
     * Sets the duration of this {@link Snackbar}. See
     * {@link Snackbar.SnackbarDuration} for available options
     *
     * @param duration
     * @return
     */
    public Snackbar duration(SnackbarDuration duration) {
        mDuration = duration;
        return this;
    }

    /**
     * Sets a custom duration of this {@link Snackbar}
     *
     * @param duration custom duration. Value must be greater than 0 or it will be ignored
     * @return
     */
    public Snackbar duration(long duration) {
        mCustomDuration = duration > 0 ? duration : mCustomDuration;
        return this;
    }

    /**
     * Attaches this {@link Snackbar} to an AbsListView (ListView, GridView, ExpandableListView) so
     * it dismisses when the list is scrolled
     *
     * @param absListView
     * @return
     */
    public Snackbar attachToAbsListView(AbsListView absListView) {
        absListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                dismiss();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        });

        return this;
    }

    /**
     * Attaches this {@link Snackbar} to a RecyclerView so it dismisses when the list is scrolled
     *
     * @param recyclerView The RecyclerView instance to attach to.
     * @return
     */
    public Snackbar attachToRecyclerView(View recyclerView) {

        try {
            Class.forName("android.support.v7.widget.RecyclerView");

            // We got here, so now we can safely check
            RecyclerUtil.setScrollListener(this, recyclerView);
        } catch (ClassNotFoundException ignored) {
            throw new IllegalArgumentException("RecyclerView not found. Did you add it to your dependencies?");
        }

        return this;
    }

    /**
     * Use a custom typeface for this Snackbar's text
     *
     * @param typeface
     * @return
     */
    public Snackbar textTypeface(Typeface typeface) {
        mTextTypeface = typeface;
        return this;
    }

    /**
     * Use a custom typeface for this Snackbar's action label
     *
     * @param typeface
     * @return
     */
    public Snackbar actionLabelTypeface(Typeface typeface) {
        mActionTypeface = typeface;
        return this;
    }

    private static MarginLayoutParams createMarginLayoutParams(ViewGroup viewGroup, int width, int height, SnackbarPosition position) {
        if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.gravity = position.getLayoutGravity();
            return params;
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);

            if (position == SnackbarPosition.TOP)
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            else
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            return params;
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params = new LayoutParams(width, height);
            params.gravity = position.getLayoutGravity();
            return params;
        } else {
            throw new IllegalStateException("Requires FrameLayout or RelativeLayout for the parent of Snackbar");
        }
    }

    static boolean shouldUsePhoneLayout(Context context) {
        if (context == null) {
            return true;
        } else {
            return context.getResources().getBoolean(R.bool.sb__is_phone);
        }
    }

    private MarginLayoutParams init(Context context, Activity targetActivity, ViewGroup parent, boolean usePhoneLayout) {
        SnackbarLayout layout = (SnackbarLayout) LayoutInflater.from(context)
                .inflate(R.layout.sb__template, this, true);

        Resources res = getResources();
        mColor = mColor != mUndefinedColor ? mColor : res.getColor(R.color.sb__background);
        mOffset = res.getDimensionPixelOffset(R.dimen.sb__offset);
        mUsePhoneLayout = usePhoneLayout;
        float scale = res.getDisplayMetrics().density;

        MarginLayoutParams params;
        if (mUsePhoneLayout) {
            // Phone
            layout.setMinimumHeight(dpToPx(mType.getMinHeight(), scale));
            layout.setMaxHeight(dpToPx(mType.getMaxHeight(), scale));
            layout.setBackgroundColor(mColor);
            params = createMarginLayoutParams(
                    parent, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, mPosition);
        } else {
            // Tablet/desktop
            mType = SnackbarType.SINGLE_LINE; // Force single-line
            layout.setMinimumWidth(res.getDimensionPixelSize(R.dimen.sb__min_width));
            layout.setMaxWidth(
                    mMaxWidthPercentage == null
                    ? res.getDimensionPixelSize(R.dimen.sb__max_width)
                    : DisplayCompat.getWidthFromPercentage(targetActivity , mMaxWidthPercentage));
            layout.setBackgroundResource(R.drawable.sb__bg);
            GradientDrawable bg = (GradientDrawable) layout.getBackground();
            bg.setColor(mColor);

            params = createMarginLayoutParams(
                    parent, FrameLayout.LayoutParams.WRAP_CONTENT, dpToPx(mType.getMaxHeight(), scale), mPosition);
        }

        if (mDrawable != mUndefinedDrawable)
            setBackgroundDrawable(layout, res.getDrawable(mDrawable));

        snackbarText = (TextView) layout.findViewById(R.id.sb__text);
        snackbarText.setText(mText);
        snackbarText.setTypeface(mTextTypeface);

        if (mTextColor != mUndefinedColor) {
            snackbarText.setTextColor(mTextColor);
        }

        snackbarText.setMaxLines(mType.getMaxLines());

        TextView snackbarAction = (TextView) layout.findViewById(R.id.sb__action);
        if (!TextUtils.isEmpty(mActionLabel)) {
            requestLayout();
            snackbarAction.setText(mActionLabel);
            snackbarAction.setTypeface(mActionTypeface);

            if (mActionColor != mUndefinedColor) {
                snackbarAction.setTextColor(mActionColor);
            }

            snackbarAction.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionClickListener != null) {

                        // Before calling the onActionClicked() callback, make sure:
                        // 1) The snackbar is not dismissing
                        // 2) If we aren't allowing multiple clicks, that this is the first click
                        if (!mIsDismissing && (!mActionClicked || mShouldAllowMultipleActionClicks)) {

                            mActionClickListener.onActionClicked(Snackbar.this);
                            mActionClicked = true;
                        }
                    }
                    if (mShouldDismissOnActionClicked) {
                        dismiss();
                    }
                }
            });
            snackbarAction.setMaxLines(mType.getMaxLines());
        } else {
            snackbarAction.setVisibility(GONE);
        }

        setClickable(true);

        if (mCanSwipeToDismiss && res.getBoolean(R.bool.sb__is_swipeable)) {
            setOnTouchListener(new SwipeDismissTouchListener(this, null,
                    new SwipeDismissTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(Object token) {
                            return true;
                        }

                        @Override
                        public void onDismiss(View view, Object token) {
                            if (view != null) {
                                if (mActionSwipeListener != null) {
                                    mActionSwipeListener.onSwipeToDismiss();
                                }
                                dismiss(false);
                            }
                        }

                        @Override
                        public void pauseTimer(boolean shouldPause) {
                            if (isIndefiniteDuration()) {
                                return;
                            }
                            if (shouldPause) {
                                removeCallbacks(mDismissRunnable);

                                mSnackbarFinish = System.currentTimeMillis();
                            } else {
                                mTimeRemaining -= (mSnackbarFinish - mSnackbarStart);

                                startTimer(mTimeRemaining);
                            }
                        }
                    }));
        }

        return params;
    }


    private void updateWindowInsets(Activity targetActivity, Rect outInsets) {
        outInsets.left = outInsets.top = outInsets.right = outInsets.bottom = 0;

        if (targetActivity == null) {
            return;
        }

        ViewGroup decorView = (ViewGroup) targetActivity.getWindow().getDecorView();
        Display display = targetActivity.getWindowManager().getDefaultDisplay();

        boolean isTranslucent = isNavigationBarTranslucent(targetActivity);
        boolean isHidden = isNavigationBarHidden(decorView);

        Rect dispFrame = mDisplayFrame;
        Point realDispSize = mRealDisplaySize;
        Point dispSize = mDisplaySize;

        decorView.getWindowVisibleDisplayFrame(dispFrame);

        DisplayCompat.getRealSize(display, realDispSize);
        DisplayCompat.getSize(display, dispSize);

        if (dispSize.x < realDispSize.x) {
            // navigation bar is placed on right side of the screen
            if (isTranslucent || isHidden) {
                int navBarWidth = realDispSize.x - dispSize.x;
                int overlapWidth = realDispSize.x - dispFrame.right;
                outInsets.right = Math.max(Math.min(navBarWidth, overlapWidth), 0);
            }
        } else if (dispSize.y < realDispSize.y) {
            // navigation bar is placed on bottom side of the screen

            if (isTranslucent || isHidden) {
                int navBarHeight = realDispSize.y - dispSize.y;
                int overlapHeight = realDispSize.y - dispFrame.bottom;
                outInsets.bottom = Math.max(Math.min(navBarHeight, overlapHeight), 0);
            }
        }
    }

    private static int dpToPx(int dp, float scale) {
        return (int) (dp * scale + 0.5f);
    }

    public void showByReplace(Activity targetActivity) {
        mIsShowingByReplace = true;
        show(targetActivity);
    }

    public void showByReplace(ViewGroup parent) {
        mIsShowingByReplace = true;
        show(parent, shouldUsePhoneLayout(parent.getContext()));
    }

    public void showByReplace(ViewGroup parent, boolean usePhoneLayout) {
        mIsShowingByReplace = true;
        show(parent, usePhoneLayout);
    }

    /**
     * Displays the {@link Snackbar} at the bottom of the
     * {@link android.app.Activity} provided.
     *
     * @param targetActivity
     */
    public void show(Activity targetActivity) {
        ViewGroup root = (ViewGroup) targetActivity.findViewById(android.R.id.content);
        boolean usePhoneLayout = shouldUsePhoneLayout(targetActivity);
        MarginLayoutParams params = init(targetActivity, targetActivity, root, usePhoneLayout);
        updateLayoutParamsMargins(targetActivity, params);
        showInternal(targetActivity, params, root);
    }

    /**
     * Displays the {@link Snackbar} at the bottom of the
     * {@link android.view.ViewGroup} provided.
     *
     * @param parent
     */
    public void show(ViewGroup parent) {
        show(parent, shouldUsePhoneLayout(parent.getContext()));
    }

    /**
     * Displays the {@link Snackbar} at the bottom of the
     * {@link android.view.ViewGroup} provided.
     *
     * @param parent
     * @param usePhoneLayout
     */
    public void show(ViewGroup parent, boolean usePhoneLayout) {
        MarginLayoutParams params = init(parent.getContext(), null, parent, usePhoneLayout);
        updateLayoutParamsMargins(null, params);
        showInternal(null, params, parent);
    }

    public Snackbar maxWidthPercentage(float maxWidthPercentage) {
        mMaxWidthPercentage = maxWidthPercentage;
        return this;
    }

    private void showInternal(Activity targetActivity, MarginLayoutParams params, ViewGroup parent) {
        parent.removeView(this);

        parent.addView(this, params);

        bringToFront();

        // As requested in the documentation for bringToFront()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            parent.requestLayout();
            parent.invalidate();
        }

        mIsShowing = true;
        mTargetActivity = targetActivity;

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                if (mEventListener != null) {
                    if (mIsShowingByReplace) {
                        mEventListener.onShowByReplace(Snackbar.this);
                    } else {
                        mEventListener.onShow(Snackbar.this);
                    }
                    if (!mAnimated) {
                        mEventListener.onShown(Snackbar.this);
                        mIsShowingByReplace = false; // reset flag
                    }
                }
                return true;
            }
        });

        if (!mAnimated) {
            if (shouldStartTimer()) {
                startTimer();
            }
            return;
        }

        Animation slideIn = AnimationUtils.loadAnimation(getContext(), getInAnimationResource(mPosition));
        slideIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mEventListener != null) {
                    mEventListener.onShown(Snackbar.this);
                    mIsShowingByReplace = false; // reset flag
                }

                focusForAccessibility(snackbarText);

                post(new Runnable() {
                    @Override
                    public void run() {
                        mSnackbarStart = System.currentTimeMillis();

                        if (mTimeRemaining == -1) {
                            mTimeRemaining = getDuration();
                        }
                        if (shouldStartTimer()) {
                            startTimer();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(slideIn);
    }

    private void focusForAccessibility(View view) {
        final AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_VIEW_FOCUSED);

        AccessibilityEventCompat.asRecord(event).setSource(view);

        view.sendAccessibilityEventUnchecked(event);
    }

    private boolean shouldStartTimer() {
        return !isIndefiniteDuration();
    }

    private boolean isIndefiniteDuration() {
        return getDuration() == SnackbarDuration.LENGTH_INDEFINITE.getDuration();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean isNavigationBarHidden(ViewGroup root) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return false;
        }

        int viewFlags = root.getWindowSystemUiVisibility();
        return (viewFlags & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) ==
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }

    private boolean isNavigationBarTranslucent(Activity targetActivity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }

        int flags = targetActivity.getWindow().getAttributes().flags;
        return (flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) != 0;
    }

    private void startTimer() {
        postDelayed(mDismissRunnable, getDuration());
    }

    private void startTimer(long duration) {
        postDelayed(mDismissRunnable, duration);
    }

    public void dismissByReplace() {
        mIsReplacePending = true;
        dismiss();
    }

    public void dismiss() {
        dismiss(mAnimated);
    }

    private void dismiss(boolean animate) {
        if (mIsDismissing) {
            return;
        }

        mIsDismissing = true;

        if (mEventListener != null && mIsShowing) {
            if (mIsReplacePending) {
                mEventListener.onDismissByReplace(Snackbar.this);
            } else {
                mEventListener.onDismiss(Snackbar.this);
            }
        }

        if (!animate) {
            finish();
            return;
        }

        final Animation slideOut = AnimationUtils.loadAnimation(getContext(), getOutAnimationResource(mPosition));
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(slideOut);
    }

    private void finish() {
        clearAnimation();
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        if (mEventListener != null && mIsShowing) {
            mEventListener.onDismissed(this);
        }
        mIsShowing = false;
        mIsReplacePending = false;
        mTargetActivity = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDismissRunnable != null) {
            removeCallbacks(mDismissRunnable);
        }
        if (mRefreshLayoutParamsMarginsRunnable != null) {
            removeCallbacks(mRefreshLayoutParamsMarginsRunnable);
        }
    }

    void dispatchOnWindowSystemUiVisibilityChangedCompat(int visible) {
        onWindowSystemUiVisibilityChangedCompat(visible);
    }

    protected void onWindowSystemUiVisibilityChangedCompat(int visible) {
        if (mRefreshLayoutParamsMarginsRunnable != null) {
            post(mRefreshLayoutParamsMarginsRunnable);
        }
    }

    protected void refreshLayoutParamsMargins() {
        if (mIsDismissing) {
            return;
        }

        ViewGroup parent = (ViewGroup) getParent();
        if (parent == null) {
            return;
        }

        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();

        updateLayoutParamsMargins(mTargetActivity, params);

        setLayoutParams(params);
    }

    protected void updateLayoutParamsMargins(Activity targetActivity, MarginLayoutParams params) {
        if (mUsePhoneLayout) {
            // Phone
            params.topMargin = mMarginTop;
            params.rightMargin = mMarginRight;
            params.leftMargin = mMarginLeft;
            params.bottomMargin = mMarginBottom;
        } else {
            // Tablet/desktop
            params.topMargin = mMarginTop;
            params.rightMargin = mMarginRight;
            params.leftMargin = mMarginLeft + mOffset;
            params.bottomMargin = mMarginBottom + mOffset;
        }

        // Add bottom/right margin when navigation bar is hidden or translucent
        updateWindowInsets(targetActivity, mWindowInsets);

        params.rightMargin += mWindowInsets.right;
        params.bottomMargin += mWindowInsets.bottom;
    }


    public int getActionColor() {
        return mActionColor;
    }

    public CharSequence getActionLabel() {
        return mActionLabel;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getColor() {
        return mColor;
    }

    public CharSequence getText() {
        return mText;
    }

    public long getDuration() {
        return mCustomDuration == -1 ? mDuration.getDuration() : mCustomDuration;
    }

    public SnackbarType getType() {
        return mType;
    }

    /**
     * @return whether the action button has been clicked. In other words, this method will let
     * you know if {@link com.nispok.snackbar.listeners.ActionClickListener#onActionClicked(Snackbar)}
     * was called. This is useful, for instance, if you want to know during
     * {@link com.nispok.snackbar.listeners.EventListener#onDismiss(Snackbar)} if the
     * {@link com.nispok.snackbar.Snackbar} is being dismissed because of its action click
     */
    public boolean isActionClicked() {
        return mActionClicked;
    }

    /**
     * @return the pixel offset of this {@link com.nispok.snackbar.Snackbar} from the left and
     * bottom of the {@link android.app.Activity}.
     */
    public int getOffset() {
        return mOffset;
    }

    public boolean isAnimated() {
        return mAnimated;
    }

    public boolean shouldDismissOnActionClicked() {
        return mShouldDismissOnActionClicked;
    }

    /**
     * @return true if this {@link com.nispok.snackbar.Snackbar} is currently showing
     */
    public boolean isShowing() {
        return mIsShowing;
    }

    /**
     * @return true if this {@link com.nispok.snackbar.Snackbar} is dismissing.
     */
    public boolean isDimissing() {
        return mIsDismissing;
    }

    /**
     * @return false if this {@link com.nispok.snackbar.Snackbar} has been dismissed
     */
    public boolean isDismissed() {
        return !mIsShowing;
    }

    /**
     * @param snackbarPosition
     * @return the animation resource used by this {@link com.nispok.snackbar.Snackbar} instance
     * to enter the view
     */
    @AnimRes
    public static int getInAnimationResource(SnackbarPosition snackbarPosition) {
        return snackbarPosition == SnackbarPosition.TOP ? R.anim.sb__top_in : R.anim.sb__bottom_in;
    }

    /**
     * @param snackbarPosition
     * @return the animation resource used by this {@link com.nispok.snackbar.Snackbar} instance
     * to exit the view
     */
    @AnimRes
    public static int getOutAnimationResource(SnackbarPosition snackbarPosition) {
        return snackbarPosition == SnackbarPosition.TOP ? R.anim.sb__top_out : R.anim.sb__bottom_out;
    }

    /**
     * Set a Background Drawable using the appropriate Android version api call
     *
     * @param view
     * @param drawable
     */
    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }
}
