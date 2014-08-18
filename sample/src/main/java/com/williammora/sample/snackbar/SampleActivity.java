package com.williammora.sample.snackbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wmora.snackbar.Snackbar;

public class SampleActivity extends Activity {

    private static final String TAG = SampleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.williammora.snackbar.R.layout.activity_sample);

        Button singleLineButton = (Button) findViewById(com.williammora.snackbar.R.id.single_line);
        singleLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SampleActivity.this)
                        .text("Single-line snackbar")
                        .show(SampleActivity.this);
            }
        });

        Button singleLineWithActionButton = (Button) findViewById(com.williammora.snackbar.R.id.single_line_with_action);
        singleLineWithActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SampleActivity.this)
                        .text("Something has been done")
                        .actionLabel("Undo")
                        .actionListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "Undoing something");
                            }
                        })
                        .show(SampleActivity.this);
            }
        });

        Button multiLineButton = (Button) findViewById(com.williammora.snackbar.R.id.multi_line);
        multiLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SampleActivity.this)
                        .type(Snackbar.SnackbarType.MULTI_LINE)
                        .text("This is a multi-line snackbar. Keep in mind that snackbars are " +
                                "meant for VERY short messages")
                        .show(SampleActivity.this);
            }
        });

        Button multiLineWithActionButton = (Button) findViewById(com.williammora.snackbar.R.id.multi_line_with_action);
        multiLineWithActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SampleActivity.this)
                        .type(Snackbar.SnackbarType.MULTI_LINE)
                        .text("This is a multi-line snackbar with an action button. Note that " +
                                "multi-line snackbars are 2 lines max")
                        .actionLabel("Undo")
                        .actionListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "Undoing something");
                            }
                        })
                        .show(SampleActivity.this);
            }
        });

        Button noAnimationButton = (Button) findViewById(com.williammora.snackbar.R.id.no_animation);
        noAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SampleActivity.this)
                        .text("No animation :(")
                        .animation(false)
                        .show(SampleActivity.this);
            }
        });

        Button customColorsButton = (Button) findViewById(com.williammora.snackbar.R.id.custom_colors);
        customColorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SampleActivity.this)
                        .text("Whoa :-O")
                        .textColor(Color.GREEN)
                        .color(Color.WHITE)
                        .actionLabel("Action")
                        .actionColor(Color.BLUE)
                        .actionListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "Doing something");
                            }
                        })
                        .show(SampleActivity.this);
            }
        });
    }

}