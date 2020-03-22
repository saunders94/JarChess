package com.example.jarchess.match.clock;

import android.util.Log;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.testmode.TestableCurrentTime;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Math.abs;

public abstract class IncrementMatchClock implements MatchClock {

    private static final int WHITE_INT = ChessColor.WHITE.getIntValue();
    private static final int BLACK_INT = ChessColor.BLACK.getIntValue();
    private static final String THREAD_NAME = "incrementMatchClockThread";
    private final int[] incrementSeconds;
    private final int[] turnsForIncrementBracket;
    private final int[] mainTimeMinutes;
    private final int[] turnsForMainTimeBracket;
    private final long[] currentDisplayTimeMillis = new long[2];
    private final int[] turn = new int[2];
    private final MatchClockObserver observer;
    private final Object lock;
    private ChessColor runningColor = null;
    private long startTimeMillis;
    private boolean flagHasFallen;
    private boolean stopHasBeenCalled = false;

    public IncrementMatchClock(int[] incrementSeconds, int[] turnsForIncrementBracket, int[] mainTimeMinutes, int[] turnsForMainTimeBracket, MatchClockObserver observer) {
        Log.d(TAG, "IncrementMatchClock() called with: incrementSeconds = [" + incrementSeconds + "], turnsForIncrementBracket = [" + turnsForIncrementBracket + "], mainTimeSeconds = [" + mainTimeMinutes + "], turnsForMainTimeBracket = [" + turnsForMainTimeBracket + "], observer = [" + observer + "]");

        this.incrementSeconds = incrementSeconds;
        this.turnsForIncrementBracket = turnsForIncrementBracket;
        this.mainTimeMinutes = mainTimeMinutes;
        this.turnsForMainTimeBracket = turnsForMainTimeBracket;
        this.observer = observer;
        this.lock = this;

        for (int i : new int[]{0, 1}) {
            currentDisplayTimeMillis[i] = mainTimeMinutes[0] * 1000 * 60;
        }
    }

    public int getIncrementSeconds(int turn) {
        Log.d(TAG, "getIncrementSeconds() called with: turn = [" + turn + "]");
        for (int i = 0; i < turnsForIncrementBracket.length; i++) {
            if (turnsForIncrementBracket[i] > turn) {
                return incrementSeconds[i];
            }
        }
        return incrementSeconds[incrementSeconds.length - 1];
    }

    public int getMainTimeSeconds(int turn) {
        Log.d(TAG, "getMainTimeSeconds() called with: turn = [" + turn + "]");
        for (int i = 0; i < turnsForMainTimeBracket.length; i++) {
            if (turnsForMainTimeBracket[i] > turn) {
                return mainTimeMinutes[i];
            }
        }
        return mainTimeMinutes[mainTimeMinutes.length - 1];
    }

    @Override
    public synchronized void start() {
        Log.d(TAG, "startTimeMillis() called");
        start(ChessColor.WHITE);
    }

    @Override
    public synchronized void start(ChessColor colorStartingTurn) {
        Log.d(TAG, "startTimeMillis() called with: colorStartingTurn = [" + colorStartingTurn + "]");
        if (!flagHasFallen && runningColor == null) {
            runningColor = colorStartingTurn;
            turn[colorStartingTurn.getIntValue()] += 1;
            startTimeMillis = TestableCurrentTime.currentTimeMillis();
            final MatchClock matchClock = this;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "MatchClock's anonymous runnable is running on thread: " + Thread.currentThread().getName());

                        synchronized (lock) {
                            while (!stopHasBeenCalled) {
                                {
                                    observer.observeMatchClock(matchClock);
                                    lock.wait(100); // updates approximately every tenth of a second.
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        // get out
                    } finally {
                        Log.d(TAG, "done running thread: " + Thread.currentThread().getName());
                    }
                }
            }, THREAD_NAME).start();
        }
    }

    @Override
    public synchronized boolean isRunning() {
        Log.d(TAG, "isRunning() called");
        return runningColor != null;
    }

    @Override
    public synchronized ChessColor getRunningColor() {
        Log.d(TAG, "getRunningColor() called");
        return runningColor;
    }

    @Override
    public synchronized void syncEnd(ChessColor colorEndingTurn, long reportedElapsedTimeMillis, long toleranceMillis) throws ClockSyncException {
        Log.d(TAG, "syncEnd() called with: colorEndingTurn = [" + colorEndingTurn + "], reportedElapsedTimeMillis = [" + reportedElapsedTimeMillis + "], toleranceMillis = [" + toleranceMillis + "]");
        Log.d(TAG, "syncEnd: TURN=" + turn[colorEndingTurn.getIntValue()]);
        if (!flagHasFallen) {
            long measuredElapsedTime = TestableCurrentTime.currentTimeMillis() - startTimeMillis;
            if (colorEndingTurn == runningColor) {
                if (toleranceMillis >= 0 && abs(reportedElapsedTimeMillis - measuredElapsedTime) > toleranceMillis) {
                    throw new ClockSyncException(reportedElapsedTimeMillis, measuredElapsedTime, toleranceMillis);
                }

                // update the current time
                currentDisplayTimeMillis[colorEndingTurn.getIntValue()] -= reportedElapsedTimeMillis;

                //handle timeout
                if (currentDisplayTimeMillis[colorEndingTurn.getIntValue()] <= 0L) {
                    flagHasFallen = true;
                    currentDisplayTimeMillis[colorEndingTurn.getIntValue()] = 0L;
                    stop();
                } else {
                    //add increment to time
                    currentDisplayTimeMillis[colorEndingTurn.getIntValue()] += (long) getIncrementSeconds(turn[colorEndingTurn.getIntValue()]) * 1000L;

                    // startTimeMillis clock for other color
                    runningColor = ChessColor.getOther(runningColor);
                    turn[runningColor.getIntValue()] += 1;
                    startTimeMillis = TestableCurrentTime.currentTimeMillis();
                }

                // replace the current time with the next main time if the number of turns requires it.
                updateCurrentTimeFromTurnBracket(colorEndingTurn.getIntValue());
            } else {
                Log.e(TAG, "syncEnd: Attempting to sync end on with the non running color");
            }
        } else {
            Log.e(TAG, "syncEnd: called after flag has fallen");
        }

    }

    @Override
    public synchronized void syncEnd(ChessColor colorEndingTurn, long reportedElapsedTimeMillis) {
        Log.d(TAG, "syncEnd() called with: colorEndingTurn = [" + colorEndingTurn + "], reportedElapsedTimeMillis = [" + reportedElapsedTimeMillis + "]");
        try {
            syncEnd(colorEndingTurn, reportedElapsedTimeMillis, -1L);
        } catch (ClockSyncException e) {
            Log.wtf(TAG, "syncEnd: SyncEnd with negative tolerance threw a clock sync exception instead of syncing any reported value. ", e);
            e.printStackTrace();
        }
    }

    @Override
    public synchronized long getDisplayedTimeMillis(ChessColor colorTimeToGet) {
        if (runningColor != null) {

            long elapsed = TestableCurrentTime.currentTimeMillis() - startTimeMillis;
            long updatedCurrent = currentDisplayTimeMillis[runningColor.getIntValue()] - elapsed;
            if (updatedCurrent <= 0L) {
                flagHasFallen = true;
                currentDisplayTimeMillis[runningColor.getIntValue()] = 0L;
                stop();
            }
            return colorTimeToGet == runningColor ? updatedCurrent : currentDisplayTimeMillis[colorTimeToGet.getIntValue()];
        } else {
            return currentDisplayTimeMillis[colorTimeToGet.getIntValue()];
        }
    }

    @Override
    public synchronized boolean flagHasFallen() {
        long elapsed = startTimeMillis - TestableCurrentTime.currentTimeMillis();
        if (runningColor != null && currentDisplayTimeMillis[runningColor.getIntValue()] - elapsed <= 0) {
            flagHasFallen = true;
            currentDisplayTimeMillis[runningColor.getIntValue()] = 0L;
            runningColor = null;
            stop();
        }
        return flagHasFallen;
    }

    @Override
    public synchronized void stop() {
        if (!stopHasBeenCalled) {
            stopHasBeenCalled = true;
            observer.observeMatchClock(this);
            runningColor = null;
        }

    }

    @Override
    public Object getLock() {
        return lock;
    }

    private synchronized void updateCurrentTimeFromTurnBracket(int colorInt) {
        Log.d(TAG, "updateCurrentTimeFromTurnBracket() called with: colorInt = [" + colorInt + "]");
        for (int i = 0; i < turnsForMainTimeBracket.length; i++) {
            if (turn[colorInt] == turnsForMainTimeBracket[i]) {
                currentDisplayTimeMillis[colorInt] = mainTimeMinutes[i] * 1000 * 60;
                Log.d(TAG, "updateCurrentTimeFromTurnBracket: updating currentDisplayTimeMillis to " + currentDisplayTimeMillis[colorInt]);
            }
        }

    }


}
