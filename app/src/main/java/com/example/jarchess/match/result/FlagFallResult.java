package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public class FlagFallResult extends WinResult {
    public FlagFallResult(ChessColor winnerColor) {
        super(winnerColor);
    }

    @Override
    protected String getWinTypeString() {
        return "clock flag fall";
    }
}
