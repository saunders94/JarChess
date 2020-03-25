package com.example.jarchess.match.clock;

import java.util.Collection;
import java.util.LinkedList;

public class flagFallEventManager {

    private static flagFallEventManager instance;
    private final Collection<FlagFallListener> listeners;

    /**
     * Creates an instance of <code>flagFallEventManager</code> to construct a singleton instance
     */
    private flagFallEventManager() {
        listeners = new LinkedList<>();
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static flagFallEventManager getInstance() {
        if (instance == null) {
            instance = new flagFallEventManager();
        }

        return instance;
    }

    public boolean add(FlagFallListener listener) {
        return listeners.add(listener);
    }

    public void notifyAllListeners(FlagFallEvent flagFallEvent) {
        for (FlagFallListener listener : listeners) {
            listener.observe(flagFallEvent);
        }
    }

    public boolean remove(FlagFallListener listener) {
        return listeners.remove(listener);
    }
}
