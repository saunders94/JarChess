package com.example.jarchess.match.clock;

import java.util.Collection;
import java.util.LinkedList;

public class ClockTickEventManager {

    private static ClockTickEventManager instance;
    private final Collection<ClockTickListener> listeners;

    /**
     * Creates an instance of <code>flagFallEventManager</code> to construct a singleton instance
     */
    private ClockTickEventManager() {
        listeners = new LinkedList<>();
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static ClockTickEventManager getInstance() {
        if (instance == null) {
            instance = new ClockTickEventManager();
        }

        return instance;
    }

    public boolean add(ClockTickListener listener) {
        return listeners.add(listener);
    }

    public void notifyAllListeners(ClockTickEvent event) {
        for (ClockTickListener listener : listeners) {
            listener.observe(event);
        }
    }

    public boolean remove(ClockTickListener listener) {
        return listeners.remove(listener);
    }
}
