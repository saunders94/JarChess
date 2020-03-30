package com.example.jarchess.match.view;

import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.result.Result;

import static android.support.constraint.Constraints.TAG;

class MatchResultDialog {

    private static final int MINIMUM_HEIGHT = 120;
    private final MatchActivity activity;
    private ViewGroup viewGroup;
    private View backgroundFadeView;
    private TextView titleTextView;
    private TextView bodyTextView;
    private Button okButton;

    public MatchResultDialog(@NonNull MatchActivity matchActivity) {
        activity = matchActivity;


        viewGroup = activity.findViewById(R.id.matchResultFrameLayout);

        backgroundFadeView = activity.findViewById(R.id.fadeFrameLayoutBottom);
        titleTextView = viewGroup.findViewById(R.id.resultTitleTextView);
        bodyTextView = viewGroup.findViewById(R.id.resultResultTextView);

        okButton = viewGroup.findViewById(R.id.matchResultOKButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.observeResultAcknowledgement();
            }
        });

        backgroundFadeView.setOnClickListener(new View.OnClickListener() { //FIXME why doesn't this work?
            @Override
            public void onClick(View v) {
                Log.d(TAG, "backgroundFadeView.onClick is running on thread: " + Thread.currentThread().getName());
                hide();
            }
        });
    }

    public void hide() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backgroundFadeView.setVisibility(View.INVISIBLE);
                viewGroup.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void show(Result matchResult) {
        final String msg = matchResult.toString();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bodyTextView.setText(msg);
                backgroundFadeView.setVisibility(View.VISIBLE);

                if (backgroundFadeView.getTop() < MINIMUM_HEIGHT) {
                    Log.d(TAG, "Resizing viewGroup fade view");
                    viewGroup.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                }
                viewGroup.setVisibility(View.VISIBLE);
            }
        });
    }
}
