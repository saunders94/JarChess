package com.example.jarchess.match.view;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.PauseResponse;
import com.example.jarchess.match.activity.MatchActivity;

public class PauseResponseDialog extends BinaryChoiceDialog {

    public static final String ACCEPT = "Accept";
    public static final String REJECT = "Reject";
    private static final String TAG = "PauseRequestDialog";

    public PauseResponseDialog(@NonNull MatchActivity matchActivity) {
        super(matchActivity, "Pause Request", "Will you accept the request?", ACCEPT, REJECT);
    }

    @Override
    protected void onClickOption(String buttonTextClicked) {
        switch (buttonTextClicked) {
            case ACCEPT:
                Log.i(TAG, "onClickOption: " + ACCEPT);
                activity.setPauseResponse(PauseResponse.ACCEPT);
                break;

            case REJECT:
                Log.i(TAG, "onClickOption: " + REJECT);
                activity.setPauseResponse(PauseResponse.REJECT);
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + buttonTextClicked);
        }
    }
}
