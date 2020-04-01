package com.example.jarchess.match.clock;

import android.util.Log;

import com.example.jarchess.LoggedThread;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.events.ClockTickEvent;
import com.example.jarchess.match.events.ClockTickEventManager;
import com.example.jarchess.match.events.FlagFallEvent;
import com.example.jarchess.match.events.FlagFallEventManager;
import com.example.jarchess.testmode.TestableCurrentTime;

import static java.lang.Math.abs;

public abstract class IncrementMatchClock implements MatchClock {
    public static final long IGNORE_TOLERANCE = -1L;
    private static final int WHITE_INT = ChessColor.WHITE.getIntValue();
    private static final int BLACK_INT = ChessColor.BLACK.getIntValue();
    private static final String THREAD_NAME = "incrementMatchClockThread";
    private static final long MILLISECOND_INTERVAL_BETWEEN_TICKS = 100;// updates approximately every tenth of a second.
    private final int[] incrementSeconds;
    private final int[] turnsForIncrementBracket;
    private final int[] mainTimeMinutes;
    private final int[] turnsForMainTimeBracket;
    private final long[] currentDisplayTimeMillis = new long[2];
    private final int[] turn = new int[2];
    private static final String TAG = "IncrementMatchClock";
    private final long toleranceMillis;
    private ChessColor colorOfFallenFlag;
    private ChessColor runningColor;
    private ChessColor stoppedColor;
    private long startTimeMillis;
    private final long[] displayTimeAtLastSync = new long[2];
    private Thread thread;
    private boolean threadNeedsToDie;
    private long turnTime;

    public IncrementMatchClock(int[] incrementSeconds, int[] turnsForIncrementBracket, int[] mainTimeMinutes, int[] turnsForMainTimeBracket, long toleranceMillis) {
        Log.d(TAG, "IncrementMatchClock() called with: incrementSeconds = [" + incrementSeconds + "], turnsForIncrementBracket = [" + turnsForIncrementBracket + "], mainTimeMinutes = [" + mainTimeMinutes + "], turnsForMainTimeBracket = [" + turnsForMainTimeBracket + "], toleranceMillis = [" + toleranceMillis + "]");
        Log.d(TAG, "IncrementMatchClock is running on thread: " + Thread.currentThread().getName());

        this.toleranceMillis = toleranceMillis;

        this.incrementSeconds = incrementSeconds;
        this.turnsForIncrementBracket = turnsForIncrementBracket;
        this.mainTimeMinutes = mainTimeMinutes;
        this.turnsForMainTimeBracket = turnsForMainTimeBracket;

        for (int i : new int[]{0, 1}) {
            currentDisplayTimeMillis[i] = mainTimeMinutes[0] * 1000 * 60;
            displayTimeAtLastSync[i] = currentDisplayTimeMillis[i];
        }
    }

    @Override
    public synchronized boolean flagHasFallen() {

        try {
            tic();
        } catch (InterruptedException e) {

            Log.e(TAG, "tic: ", e);
        }
        return colorOfFallenFlag != null;
    }

    @Override
    public synchronized ChessColor getColorOfFallenFlag() {
        try {
            tic();
        } catch (InterruptedException e) {

            Log.e(TAG, "getColorOfFallenFlag: ", e);
        }
        return colorOfFallenFlag;
    }

    @Override
    public synchronized long getDisplayedTimeMillis(ChessColor colorTimeToGet) {
        try {
            tic();
        } catch (InterruptedException e) {
            Log.e(TAG, "getDisplayedTimeMillis: ", e);
        }

        return currentDisplayTimeMillis[colorTimeToGet.getIntValue()];

    }

    @Override
    public synchronized ChessColor getStoppedColor() {
        return stoppedColor;
    }

    @Override
    public synchronized ChessColor getRunningColor() {
        Log.v(TAG, "getRunningColor() called");
        return runningColor;
    }

    @Override
    public long getToleranceMillis() {
        return toleranceMillis;
    }

    @Override
    public synchronized void kill() {
        threadNeedsToDie = true;
    }

    @Override
    public synchronized boolean isAlive() {
        return thread != null && !threadNeedsToDie;
    }

    @Override
    public synchronized boolean isRunning() {
        return runningColor != null;
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

            thread = new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    synchronized (matchClock) {
                        threadNeedsToDie = false;
                        try {

                            while (!threadNeedsToDie) {
                                {
                                    while (runningColor == null) {
                                        matchClock.wait();
                                    }
                                    tic();
                                }
                            }

                        } catch (InterruptedException e) {
                            // get out
                        }
                    }
                }
            }, THREAD_NAME);

            thread.start();
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
    public synchronized void stop() {

        long current = TestableCurrentTime.currentTimeMillis();
        if (thread != null && runningColor != null) {
            long elapsed = current - startTimeMillis;
            currentDisplayTimeMillis[runningColor.getIntValue()] -= elapsed;
            stoppedColor = runningColor;
            runningColor = null;
            notifyClockTickListeners();
        }
    }

    @Override
    public synchronized void syncEnd(ChessColor colorEndingTurn, long reportedElapsedTimeMillis) throws ClockSyncException {

        long currentTime = TestableCurrentTime.currentTimeMillis();
        int i = colorEndingTurn.getIntValue();
        Log.v(TAG, "syncEnd() called at: " + currentTime);
        Log.v(TAG, "syncEnd() called with: colorEndingTurn = [" + colorEndingTurn + "], reportedElapsedTimeMillis = [" + reportedElapsedTimeMillis + "], toleranceMillis = [" + toleranceMillis + "]");
        Log.v(TAG, "syncEnd: " + colorEndingTurn + "'s turn = " + turn[i]);
        if (colorOfFallenFlag == null) {
            turn[runningColor.getIntValue()] += 1;
            long measuredElapsedTime = currentTime - startTimeMillis + turnTime;
            turnTime = 0L;

            Log.i(TAG, "syncEnd: measured elapsed time was    " + measuredElapsedTime / 1000.0 + " seconds");
            Log.i(TAG, "syncEnd: reported elapsed time was    " + reportedElapsedTimeMillis / 1000.0 + " seconds");

            startTimeMillis = currentTime;

            if (colorEndingTurn == runningColor) {
                if (toleranceMillis >= 0 && abs(reportedElapsedTimeMillis - measuredElapsedTime) > toleranceMillis) {
                    Log.i(TAG, "syncEnd: The reported time is not within tolerance.");
                    throw new ClockSyncException(reportedElapsedTimeMillis, measuredElapsedTime, toleranceMillis, colorEndingTurn);
                }
                Log.i(TAG, "syncEnd: The reported time is within tolerance.");

                // update the current time
                Log.i(TAG, "syncEnd: lastSync display time was    " + displayTimeAtLastSync[i] / 1000.0 + " seconds");
                Log.i(TAG, "syncEnd: current display time was     " + currentDisplayTimeMillis[i] / 1000.0 + " seconds");
                currentDisplayTimeMillis[i] = displayTimeAtLastSync[i] - reportedElapsedTimeMillis;
                Log.i(TAG, "syncEnd: current display time set to  " + currentDisplayTimeMillis[i] / 1000.0 + " seconds");

                //handle timeout
                if (currentDisplayTimeMillis[i] <= 0L) {
                    Log.i(TAG, "syncEnd: The participant ran out of time.");
                    handleFlagFall(colorEndingTurn);
                } else {
                    Log.i(TAG, "syncEnd: The participant did not run out of time.");
                    //add increment to time
                    Log.i(TAG, "syncEnd: add " + (long) getIncrementSeconds(turn[i]) + " seconds");
                    currentDisplayTimeMillis[i] += (long) getIncrementSeconds(turn[i]) * 1000L;
                    Log.i(TAG, "syncEnd: current display time set to  " + currentDisplayTimeMillis[i] / 1000.0 + " seconds");

                    // startTimeMillis clock for other color
                    runningColor = ChessColor.getOther(runningColor);
                }

                // replace the current time with the next main time if the number of turns requires it.
                updateCurrentTimeFromTurnBracket(i);

                displayTimeAtLastSync[i] = currentDisplayTimeMillis[i];
            } else {
                String msg = "syncEnd: Attempting to sync end on with the non running color";
                Log.e(TAG, msg);
                throw new RuntimeException(msg);
            }
        } else {
            String msg = "syncEnd: called after flag has fallen";
            Log.e(TAG, msg);
            throw new RuntimeException(msg);
        }

    }

    private synchronized void handleFlagFall(ChessColor colorOfFallenFlag) {
        Log.d(TAG, "handleFlagFall() called with: colorOfFallenFlag = [" + colorOfFallenFlag + "]");
        Log.d(TAG, "handleFlagFall is running on thread: " + Thread.currentThread().getName());
        currentDisplayTimeMillis[runningColor.getIntValue()] = 0L;
        this.colorOfFallenFlag = colorOfFallenFlag;
        stop();
        threadNeedsToDie = true;
        FlagFallEventManager.getInstance().notifyAllListeners(new FlagFallEvent(colorOfFallenFlag));
        Log.d(TAG, "handleFlagFall() returned: ");
    }

    private synchronized void notifyClockTickListeners() {
        ClockTickEventManager.getInstance().notifyAllListeners(new ClockTickEvent(new long[]{currentDisplayTimeMillis[0], currentDisplayTimeMillis[1]}));
    }

    private synchronized void tic() throws InterruptedException {
        if (runningColor != null) {
            long currentTime = TestableCurrentTime.currentTimeMillis();
            long elapsed = currentTime - startTimeMillis;
            currentDisplayTimeMillis[runningColor.getIntValue()] -= elapsed;
            turnTime += elapsed;
            startTimeMillis = currentTime;

            if (currentDisplayTimeMillis[runningColor.getIntValue()] <= 0L) {
                handleFlagFall(runningColor);
            }

            notifyClockTickListeners();
            wait(MILLISECOND_INTERVAL_BETWEEN_TICKS);
        }
    }

    public int getIncrementSeconds(int turn) {
        Log.v(TAG, "getIncrementSeconds() called with: turn = [" + turn + "]");
        for (int i = 0; i < turnsForIncrementBracket.length; i++) {
            if (turnsForIncrementBracket[i] > turn) {
                return incrementSeconds[i];
            }
        }
        return incrementSeconds[incrementSeconds.length - 1];
    }

    public int getMainTimeSeconds(int turn) {
        Log.v(TAG, "getMainTimeSeconds() called with: turn = [" + turn + "]");
        for (int i = 0; i < turnsForMainTimeBracket.length; i++) {
            if (turnsForMainTimeBracket[i] > turn) {
                return mainTimeMinutes[i];
            }
        }
        return mainTimeMinutes[mainTimeMinutes.length - 1];
    }

    private synchronized void updateCurrentTimeFromTurnBracket(int colorInt) {
        Log.v(TAG, "updateCurrentTimeFromTurnBracket() called with: colorInt = [" + colorInt + "]");
        for (int i = 0; i < turnsForMainTimeBracket.length; i++) {
            if (turn[colorInt] == turnsForMainTimeBracket[i]) {
                Log.i(TAG, "updateCurrentTimeFromTurnBracket: New bracket reached on turn " + turn[colorInt] + ", setting time to " + mainTimeMinutes[i] + " minutes.");
                currentDisplayTimeMillis[colorInt] = mainTimeMinutes[i] * 1000 * 60;
                Log.v(TAG, "updateCurrentTimeFromTurnBracket: updating currentDisplayTimeMillis to " + currentDisplayTimeMillis[colorInt]);
            }
        }

    }
}
