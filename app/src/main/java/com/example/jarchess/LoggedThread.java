package com.example.jarchess;

import android.util.Log;

public class LoggedThread extends Thread {
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
    }
}
