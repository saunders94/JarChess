package com.example.jarchess.match;

import androidx.annotation.NonNull;

public class DrawResponse {
    public static final DrawResponse ACCEPT = new DrawResponse(true);
    public static final DrawResponse REJECT = new DrawResponse(false);
    private final boolean accepted;

    private DrawResponse(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @NonNull
    @Override
    public String toString() {
        return isAccepted() ? "Accepted Draw Request" : "Rejected Draw Request";
    }
}
