package com.example.jarchess.match;

import androidx.annotation.NonNull;

public class PauseResponse {
    public static final PauseResponse ACCEPT = new PauseResponse(true);
    public static final PauseResponse REJECT = new PauseResponse(false);
    private final boolean accepted;

    private PauseResponse(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @NonNull
    @Override
    public String toString() {
        return isAccepted() ? "Accepted Pause Request" : "Rejected Pause Request";
    }
}
