package com.example.jarchess.match.clock;

import android.util.Log;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.events.ClockTickEvent;
import com.example.jarchess.match.events.ClockTickEventManager;
import com.example.jarchess.testmode.TestableCurrentTime;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Math.abs;

public class CasualMatchClock implements MatchClock {

    public static final long IGNORE_TOLERANCE = -1L;
    public static final String THREAD_NAME = "casualClockThread";
    private static final long MILLISECOND_INTERVAL_BETWEEN_TICKS = 100;// updates approximately every tenth of a second.
    private final long[] currentDisplayTimeMillis = new long[]{0L, 0L};
    private final long toleranceMillis;
    private ChessColor runningColor;
    private ChessColor stoppedColor;
    private long startTimeMillis;
    private Thread thread;
    private boolean threadNeedsToDie;

    public CasualMatchClock(long toleranceMillis) {
        this.toleranceMillis = toleranceMillis;
    }


    public CasualMatchClock() {
        this(IGNORE_TOLERANCE);
    }

    @Override
    public synchronized boolean flagHasFallen() {
        // flag never falls
        return false;
    }

    @Override
    public synchronized ChessColor getColorOfFallenFlag() {
        // flag never falls
        return null;
    }

    @Override
    public synchronized long getDisplayedTimeMillis(ChessColor colorTimeToGet) {
        if (runningColor != null) {

            long elapsed = TestableCurrentTime.currentTimeMillis() - startTimeMillis;
            long updatedCurrent = currentDisplayTimeMillis[runningColor.getIntValue()] + elapsed;
            return colorTimeToGet == runningColor ? updatedCurrent : currentDisplayTimeMillis[colorTimeToGet.getIntValue()];
        } else {
            return currentDisplayTimeMillis[colorTimeToGet.getIntValue()];
        }
    }

    @Override
    public synchronized ChessColor getRunningColor() {
        return runningColor;
    }

    @Override
    public synchronized ChessColor getStoppedColor() {
        return runningColor;
    }

    @Override
    public synchronized long getToleranceMillis() {
        return toleranceMillis;
    }

    @Override
    public synchronized boolean isRunning() {
        return runningColor != null;
    }

    @Override
    public void kill() {
        threadNeedsToDie = true;
    }

    @Override
    public synchronized boolean isAlive() {
        return thread != null && !threadNeedsToDie;
    }

    @Override
    public synchronized void start() {
        if (thread != null) {
            resume();
        } else {
            stoppedColor = null;
            runningColor = ChessColor.WHITE;
            startTimeMillis = TestableCurrentTime.currentTimeMillis();
            final MatchClock matchClock = this;

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (matchClock) {
                        threadNeedsToDie = false;
                        try {
                            Log.d(TAG, "MatchClock's anonymous runnable is running on thread: " + Thread.currentThread().getName());

                            while (!threadNeedsToDie) {
                                {
                                    while (runningColor == null) {
                                        matchClock.wait();
                                    }
                                    notifyClockTickListeners();
                                    matchClock.wait(MILLISECOND_INTERVAL_BETWEEN_TICKS);
                                }
                            }

                        } catch (InterruptedException e) {
                            // get out
                        } finally {
                            Log.d(TAG, "done running thread: " + Thread.currentThread().getName());
                        }
                    }
                }
            }, THREAD_NAME);

            thread.start();
        }

    }

    @Override
    public synchronized void stop() {

        long current = TestableCurrentTime.currentTimeMillis();
        if (thread != null && runningColor != null) {
            long elapsed = current - startTimeMillis;
            currentDisplayTimeMillis[runningColor.getIntValue()] += elapsed;
            stoppedColor = runningColor;
            runningColor = null;
            notifyClockTickListeners();
        }
    }

    @Override
    public synchronized void resume() {
        if (thread != null && stoppedColor != null) {
            runningColor = stoppedColor;
            stoppedColor = null;
            notifyAll();
            startTimeMillis = TestableCurrentTime.currentTimeMillis();
        }
    }

    @Override
    public synchronized void reset() {

        threadNeedsToDie = true;
        thread = null;
        notifyAll();

        while (threadNeedsToDie) {
            try {
                wait();
            } catch (InterruptedException e) {
                Log.e(TAG, "reset: ", e);
            }
        }

        start();
    }

    @Override
    public synchronized void syncEnd(ChessColor colorEndingTurn, long reportedElapsedTimeMillis) throws ClockSyncException {

        if (thread != null && runningColor != null) {
            long measuredElapsedTime = TestableCurrentTime.currentTimeMillis() - startTimeMillis;
            if (colorEndingTurn == runningColor) {
                if (toleranceMillis >= 0 && abs(reportedElapsedTimeMillis - measuredElapsedTime) > toleranceMillis) {
                    throw new ClockSyncException(reportedElapsedTimeMillis, measuredElapsedTime, toleranceMillis, colorEndingTurn);
                }

                // update the current time
                currentDisplayTimeMillis[colorEndingTurn.getIntValue()] += reportedElapsedTimeMillis;

                // toggle to next color
                runningColor = ChessColor.getOther(runningColor);
                startTimeMillis = TestableCurrentTime.currentTimeMillis();
            } else {
                Log.e(TAG, "syncEnd: Attempting to sync end on with the non running color");
            }
        } else {
            Log.e(TAG, "syncEnd: called after clock has stopped");
        }

    }

    private synchronized void notifyClockTickListeners() {
        ClockTickEventManager.getInstance().notifyAllListeners(new ClockTickEvent(new long[]{getDisplayedTimeMillis(ChessColor.getFromInt(0)), getDisplayedTimeMillis(ChessColor.getFromInt(1))}));
    }
}
