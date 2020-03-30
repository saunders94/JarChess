package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;

public abstract class ChessMatchResult {
    final ChessColor winnerColor;

    public ChessMatchResult(ChessColor winnerColor) {
        this.winnerColor = winnerColor;
    }

    protected abstract String getMessage();

    @NonNull
    @Override
    public String toString() {
        return getMessage();
    }

    public boolean wasDraw() {
        return winnerColor == null;
    }
}
