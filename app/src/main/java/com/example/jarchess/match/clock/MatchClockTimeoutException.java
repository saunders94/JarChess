package com.example.jarchess.match.clock;

public class MatchClockTimeoutException extends Exception {
    private final static String MSG = "Match clock flag has fallen";

    public MatchClockTimeoutException() {
        super(MSG);
    }
}
