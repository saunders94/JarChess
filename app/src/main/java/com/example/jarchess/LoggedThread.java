package com.example.jarchess;

import android.util.Log;

import com.example.jarchess.match.ThreadInterrupter;

import java.util.Collection;
import java.util.LinkedList;

public class LoggedThread extends Thread {
    private static final String TAG = "LoggedThread";
    private static Collection<LoggedThread> list = new LinkedList<>();
    private final String tag;
    private final boolean skipOnJoinAll;
    public static final ThreadInterrupter inputThreads = new ThreadInterrupter();

    public LoggedThread(final String tag, final Runnable runnable, final String threadName, final boolean skippOnJoinAll) {

        super(new Runnable() {
            @Override
            public void run() {
                Log.d(tag, "starting thread: " + Thread.currentThread().getName());
                try {
                    runnable.run();
                } catch (Exception e) {
                    Log.e(TAG, "run: ", e);
                } finally {
                    Log.d(tag, "ending thread: " + Thread.currentThread().getName());
                }
            }
        }, threadName);
        this.tag = tag;
        this.skipOnJoinAll = skippOnJoinAll;
    }

    public LoggedThread(final String tag, final Runnable runnable, final String threadName) {
        this(tag, runnable, threadName, true);
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
        super.start();
        list.add(this);

    }

}
