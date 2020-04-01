package com.example.jarchess.match.events;

import java.util.Collection;
import java.util.LinkedList;

public class PauseButtonPressedEventManager implements Clearable {


    private static PauseButtonPressedEventManager instance;
    private final Collection<PauseButtonPressedEventListener> listeners = new LinkedList<>();

    /**
     * Creates an instance of <code>PauseButtonPressedEventManager</code> to construct a singleton instance
     */
    private PauseButtonPressedEventManager() {
        super();
        ClearableManager.add(this);
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static PauseButtonPressedEventManager getInstance() {
        if (instance == null) {
            instance = new PauseButtonPressedEventManager();
        }

        return instance;
    }

    public boolean add(PauseButtonPressedEventListener listener) {
        return listeners.add(listener);
    }

    @Override
    public void clear() {
        listeners.clear();
    }

    public void notifyAllListeners(PauseButtonPressedEvent event) {
        for (PauseButtonPressedEventListener listener : listeners) {
            listener.observe(event);
        }
    }

    boolean remove(PauseButtonPressedEventListener listener) {
        return listeners.remove(listener);
    }

}
