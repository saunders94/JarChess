package com.example.jarchess.match.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;

import java.util.Collection;
import java.util.LinkedList;

class PausePendingDialog {
    private static final String TAG = "PausePendingDialog";

    private final MatchActivity activity;
    private final ViewGroup viewGroup;
    private final Button button;
    private final ProgressBar progressBar;
    private final TextView pausedTextView;
    private final TextView pauseRequestPendingTextView;
    private final TextView resumeRequestPendingTextView;
    private final TextView resumeTextView;
    private final Collection<TextView> textViews = new LinkedList<>();
    private View backgroundFadeView;

    public PausePendingDialog(MatchActivity matchActivity) {
        this.activity = matchActivity;
        viewGroup = activity.findViewById(R.id.pendingPauseDialog);
        pausedTextView = viewGroup.findViewById(R.id.pausePendingTitleTextView_Paused);
        textViews.add(pausedTextView);
        pauseRequestPendingTextView = viewGroup.findViewById(R.id.pausePendingTitleTextView_PauseRequestPending);
        textViews.add(pauseRequestPendingTextView);
        resumeTextView = viewGroup.findViewById(R.id.pausePendingTitleTextView_Resume);
        textViews.add(resumeTextView);
        resumeRequestPendingTextView = viewGroup.findViewById(R.id.pausePendingTitleTextView_ResumeRequestPending);
        textViews.add(resumeRequestPendingTextView);
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

    private void setTextView(TextView textView) {
        for (TextView t : textViews) {
            if (t != textView) {
                t.setVisibility(View.GONE);
            }
        }

        textView.setVisibility(View.VISIBLE);
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
                setTextView(pausedTextView);

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
                setTextView(pauseRequestPendingTextView);
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
                setTextView(resumeRequestPendingTextView);
                backgroundFadeView.setVisibility(View.VISIBLE);
                viewGroup.setVisibility(View.VISIBLE);
            }
        });
    }


}
