package com.example.jarchess.match.clock;

import com.example.jarchess.match.ChessColor;

class FlagFallEvent {

    private final ChessColor color;

    FlagFallEvent(ChessColor color) {
        this.color = color;
    }

    private final ChessColor getColor() {
        return color;
    }
}
