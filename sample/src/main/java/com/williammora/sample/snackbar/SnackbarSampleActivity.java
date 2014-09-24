package com.williammora.sample.snackbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.williammora.snackbar.Snackbar;

import java.lang.Override;

public class SnackbarSampleActivity extends Activity {

    private static final String TAG = SnackbarSampleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Button singleLineButton = (Button) findViewById(R.id.single_line);
        singleLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SnackbarSampleActivity.this)
                        .text("Single-line snackbar")
                        .show(SnackbarSampleActivity.this);
            }
        });

        Button singleLineWithActionButton = (Button) findViewById(R.id.single_line_with_action);
        singleLineWithActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SnackbarSampleActivity.this)
                        .text("Something has been done")
                        .actionLabel("Undo")
                        .actionListener(new Snackbar.ActionClickListener() {
                            @Override
                            public void onActionClicked() {
                                Log.i(TAG, "Action touched");
                            }
                        })
                        .show(SnackbarSampleActivity.this);
            }
        });

        Button multiLineButton = (Button) findViewById(R.id.multi_line);
        multiLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SnackbarSampleActivity.this)
                        .type(Snackbar.SnackbarType.MULTI_LINE)
                        .text("This is a multi-line snackbar. Keep in mind that snackbars are " +
                                "meant for VERY short messages")
                        .show(SnackbarSampleActivity.this);
            }
        });

        Button multiLineWithActionButton = (Button) findViewById(R.id.multi_line_with_action);
        multiLineWithActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SnackbarSampleActivity.this)
                        .type(Snackbar.SnackbarType.MULTI_LINE)
                        .text("This is a multi-line snackbar with an action button. Note that " +
                                "multi-line snackbars are 2 lines max")
                        .actionLabel("Action")
                        .actionListener(new Snackbar.ActionClickListener() {
                            @Override
                            public void onActionClicked() {
                                Log.i(TAG, "Action touched");
                            }
                        })
                        .show(SnackbarSampleActivity.this);
            }
        });

        Button noAnimationButton = (Button) findViewById(R.id.no_animation);
        noAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SnackbarSampleActivity.this)
                        .text("No animation :(")
                        .animation(false)
                        .duration(1000l)
                        .show(SnackbarSampleActivity.this);
            }
        });

        Button dismissListenerButton = (Button) findViewById(R.id.dismiss_listener);
        dismissListenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SnackbarSampleActivity.this)
                        .text("This will do something when dismissed")
                        .dismissListener(new Snackbar.DismissListener() {
                            @Override
                            public void onDismiss() {
                                Log.i(TAG, "Snackbar dismissed");
                            }
                        })
                        .show(SnackbarSampleActivity.this);
            }
        });

        Button customColorsButton = (Button) findViewById(R.id.custom_colors);
        customColorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SnackbarSampleActivity.this)
                        .text("Shorter message, different colors")
                        .textColor(Color.GREEN)
                        .color(Color.BLUE)
                        .actionLabel("Action")
                        .actionColor(Color.RED)
                        .actionListener(new Snackbar.ActionClickListener() {
                            @Override
                            public void onActionClicked() {
                                Log.i(TAG, "Action touched");
                            }
                        })
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .show(SnackbarSampleActivity.this);
            }
        });
    }

}