package com.example.jarchess.match.events;

import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;

public class MatchResultIsInEventManager implements Clearable {
    private static final String TAG = "MatchResultIsInEventMan";
    private static MatchResultIsInEventManager instance;
    private final Collection<MatchResultIsInEventListener> listeners;

    /**
     * Creates an instance of <code>MatchResultIsInEventManager</code> to construct a singleton instance
     */
    private MatchResultIsInEventManager() {
        listeners = new LinkedList<>();
        MatchClearableManager.add(this);
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static MatchResultIsInEventManager getInstance() {
        if (instance == null) {
            instance = new MatchResultIsInEventManager();
        }

        return instance;
    }

    public boolean add(MatchResultIsInEventListener listener) {
        return listeners.add(listener);
    }

    @Override
    public void clear() {
        listeners.clear();
    }

    public void notifyAllListeners(MatchResultIsInEvent event) {
        for (MatchResultIsInEventListener listener : listeners) {
            listener.observe(event);
            Log.d(TAG, "notifyAllListeners: notifying " + listener.getClass().getSimpleName());
        }
    }

    public boolean remove(MatchResultIsInEvent listener) {
        return listeners.remove(listener);
    }
}
