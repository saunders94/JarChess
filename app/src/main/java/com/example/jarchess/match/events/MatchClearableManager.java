package com.example.jarchess.match.events;

import java.util.Collection;
import java.util.LinkedList;

public class MatchClearableManager {

    private static Collection<Clearable> clearables = new LinkedList<>();
    private static MatchClearableManager instance;

    /**
     * Creates an instance of <code>ClearableManager</code> to construct a singleton instance
     */
    private MatchClearableManager() {
    }

    public static boolean add(Clearable clearable) {
        return clearables.add(clearable);
    }

    public static void clearAll() {
        for (Clearable c : clearables) {
            c.clear();
        }
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static MatchClearableManager getInstance() {
        if (instance == null) {
            instance = new MatchClearableManager();
        }

        return instance;
    }
}
