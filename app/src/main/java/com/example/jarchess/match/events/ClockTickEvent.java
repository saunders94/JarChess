package com.example.jarchess.match.events;

import com.example.jarchess.match.ChessColor;

public class ClockTickEvent {
    private final long[] displayedTimeMillis;

    public ClockTickEvent(long[] displayedTimeMillis) {
        this.displayedTimeMillis = displayedTimeMillis;
    }

    public long getDisplayedTimeMillis(ChessColor color) {
        return displayedTimeMillis[color.getIntValue()];
    }
}
