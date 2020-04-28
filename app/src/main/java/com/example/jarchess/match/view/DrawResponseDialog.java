package com.example.jarchess.match.view;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.DrawResponse;

public class DrawResponseDialog extends BinaryChoiceDialog {

    public static final String ACCEPT = "Accept";
    public static final String REJECT = "Reject";
    private static final String TAG = "DrawRequestDialog";

    public DrawResponseDialog(@NonNull final MatchView matchView) {
        super(matchView, "Draw Request", "Will you accept the request?", ACCEPT, REJECT);
    }

    @Override
    protected void onClickOption(String buttonTextClicked) {
        switch (buttonTextClicked) {
            case ACCEPT:
                Log.i(TAG, "onClickOption: " + ACCEPT);
                activity.setDrawResponse(DrawResponse.ACCEPT);
                hideWithoutEnablingDrawButton();
                break;

            case REJECT:
                Log.i(TAG, "onClickOption: " + REJECT);
                activity.setDrawResponse(DrawResponse.REJECT);
                hide();
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + buttonTextClicked);
        }
    }
}
