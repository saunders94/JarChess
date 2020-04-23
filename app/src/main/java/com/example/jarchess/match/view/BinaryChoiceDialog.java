package com.example.jarchess.match.view;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;

abstract class BinaryChoiceDialog {
    private final TextView titleTextView;
    private static final int MINIMUM_HEIGHT = 120;
    protected final MatchActivity activity;
    private final TextView bodyTextView;
    private final String title;
    private final String buttonText1;
    private final String body;
    private final Button[] buttons = new Button[2];
    private ViewGroup viewGroup;
    private View backgroundFadeView;
    private boolean aDialogIsOpen = false;
    private String buttonText0;

    public BinaryChoiceDialog(@NonNull MatchActivity matchActivity, String title, String body, final String buttonText0, final String buttonText1) {
        activity = matchActivity;
        this.title = title;
        this.body = body;
        this.buttonText0 = buttonText0;
        this.buttonText1 = buttonText1;


        viewGroup = activity.findViewById(R.id.binaryChoiceDialogFrameLayout);
        titleTextView = activity.findViewById(R.id.binaryTitleTextView);
        bodyTextView = activity.findViewById(R.id.binaryBodyTextView);
        backgroundFadeView = activity.findViewById(R.id.fadeFrameLayoutBottom);

        buttons[0] = viewGroup.findViewById(R.id.binaryChoice0Button);
        buttons[1] = viewGroup.findViewById(R.id.binaryChoice1Button);

        backgroundFadeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

    }

    public MatchActivity getActivity() {
        return activity;
    }

    public synchronized void hide() {
        if (aDialogIsOpen) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    backgroundFadeView.setVisibility(View.INVISIBLE);
                    viewGroup.setVisibility(View.INVISIBLE);

                }
            });
        }
    }

    protected abstract void onClickOption(String buttonTextClicked);

    public synchronized void show() {

        if (!aDialogIsOpen) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    titleTextView.setText(title);
                    bodyTextView.setText(body);
                    buttons[0].setText(buttonText0);
                    buttons[1].setText(buttonText1);
                    buttons[0].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickOption(buttonText0);
                            hide();
                        }
                    });

                    buttons[1].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickOption(buttonText1);
                            hide();
                        }
                    });

                    backgroundFadeView.setVisibility(View.VISIBLE);
                    if (backgroundFadeView.getTop() < MINIMUM_HEIGHT) {
                        viewGroup.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                    }
                    viewGroup.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
