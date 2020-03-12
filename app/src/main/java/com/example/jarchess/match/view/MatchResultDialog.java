package com.example.jarchess.match.view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.result.Result;

class MatchResultDialog {

    private final MatchActivity activity;
    private View view;
    private View backgroundFadeView;
    private TextView titleTextView;
    private TextView bodyTextView;
    private Button okButton;

    public MatchResultDialog(@NonNull MatchActivity matchActivity) {
        activity = matchActivity;


        view = activity.findViewById(R.id.matchResultFrameLayout);

        backgroundFadeView = activity.findViewById(R.id.fadeFrameLayoutBottom);
        titleTextView = view.findViewById(R.id.resultTitleTextView);
        bodyTextView = view.findViewById(R.id.resultResultTextView);

        okButton = view.findViewById(R.id.matchResultOKButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.observeResultAcknowledgement();
            }
        });

        backgroundFadeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    public void hide() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backgroundFadeView.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
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
                view.setVisibility(View.VISIBLE);
            }
        });
    }
}
