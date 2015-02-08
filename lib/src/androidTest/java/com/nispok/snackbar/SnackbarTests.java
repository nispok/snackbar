package com.nispok.snackbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.test.InstrumentationTestCase;

import com.nispok.snackbar.enums.SnackbarType;

public class SnackbarTests extends InstrumentationTestCase {

    private Snackbar mSnackbar;
    private Context mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
    }

    public void testSnackbarShouldBeSingleLineByDefault() {
        mSnackbar = Snackbar.with(mContext);

        assertEquals(SnackbarType.SINGLE_LINE, mSnackbar.getType());
    }

    public void testSnackbarTypeCanBeChanged() {
        mSnackbar = Snackbar.with(mContext).type(SnackbarType.MULTI_LINE);

        assertEquals(SnackbarType.MULTI_LINE, mSnackbar.getType());
    }

    public void testSnackbarShouldHaveLongLengthDurationSetByDefault() {
        mSnackbar = Snackbar.with(mContext);

        assertEquals(Snackbar.SnackbarDuration.LENGTH_LONG.getDuration(), mSnackbar.getDuration());
    }

    public void testSnackbarDurationCanBeChanged() {
        mSnackbar = Snackbar.with(mContext).duration(Snackbar.SnackbarDuration.LENGTH_SHORT);

        assertEquals(Snackbar.SnackbarDuration.LENGTH_SHORT.getDuration(), mSnackbar.getDuration());
    }

    public void testSnackbarTextColorCanBeChanged() {
        mSnackbar = Snackbar.with(mContext).textColor(Color.GREEN);

        assertEquals(Color.GREEN, mSnackbar.getTextColor());
    }

    public void testSnackbarActionColorCanBeChanged() {
        mSnackbar = Snackbar.with(mContext).actionColor(Color.BLUE);

        assertEquals(Color.BLUE, mSnackbar.getActionColor());
    }

    public void testSnackbarAnimationShouldBeEnabledByDefault() {
        mSnackbar = Snackbar.with(mContext);

        assertTrue(mSnackbar.isAnimated());
    }

    public void testSnackbarAnimationCanBeDisabled() {
        mSnackbar = Snackbar.with(mContext).animation(false);

        assertFalse(mSnackbar.isAnimated());
    }

    public void testSnackbarCanBeCreatedWithMultipleCustomConfiguration() {
        mSnackbar = Snackbar.with(mContext)
                .color(Color.BLUE)
                .textColor(Color.GREEN)
                .text("Aloha!")
                .actionLabel("Action")
                .type(SnackbarType.MULTI_LINE)
                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT);

        assertEquals(Color.BLUE, mSnackbar.getColor());
        assertEquals(Color.GREEN, mSnackbar.getTextColor());
        assertEquals("Aloha!", mSnackbar.getText());
        assertEquals("Action", mSnackbar.getActionLabel());
        assertEquals(SnackbarType.MULTI_LINE, mSnackbar.getType());
        assertEquals(Snackbar.SnackbarDuration.LENGTH_SHORT.getDuration(), mSnackbar.getDuration());
    }

    public void testSnackbarCanBeCreatedWithColorResources() {
        mSnackbar = Snackbar.with(mContext)
                .colorResource(R.color.sb__action_bg_color)
                .textColorResource(R.color.sb__text_color)
                .actionColorResource(R.color.sb__action_bg_color);

        Resources res = getInstrumentation().getTargetContext().getResources();
        int color = res.getColor(R.color.sb__action_bg_color);
        int textColor = res.getColor(R.color.sb__text_color);
        int actionColor = res.getColor(R.color.sb__action_bg_color);

        assertEquals(color, mSnackbar.getColor());
        assertEquals(textColor, mSnackbar.getTextColor());
        assertEquals(actionColor, mSnackbar.getActionColor());
    }

    public void testSnackbarCanBeCreatedWithCustomDuration() {
        mSnackbar = Snackbar.with(mContext)
                .duration(1000l);

        assertEquals(1000l, mSnackbar.getDuration());
    }

    public void testSnackbarWithCustomDurationOverrideSnackbarDuration() {
        mSnackbar = Snackbar.with(mContext)
                .duration(1000l)
                .duration(Snackbar.SnackbarDuration.LENGTH_LONG);

        assertEquals(1000l, mSnackbar.getDuration());
    }

    public void testSnackbarCanBeSetToNotDismissOnActionClicked() {
        mSnackbar = Snackbar.with(mContext)
                .dismissOnActionClicked(false);

        assertFalse(mSnackbar.shouldDismissOnActionClicked());
    }

    public void testSnackbarShouldBeDismissedOnActionClickedByDefault() {
        mSnackbar = Snackbar.with(mContext);

        assertTrue(mSnackbar.shouldDismissOnActionClicked());
    }

    public void testSnackbarCustomDurationMustBeGreaterThanZero() {
        mSnackbar = Snackbar.with(mContext).duration(0);

        assertFalse(mSnackbar.getDuration() == 0);
    }

}
