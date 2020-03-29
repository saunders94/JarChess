package com.example.jarchess.match.view;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

class LeaveMatchDialog {

    private final MatchActivity activity;
    private View view;
    private ImageView backgroundFadeImageView;
    private Button resignButton;
    private Button requestDrawButton;

    public LeaveMatchDialog(@NonNull MatchActivity matchActivity) {
        activity = matchActivity;


        view = activity.findViewById(R.id.leaveMatchDialogFrameLayout);

        backgroundFadeImageView = view.findViewById(R.id.leaveMatchFadeBackgroundImageView);
        resignButton = view.findViewById(R.id.resignButton);
        requestDrawButton = view.findViewById(R.id.requestDrawButton);

        backgroundFadeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        resignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.observeResignButtonClick();
            }
        });
    }

    public void hide() {
        view.setVisibility(View.INVISIBLE);
    }

    public void show() {
        view.setVisibility(View.VISIBLE);
        Log.d(TAG, "show: should be visible");
    }
}
