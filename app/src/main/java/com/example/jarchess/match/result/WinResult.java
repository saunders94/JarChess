package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public abstract class WinResult extends ChessMatchResult {
    public WinResult(ChessColor winnerColor) {
        super(winnerColor);
    }

    public ChessColor getLoserColor() {
        return ChessColor.getOther(winnerColor);
    }

    @Override
    protected String getMessage() {
        return winnerColor.toString() + " wins by " + winTypeString() + ".";
    }

    public ChessColor getWinnerColor() {
        return winnerColor;
    }

    protected abstract String winTypeString();
}
