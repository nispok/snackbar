package com.williammora.snackbar;

import android.app.Activity;
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
        setContentView(R.layout.activity_sample);

        Button singleLineButton = (Button) findViewById(R.id.single_line);
        singleLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(SampleActivity.this)
                        .text("Single-line snackbar")
                        .show(SampleActivity.this);
            }
        });

        Button singleLineWithActionButton = (Button) findViewById(R.id.single_line_with_action);
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
    }

}
