package com.example.jarchess.match.view;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.events.RequestDrawButtonPressedEvent;
import com.example.jarchess.match.events.RequestDrawButtonPressedEventManager;

class LeaveMatchDialog {

    private final MatchActivity activity;
    @NonNull
    private final MatchView matchView;
    private View view;
    private ImageView backgroundFadeImageView;
    private Button resignButton;
    private Button requestDrawButton;
    private static final String TAG = "LeaveMatchDialog";

    public LeaveMatchDialog(@NonNull MatchView matchView) {
        this.matchView = matchView;
        activity = matchView.getActivity();


        view = activity.findViewById(R.id.leaveMatchDialogFrameLayout);

        backgroundFadeImageView = view.findViewById(R.id.leaveMatchFadeBackgroundImageView);
        resignButton = view.findViewById(R.id.resignButton);
        requestDrawButton = view.findViewById(R.id.requestDrawButton);
        requestDrawButton.setVisibility(View.VISIBLE);
        requestDrawButton.setClickable(true);

        backgroundFadeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        resignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Resign Button Clicked");
                requestDrawButton.setVisibility(View.INVISIBLE);
                requestDrawButton.setClickable(false);
                activity.observeResignButtonClick();
            }
        });

        requestDrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Request Draw Button Clicked");
                requestDrawButton.setVisibility(View.INVISIBLE);
                requestDrawButton.setClickable(false);
                hide();
                RequestDrawButtonPressedEventManager.getInstance().notifyAllListeners(new RequestDrawButtonPressedEvent());
            }
        });
    }

    public void hide() {
        view.setVisibility(View.INVISIBLE);
    }

    public void disableDrawButton() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestDrawButton.setVisibility(View.INVISIBLE);
                requestDrawButton.setClickable(false);
            }
        });
    }

    public void enableDrawButton() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestDrawButton.setVisibility(View.VISIBLE);
                requestDrawButton.setClickable(true);
            }
        });
    }

    public void show() {
        view.setVisibility(View.VISIBLE);
        Log.d(TAG, "show: should be visible");
    }
}
