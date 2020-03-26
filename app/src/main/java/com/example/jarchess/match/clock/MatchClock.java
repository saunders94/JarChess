package com.example.jarchess.match.clock;

import com.example.jarchess.match.ChessColor;

public interface MatchClock {

    boolean flagHasFallen();

    ChessColor getFallenFlag();

    long getDisplayedTimeMillis(ChessColor colorTimeToGet);

    Object getLock();

    ChessColor getRunningColor();

    boolean isRunning();

    void start();

    void start(ChessColor colorStartingTurn);

    void stop();

    void syncEnd(ChessColor colorEndingTurn, long reportedElapsedTimeMillis, long toleranceMillis) throws ClockSyncException;

    void syncEnd(ChessColor colorEndingTurn, long reportedElapsedTimeMillis);
}
