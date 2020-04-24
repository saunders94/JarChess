package com.example.jarchess.match.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;

class PausePendingDialog {
    private static final String TAG = "PausePendingDialog";

    private final MatchActivity activity;
    private final ViewGroup viewGroup;
    private final TextView textView;
    private final Button button;
    private final ProgressBar progressBar;
    private View backgroundFadeView;

    public PausePendingDialog(MatchActivity matchActivity) {
        this.activity = matchActivity;
        viewGroup = activity.findViewById(R.id.pendingPauseDialog);
        textView = viewGroup.findViewById(R.id.pausePendingTitleTextView);
        button = viewGroup.findViewById(R.id.pausePendingButton);
        progressBar = viewGroup.findViewById(R.id.pausePendingProgressBar);
        backgroundFadeView = activity.findViewById(R.id.fadeFrameLayoutBottom);
        button.setText("Resume");
    }

    public void hide() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run() called in hide()");
                Log.d(TAG, "run is running on thread: " + Thread.currentThread().getName());
                backgroundFadeView.setVisibility(View.GONE);
                viewGroup.setVisibility(View.GONE);
            }
        });
    }

    public void showPauseRequestAccepted() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run() called in showPauseRequestAccepted()");
                Log.d(TAG, "run is running on thread: " + Thread.currentThread().getName());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick: resume button");
                        activity.handleResumeButtonClick();
                    }
                });
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                textView.setText("Paused");
                backgroundFadeView.setVisibility(View.VISIBLE);
                viewGroup.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showPauseRequestPending() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run() called in showPauseRequestPending()");
                Log.d(TAG, "run is running on thread: " + Thread.currentThread().getName());
                button.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("Pause Request Pending");
                backgroundFadeView.setVisibility(View.VISIBLE);
                viewGroup.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showResumeRequestPending() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run() called in showResumeRequestPending()");
                Log.d(TAG, "run is running on thread: " + Thread.currentThread().getName());
                button.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("Resume Request Pending");
                backgroundFadeView.setVisibility(View.VISIBLE);
                viewGroup.setVisibility(View.VISIBLE);
            }
        });
    }


}
