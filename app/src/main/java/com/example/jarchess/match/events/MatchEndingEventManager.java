package com.example.jarchess.match.events;

import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;

public class MatchEndingEventManager implements Clearable {
    private static final String TAG = "MatchEndingEventManager";
    private static MatchEndingEventManager instance;
    private final Collection<MatchEndingEventListener> listeners;

    /**
     * Creates an instance of <code>MatchEndingEventManager</code> to construct a singleton instance
     */
    private MatchEndingEventManager() {
        listeners = new LinkedList<>();
        MatchClearableManager.add(this);
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static MatchEndingEventManager getInstance() {
        if (instance == null) {
            instance = new MatchEndingEventManager();
        }

        return instance;
    }

    public synchronized boolean add(MatchEndingEventListener listener) {
        return listeners.add(listener);
    }

    @Override
    public synchronized void clear() {
        listeners.clear();
    }

    public synchronized void notifyAllListeners(MatchEndingEvent event) {
        Log.d(TAG, "notifyAllListeners() called with: event = [" + event + "]");
        Log.d(TAG, "notifyAllListeners is running on thread: " + Thread.currentThread().getName());
        Log.d(TAG, "notifyAllListeners: number of listeners = " + listeners.size());
        for (MatchEndingEventListener listener : listeners) {
            listener.observe(event);
            Log.d(TAG, "notifyAllListeners: notifying " + listener.getClass().getSimpleName());
        }
    }

    public synchronized boolean remove(MatchEndingEventListener listener) {
        return listeners.remove(listener);
    }
}
