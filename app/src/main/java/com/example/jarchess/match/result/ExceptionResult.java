package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public class ExceptionResult extends WinResult {
    private final String message;

    public ExceptionResult(ChessColor winnerColor, String message) {
        super(winnerColor);
        this.message = message;
    }

    @Override
    protected String getMessage() {
        return message;
    }

    @Override
    protected String getWinTypeString() {
        return null;
    }
}
