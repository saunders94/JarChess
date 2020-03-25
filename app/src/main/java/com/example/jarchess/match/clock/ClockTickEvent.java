package com.example.jarchess.match.clock;

import com.example.jarchess.match.ChessColor;

class ClockTickEvent {
    private final long[] displayedTimeMillis;

    ClockTickEvent(long[] displayedTimeMillis) {
        this.displayedTimeMillis = displayedTimeMillis;
    }

    public long getDisplayedTimeMillis(ChessColor color) {
        return displayedTimeMillis[color.getIntValue()];
    }
}
