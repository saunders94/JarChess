package com.example.jarchess.match.events;

import android.util.Log;

import java.util.Collection;
import java.util.LinkedList;

public class SquareClickEventManager implements Clearable {
    private static final String TAG = "SquareClickEventManager";
    private static SquareClickEventManager instance;
    private final Collection<SquareClickEventListener> listeners;

    /**
     * Creates an instance of <code>SquareClickEventManager</code> to construct a singleton instance
     */
    private SquareClickEventManager() {
        listeners = new LinkedList<>();
        ClearableManager.add(this);
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static SquareClickEventManager getInstance() {
        if (instance == null) {
            instance = new SquareClickEventManager();
        }

        return instance;
    }

    public boolean add(SquareClickEventListener listener) {
        return listeners.add(listener);
    }

    @Override
    public void clear() {
        listeners.clear();
    }

    public void notifyAllListeners(SquareClickEvent event) {
        for (SquareClickEventListener listener : listeners) {
            listener.observe(event);
            Log.d(TAG, "notifyAllListeners: notifying " + listener.getClass().getSimpleName());
        }
    }

    public boolean remove(SquareClickEvent listener) {
        return listeners.remove(listener);
    }
}
