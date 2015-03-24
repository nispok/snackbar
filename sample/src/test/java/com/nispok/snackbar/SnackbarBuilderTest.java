package com.nispok.snackbar;

import android.content.Context;
import android.graphics.Color;

import com.nispok.snackbar.enums.SnackbarType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SnackbarBuilderTest {

    private Snackbar mSnackbar;
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = Robolectric.application;
    }

    @Test
    public void testSnackbarShouldBeSingleLineByDefault() {
        mSnackbar = Snackbar.with(mContext);

        assertEquals(SnackbarType.SINGLE_LINE, mSnackbar.getType());
    }

    @Test
    public void testSnackbarTypeCanBeChanged() {
        mSnackbar = Snackbar.with(mContext).type(SnackbarType.MULTI_LINE);

        assertEquals(SnackbarType.MULTI_LINE, mSnackbar.getType());
    }

    @Test
    public void testSnackbarShouldHaveLongLengthDurationSetByDefault() {
        mSnackbar = Snackbar.with(mContext);

        assertEquals(Snackbar.SnackbarDuration.LENGTH_LONG.getDuration(), mSnackbar.getDuration());
    }

    @Test
    public void testSnackbarDurationCanBeChanged() {
        mSnackbar = Snackbar.with(mContext).duration(Snackbar.SnackbarDuration.LENGTH_SHORT);

        assertEquals(Snackbar.SnackbarDuration.LENGTH_SHORT.getDuration(), mSnackbar.getDuration());
    }

    @Test
    public void testSnackbarTextColorCanBeChanged() {
        mSnackbar = Snackbar.with(mContext).textColor(Color.GREEN);

        assertEquals(Color.GREEN, mSnackbar.getTextColor());
    }

    @Test
    public void testSnackbarActionColorCanBeChanged() {
        mSnackbar = Snackbar.with(mContext).actionColor(Color.BLUE);

        assertEquals(Color.BLUE, mSnackbar.getActionColor());
    }

    @Test
    public void testSnackbarAnimationShouldBeEnabledByDefault() {
        mSnackbar = Snackbar.with(mContext);

        assertTrue(mSnackbar.isAnimated());
    }

    @Test
    public void testSnackbarAnimationCanBeDisabled() {
        mSnackbar = Snackbar.with(mContext).animation(false);

        assertFalse(mSnackbar.isAnimated());
    }

    @Test
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

    /*
    @Test
    public void testSnackbarCanBeCreatedWithColorResources() {
        mSnackbar = Snackbar.with(mContext)
                .colorResource(R.color.sb__action_bg_color)
                .textColorResource(R.color.sb__text_color)
                .actionColorResource(R.color.sb__action_bg_color);

        Resources res = mContext.getResources();
        int color = res.getColor(R.color.sb__action_bg_color);
        int textColor = res.getColor(R.color.sb__text_color);
        int actionColor = res.getColor(R.color.sb__action_bg_color);

        assertEquals(color, mSnackbar.getColor());
        assertEquals(textColor, mSnackbar.getTextColor());
        assertEquals(actionColor, mSnackbar.getActionColor());
    }
*/

    @Test
    public void testSnackbarCanBeCreatedWithCustomDuration() {
        mSnackbar = Snackbar.with(mContext)
                .duration(1000l);

        assertEquals(1000l, mSnackbar.getDuration());
    }

    @Test
    public void testSnackbarWithCustomDurationOverrideSnackbarDuration() {
        mSnackbar = Snackbar.with(mContext)
                .duration(1000l)
                .duration(Snackbar.SnackbarDuration.LENGTH_LONG);

        assertEquals(1000l, mSnackbar.getDuration());
    }

    @Test
    public void testSnackbarCanBeSetToNotDismissOnActionClicked() {
        mSnackbar = Snackbar.with(mContext)
                .dismissOnActionClicked(false);

        assertFalse(mSnackbar.shouldDismissOnActionClicked());
    }

    @Test
    public void testSnackbarShouldBeDismissedOnActionClickedByDefault() {
        mSnackbar = Snackbar.with(mContext);

        assertTrue(mSnackbar.shouldDismissOnActionClicked());
    }

    @Test
    public void testSnackbarCustomDurationMustBeGreaterThanZero() {
        mSnackbar = Snackbar.with(mContext).duration(0);

        assertFalse(mSnackbar.getDuration() == 0);
    }

}
