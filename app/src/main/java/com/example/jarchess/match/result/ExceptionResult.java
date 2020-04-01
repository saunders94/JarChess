package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public class ExceptionResult extends WinResult {
    private final String message;
    private final Exception exception;

    public ExceptionResult(ChessColor winnerColor, String message, Exception e) {
        super(winnerColor);
        this.message = message;
        this.exception = e;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    protected String getWinTypeString() {
        return message;
    }
}
