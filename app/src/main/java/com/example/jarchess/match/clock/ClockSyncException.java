package com.example.jarchess.match.clock;

import android.util.Log;

import com.example.jarchess.match.ChessColor;

public class ClockSyncException extends Exception {
    private static final String TAG = "ClockSyncException";
    private final ChessColor colorOutOfSync;

    public ClockSyncException(long reportedElapsedTime, long measuredElapsedTime, long toleranceMillis, ChessColor colorOutOfSync) {
        super("Clock sync resulted in a difference greater than the allowed tolerance: " + "reportedElapsedTime=" + reportedElapsedTime + ", measuredElapsedTime=" + measuredElapsedTime + ", toleranceMillis=" + toleranceMillis);
        this.colorOutOfSync = colorOutOfSync;
        Log.d(TAG, "ClockSyncException() called with: reportedElapsedTime = [" + reportedElapsedTime + "], measuredElapsedTime = [" + measuredElapsedTime + "], toleranceMillis = [" + toleranceMillis + "], colorOutOfSync = [" + colorOutOfSync + "]");
        Log.d(TAG, "ClockSyncException is running on thread: " + Thread.currentThread().getName());
    }

    public ChessColor getColorOutOfSync() {
        return colorOutOfSync;
    }
}
