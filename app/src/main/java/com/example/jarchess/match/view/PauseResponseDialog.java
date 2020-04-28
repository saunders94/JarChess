package com.example.jarchess.match.view;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.PauseResponse;

public class PauseResponseDialog extends BinaryChoiceDialog {

    public static final String ACCEPT = "Accept";
    public static final String REJECT = "Reject";
    private static final String TAG = "PauseRequestDialog";
    @NonNull
    private final MatchView matchView;

    public PauseResponseDialog(@NonNull final MatchView matchView) {
        super(matchView, "Pause Request", "Will you accept the request?", ACCEPT, REJECT);
        this.matchView = matchView;
    }

    @Override
    protected void onClickOption(String buttonTextClicked) {
        switch (buttonTextClicked) {
            case ACCEPT:
                Log.i(TAG, "onClickOption: " + ACCEPT);
                activity.setPauseResponse(PauseResponse.ACCEPT);
                matchView.showAcceptedPauseDialog();
                hideWithoutEnablingDrawButton();
                break;

            case REJECT:
                Log.i(TAG, "onClickOption: " + REJECT);
                activity.setPauseResponse(PauseResponse.REJECT);
                hide();
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + buttonTextClicked);
        }
    }
}
