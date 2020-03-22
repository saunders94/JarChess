package com.example.jarchess.match.clock;

class ClockSyncException extends Exception {
    public ClockSyncException(long reportedElapsedTime, long measuredElapsedTime, long toleranceMillis) {
        super("Clock sync resulted in a difference greater than the allowed tolerance: " + "reportedElapsedTime=" + reportedElapsedTime + ", measuredElapsedTime=" + measuredElapsedTime + ", toleranceMillis=" + toleranceMillis);
    }
}
