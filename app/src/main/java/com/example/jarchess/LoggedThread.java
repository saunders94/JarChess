package com.example.jarchess;

import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;

public class LoggedThread extends Thread {
    private static Collection<LoggedThread> list = new LinkedList<>();
    private final String TAG;
    public LoggedThread(final String TAG, final Runnable runnable, final String threadName) {

        super(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "starting thread: " + Thread.currentThread().getName());
                try {
                    runnable.run();
                } finally {
                    Log.d(TAG, "ending thread: " + Thread.currentThread().getName());
                }
            }
        }, threadName);
        this.TAG = TAG;
    }

    public static void clear() {
        list.clear();
    }

    public static void logAllThreads() {
        Collection<LoggedThread> toRemove = new LinkedList<>();
        for (LoggedThread loggedThread : list) {

            String interrupted = loggedThread.isInterrupted() ? " and Interrupted" : "";

            if (loggedThread.isAlive()) {
                Log.d(loggedThread.TAG, "logAllThreads: " + loggedThread.getName() + " is Alive" + interrupted);

            } else {
                Log.d(loggedThread.TAG, "logAllThreads: " + loggedThread.getName() + " is Dead" + interrupted);
            }
        }
    }


    public static void logThreadHoldingLock(Object lock, String lockName) {
        for (LoggedThread loggedThread : list) {
            if (holdsLock(lock)) {
                Log.d(loggedThread.TAG, "logAllThreads: " + loggedThread.getName() + " is holding lock " + lockName);
            }
        }
    }

    @Override
    public synchronized void start() {
        list.add(this);
        super.start();
    }

}
