package com.example.jarchess.match.view;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.participant.RemoteOpponent;

public class PauseRequestDialog extends BinaryChoiceDialog {

    public static final String ACCEPT = "Accept";
    public static final String REJECT = "Reject";
    private static final String TAG = "PauseRequestDialog";
    private final RemoteOpponent remoteOpponent;

    public PauseRequestDialog(@NonNull MatchActivity matchActivity, RemoteOpponent remoteOpponent) {
        super(matchActivity, "Pause Request", "Will you accept the request?", ACCEPT, REJECT);
        this.remoteOpponent = remoteOpponent;
    }

    @Override
    protected void onClickOption(String buttonTextClicked) {
        switch (buttonTextClicked) {
            case ACCEPT:
                Log.i(TAG, "onClickOption: " + ACCEPT);
                remoteOpponent.acceptPauseRequest();
                break;

            case REJECT:
                Log.i(TAG, "onClickOption: " + REJECT);
                remoteOpponent.rejectPauseRequest();
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + buttonTextClicked);
        }
    }
}
