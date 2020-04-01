package com.example.jarchess.match.events;

import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;

public class FlagFallEventManager implements Clearable {

    private static final String TAG = "FlagFallEventManager";
    private static FlagFallEventManager instance;
    private final Collection<FlagFallEventListener> listeners;

    /**
     * Creates an instance of <code>FlagFallEventManager</code> to construct a singleton instance
     */
    private FlagFallEventManager() {
        listeners = new LinkedList<>();
        MatchClearableManager.add(this);
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static FlagFallEventManager getInstance() {
        if (instance == null) {
            instance = new FlagFallEventManager();
        }

        return instance;
    }

    public boolean add(FlagFallEventListener listener) {
        return listeners.add(listener);
    }

    @Override
    public void clear() {
        listeners.clear();
    }

    public void notifyAllListeners(FlagFallEvent flagFallEvent) {
        Log.d(TAG, "notifyAllListeners() called with: flagFallEvent = [" + flagFallEvent + "]");
        Log.d(TAG, "notifyAllListeners is running on thread: " + Thread.currentThread().getName());
        Log.d(TAG, "notifyAllListeners: number of listeners = " + listeners.size());

        for (FlagFallEventListener listener : listeners) {
            listener.observe(flagFallEvent);
            Log.d(TAG, "notifyAllListeners: notifying " + listener.getClass().getSimpleName());
        }
        MatchEndingEventManager.getInstance().notifyAllListeners(flagFallEvent);
    }

    public boolean remove(FlagFallEventListener listener) {
        return listeners.remove(listener);
    }
}
