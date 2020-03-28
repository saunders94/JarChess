package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public class InvalidTurnReceivedResult extends WinResult {

    public InvalidTurnReceivedResult(ChessColor winnerColor) {
        super(winnerColor);
    }

    @Override
    protected String winTypeString() {
        return "invalid move";
    }
}
