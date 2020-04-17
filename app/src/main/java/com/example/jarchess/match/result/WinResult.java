package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public abstract class WinResult extends ChessMatchResult {
    public WinResult(ChessColor winnerColor, ResultType type) {
        super(winnerColor, type);
    }

    public ChessColor getLoserColor() {
        return ChessColor.getOther(winnerColor);
    }

    @Override
    protected String getMessage() {
        return winnerColor.toString() + " wins by " + getWinTypeString() + ".";
    }

    public ChessColor getWinnerColor() {
        return winnerColor;
    }

    protected abstract String getWinTypeString();
}
