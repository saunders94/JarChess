package com.example.jarchess.match.events;

import java.util.Collection;
import java.util.LinkedList;

public class ClockTickEventManager implements Clearable {

    private static ClockTickEventManager instance;
    private final Collection<ClockTickEventListener> listeners;

    /**
     * Creates an instance of <code>FlagFallEventManager</code> to construct a singleton instance
     */
    private ClockTickEventManager() {
        listeners = new LinkedList<>();
        ClearableManager.add(this);
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

    public boolean add(ClockTickEventListener listener) {
        return listeners.add(listener);
    }

    @Override
    public void clear() {
        listeners.clear();
    }

    public void notifyAllListeners(ClockTickEvent event) {
        for (ClockTickEventListener listener : listeners) {
            listener.observe(event);
        }
    }

    public boolean remove(ClockTickEventListener listener) {
        return listeners.remove(listener);
    }
}
