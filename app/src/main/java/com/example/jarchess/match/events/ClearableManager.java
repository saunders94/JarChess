package com.example.jarchess.match.events;

import java.util.Collection;
import java.util.LinkedList;

public class ClearableManager {

    private static Collection<Clearable> clearables = new LinkedList<>();
    private static ClearableManager instance;

    /**
     * Creates an instance of <code>ClearableManager</code> to construct a singleton instance
     */
    private ClearableManager() {
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
    public static ClearableManager getInstance() {
        if (instance == null) {
            instance = new ClearableManager();
        }

        return instance;
    }
}
