package com.example.jarchess.match.clock;

import com.example.jarchess.match.ChessColor;

public interface MatchClock {

    boolean flagHasFallen();
    ChessColor getFallenFlag();
    long getDisplayedTimeMillis(ChessColor colorTimeToGet);
    ChessColor getRunningColor();

    ChessColor getStoppedColor();

    long getToleranceMillis();

    boolean isRunning();
    void start();
    void stop();

    void reset();

    void resume();

    void syncEnd(ChessColor colorEndingTurn, long reportedElapsedTimeMillis) throws ClockSyncException;
}
