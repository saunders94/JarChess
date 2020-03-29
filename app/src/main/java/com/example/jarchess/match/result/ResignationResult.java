package com.example.jarchess.match.result;

import com.example.jarchess.match.ChessColor;

public class ResignationResult extends WinResult {

    public ResignationResult(ChessColor winnerColor) {
        super(winnerColor);
    }

    @Override
    protected String winTypeString() {
        return "resignation";
    }
}
