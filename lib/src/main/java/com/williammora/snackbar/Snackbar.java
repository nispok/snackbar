package com.williammora.snackbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.williammora.snackbar.listeners.SwipeDismissTouchListener;

/**
 * View that provides quick feedback about an operation in a small popup at the base of the screen
 */
public class Snackbar extends RelativeLayout {

    public enum SnackbarType {
        SINGLE_LINE(56, 1), MULTI_LINE(80, 2);

        private int height;
        private int maxLines;

        SnackbarType(int height, int maxLines) {
            this.height = height;
            this.maxLines = maxLines;
        }

        public int getHeight() {
            return height;
        }

        public int getMaxLines() {
            return maxLines;
        }
    }

    public enum SnackbarDuration {
        LENGTH_SHORT(2000), LENGTH_LONG(3500);

        private long duration;

        SnackbarDuration(long duration) {
            this.duration = duration;
        }

        public long getDuration() {
            return duration;
        }
    }

    private SnackbarType mType = SnackbarType.SINGLE_LINE;
    private SnackbarDuration mDuration = SnackbarDuration.LENGTH_LONG;
    private CharSequence mText;
    private int mColor = 0xff323232;
    private int mTextColor = Color.WHITE;
    private CharSequence mActionLabel;
    private int mActionColor = Color.GREEN;
    private boolean mAnimated = true;
    private long mCustomDuration = -1;
    private ActionClickListener mActionClickListener;
    private boolean mShouldDismiss = true;
    private DismissListener mDismissListener;
    private boolean mIsShowing = false;

    private Snackbar(Context context) {
        super(context);
    }

    public static Snackbar with(Context context) {
        return new Snackbar(context);
    }

    /**
     * Sets the type of {@link Snackbar} to be displayed.
     *
     * @param type the {@link Snackbar.SnackbarType} of this instance
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
        return this;
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
     * Determines whether this {@link Snackbar} should dismiss when the action button is touched
     *
     * @param shouldDismiss
     * @return
     */
    public Snackbar dismissOnActionClicked(boolean shouldDismiss) {
        mShouldDismiss = shouldDismiss;
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
     * Sets the listener to be called when the {@link Snackbar} is dismissed.
     *
     * @param listener
     * @return
     */
    public Snackbar dismissListener(DismissListener listener) {
        mDismissListener = listener;
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
     * @param duration
     * @return
     */
    public Snackbar duration(long duration) {
        mCustomDuration = duration;
        return this;
    }

    private void init(Activity parent) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(parent)
                .inflate(R.layout.snackbar, this, true);

        layout.setBackgroundColor(mColor);

        float scale = parent.getResources().getDisplayMetrics().density;
        int height = (int) (mType.getHeight() * scale + 0.5f);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height);

        layout.setLayoutParams(params);

        TextView snackbarText = (TextView) layout.findViewById(R.id.snackbar_text);
        snackbarText.setText(mText);
        snackbarText.setTextColor(mTextColor);
        snackbarText.setMaxLines(mType.getMaxLines());

        TextView snackbarAction = (TextView) layout.findViewById(R.id.snackbar_action);
        if (!TextUtils.isEmpty(mActionLabel)) {
            requestLayout();
            snackbarAction.setText(mActionLabel);
            snackbarAction.setTextColor(mActionColor);
            snackbarAction.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionClickListener != null) {
                        mActionClickListener.onActionClicked();
                    }
                    if (mShouldDismiss) {
                        dismiss();
                    }
                }
            });
            snackbarAction.setMaxLines(mType.getMaxLines());
        } else {
            snackbarAction.setVisibility(GONE);
        }

        setClickable(true);

        setOnTouchListener(new SwipeDismissTouchListener(this, null,
                new SwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                        if (view != null) {
                            dismiss();
                        }
                    }
                }));
    }

    /**
     * Displays the {@link Snackbar} at the bottom of the
     * {@link android.app.Activity} provided.
     *
     * @param targetActivity
     */
    public void show(Activity targetActivity) {
        init(targetActivity);
        ViewGroup root = (ViewGroup) targetActivity.findViewById(android.R.id.content);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        root.addView(this, params);

        mIsShowing = true;

        if (mAnimated) {
            startSnackbarAnimation();
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, getDuration());
        }
    }

    private void startSnackbarAnimation() {
        final Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.snackbar_out);
        fadeOut.setStartOffset(getDuration());
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Animation slideIn = AnimationUtils.loadAnimation(getContext(), R.anim.snackbar_in);
        slideIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        startAnimation(fadeOut);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(slideIn);
    }

    public void dismiss() {
        clearAnimation();
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        if (mDismissListener != null && mIsShowing) {
            mDismissListener.onDismiss();
        }
        mIsShowing = false;
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

    public boolean isAnimated() {
        return mAnimated;
    }

    public boolean shouldDismissOnActionClicked() {
        return mShouldDismiss;
    }

    /**
     * Returns whether this {@link com.williammora.snackbar.Snackbar} is currently showing
     * @return
     */
    public boolean isShowing() {
        return mIsShowing;
    }

    /**
     * Returns whether this {@link com.williammora.snackbar.Snackbar} has been dismissed
     * @return
     */
    public boolean isDismissed() {
        return !mIsShowing;
    }

    public interface ActionClickListener {
        public void onActionClicked();
    }

    public interface DismissListener {
        public void onDismiss();
    }
}
