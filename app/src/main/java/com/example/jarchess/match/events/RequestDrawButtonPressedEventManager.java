package com.example.jarchess.match.events;

import java.util.Collection;
import java.util.LinkedList;

public class RequestDrawButtonPressedEventManager implements Clearable {


    private static RequestDrawButtonPressedEventManager instance;
    private final Collection<RequestDrawButtonPressedEventListener> listeners = new LinkedList<>();

    /**
     * Creates an instance of <code>RequestDrawButtonPressedEventManager</code> to construct a singleton instance
     */
    private RequestDrawButtonPressedEventManager() {
        super();
        ClearableManager.add(this);
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static RequestDrawButtonPressedEventManager getInstance() {
        if (instance == null) {
            instance = new RequestDrawButtonPressedEventManager();
        }

        return instance;
    }

    public boolean add(RequestDrawButtonPressedEventListener listener) {
        return listeners.add(listener);
    }

    @Override
    public void clear() {
        listeners.clear();
    }

    public void notifyAllListeners(RequestDrawButtonPressedEvent event) {
        for (RequestDrawButtonPressedEventListener listener : listeners) {
            listener.observe(event);
        }
    }

    boolean remove(RequestDrawButtonPressedEventListener listener) {
        return listeners.remove(listener);
    }

}
