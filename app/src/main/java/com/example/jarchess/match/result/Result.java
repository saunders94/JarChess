package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;

public abstract class Result {
    final ChessColor winnerColor;

    public Result(ChessColor winnerColor) {
        this.winnerColor = winnerColor;
    }

    public boolean wasDraw() {
        return winnerColor == null;
    }

    @NonNull
    @Override
    public String toString() {
        return getMessage();
    }

    protected abstract String getMessage();
}
