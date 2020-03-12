package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public class TimeoutResult extends WinResult {
    public TimeoutResult(ChessColor winnerColor) {
        super(winnerColor);
    }

    @Override
    protected String winTypeString() {
        return "timeout";
    }
}
