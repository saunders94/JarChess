package com.example.jarchess.match.view;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;

abstract class BinaryChoiceDialog {
    private static final int MINIMUM_HEIGHT = 120;
    private final MatchActivity activity;
    private final String title;
    private final String body;
    private final Button[] buttons = new Button[2];
    private ViewGroup viewGroup;
    private View backgroundFadeView;

    public BinaryChoiceDialog(@NonNull MatchActivity matchActivity, String title, String body, final String buttonText0, final String buttonText1) {
        activity = matchActivity;
        this.title = title;
        this.body = body;


        viewGroup = activity.findViewById(R.id.promotionChoiceDialogFrameLayout);

        backgroundFadeView = activity.findViewById(R.id.fadeFrameLayoutBottom);

        buttons[0] = viewGroup.findViewById(R.id.binaryChoice0Button);
        buttons[0].setText(buttonText0);
        buttons[1] = viewGroup.findViewById(R.id.binaryChoice1Button);
        buttons[1].setText(buttonText1);

        backgroundFadeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

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
    }

    public MatchActivity getActivity() {
        return activity;
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

    protected abstract void onClickOption(String buttonTextClicked);

    public void show() {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backgroundFadeView.setVisibility(View.VISIBLE);
                if (backgroundFadeView.getTop() < MINIMUM_HEIGHT) {
                    viewGroup.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                }
                viewGroup.setVisibility(View.VISIBLE);
            }
        });
    }
}
