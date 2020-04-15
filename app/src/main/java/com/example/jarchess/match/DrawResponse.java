package com.example.jarchess.match;

public class DrawResponse {
    private final boolean accepted;

    public DrawResponse(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
