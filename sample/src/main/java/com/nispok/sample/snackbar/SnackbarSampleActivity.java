package com.nispok.sample.snackbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nispok.sample.snackbar.utils.SnackbarManager;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;

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
                SnackbarManager.getInstance().show(
                        Snackbar.with(SnackbarSampleActivity.this)
                                .text("Single-line snackbar"),
                        SnackbarSampleActivity.this);
            }
        });

        Button singleLineWithActionButton = (Button) findViewById(R.id.single_line_with_action);
        singleLineWithActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarManager.getInstance().show(
                        Snackbar.with(SnackbarSampleActivity.this)
                                .text("Something has been done")
                                .actionLabel("Undo")
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked() {
                                        Toast.makeText(SnackbarSampleActivity.this,
                                                "Action undone",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }),
                        SnackbarSampleActivity.this);
            }
        });

        Button multiLineButton = (Button) findViewById(R.id.multi_line);
        multiLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarManager.getInstance().show(
                        Snackbar.with(SnackbarSampleActivity.this)
                                .type(SnackbarType.MULTI_LINE)
                                .text("This is a multi-line snackbar. Keep in mind that snackbars" +
                                        " are meant for VERY short messages"),
                        SnackbarSampleActivity.this);
            }
        });

        Button multiLineWithActionButton = (Button) findViewById(R.id.multi_line_with_action);
        multiLineWithActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarManager.getInstance().show(
                        Snackbar.with(SnackbarSampleActivity.this)
                                .type(SnackbarType.MULTI_LINE)
                                .text("This is a multi-line snackbar with an action button. Note " +
                                        "that multi-line snackbars are 2 lines max")
                                .actionLabel("Action")
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked() {
                                        Toast.makeText(SnackbarSampleActivity.this,
                                                "Action clicked",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }),
                        SnackbarSampleActivity.this);
            }
        });

        Button noAnimationButton = (Button) findViewById(R.id.no_animation);
        noAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarManager.getInstance().show(
                        Snackbar.with(SnackbarSampleActivity.this)
                                .text("No animation :(")
                                .animation(false)
                                .duration(1000l),
                        SnackbarSampleActivity.this);
            }
        });

        Button eventListenerButton = (Button) findViewById(R.id.event_listener);
        eventListenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarManager.getInstance().show(
                        Snackbar.with(SnackbarSampleActivity.this)
                                .text("I'm showing a toast on exit")
                                .eventListener(new EventListener() {
                                    @Override
                                    public void onShow(int height) {
                                        Log.i(TAG, "Snackbar will show. Height: " + height);
                                    }

                                    @Override
                                    public void onDismiss(int height) {
                                        Toast.makeText(SnackbarSampleActivity.this,
                                                "Snackbar dismissed. Height in DP: " + height,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }),
                        SnackbarSampleActivity.this);
            }
        });

        Button customColorsButton = (Button) findViewById(R.id.custom_colors);
        customColorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarManager.getInstance().show(
                        Snackbar.with(SnackbarSampleActivity.this)
                                .text("Different colors")
                                .textColor(Color.parseColor("#ff9d9d9c"))
                                .color(Color.parseColor("#ff914300"))
                                .actionLabel("Action")
                                .actionColor(Color.parseColor("#ff5a2900"))
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked() {
                                        Log.i(TAG, "Action touched");
                                    }
                                })
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT),
                        SnackbarSampleActivity.this);
            }
        });

        Button swipeableButton = (Button) findViewById(R.id.swipeable);
        swipeableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarManager.getInstance().show(
                        Snackbar.with(SnackbarSampleActivity.this)
                                .text("Swipe me off the screen")
                                .swipeToDismiss(true),
                        SnackbarSampleActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_go_to_repo) {
            goToRepo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToRepo() {
        Uri uri = Uri.parse(getString(R.string.repo_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}