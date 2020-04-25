package com.example.jarchess;

import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;

public class LoggedThread extends Thread {
    private static final String TAG = "LoggedThread";
    private static Collection<LoggedThread> list = new LinkedList<>();
    private final String tag;

    public LoggedThread(final String tag, final Runnable runnable, final String threadName) {

        super(new Runnable() {
            @Override
            public void run() {
                Log.d(tag, "starting thread: " + Thread.currentThread().getName());
                try {
                    runnable.run();
                } finally {
                    Log.d(tag, "ending thread: " + Thread.currentThread().getName());
                }
            }
        }, threadName);
        this.tag = tag;
    }

    public static void clear() {
        list.clear();
    }

    public static void logAllThreads() {
        Log.d(TAG, "logAllThreads() called");
        Log.d(TAG, "logAllThreads is running on thread: " + Thread.currentThread().getName());
        Collection<LoggedThread> toRemove = new LinkedList<>();
        for (LoggedThread loggedThread : list) {

            String interrupted = loggedThread.isInterrupted() ? " and Interrupted" : "";

            if (loggedThread.isAlive()) {
                Log.d(loggedThread.tag, "logAllThreads: " + loggedThread.getName() + " is Alive" + interrupted);

            } else {
                Log.d(loggedThread.tag, "logAllThreads: " + loggedThread.getName() + " is Dead" + interrupted);
                toRemove.add(loggedThread);
            }
        }

        for (LoggedThread t : toRemove) {
            list.remove(t);
        }
    }


    public static void logThreadHoldingLock(Object lock, String lockName) {
        for (LoggedThread loggedThread : list) {
            if (holdsLock(lock)) {
                Log.d(loggedThread.tag, "logAllThreads: " + loggedThread.getName() + " is holding lock " + lockName);
            }
        }
    }

    @Override
    public synchronized void start() {
        list.add(this);
        super.start();
    }

}
