package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public class CheckmateResult extends WinResult {

    public CheckmateResult(ChessColor winnerColor) {
        super(winnerColor);
    }

    @Override
    protected String winTypeString() {
        return "checkmate";
    }
}
