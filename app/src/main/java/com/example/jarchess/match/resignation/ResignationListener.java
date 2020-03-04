package com.example.jarchess.match.resignation;

/**
 * A resignation listener listens for a resignation event and reacts when one is observed
 */
public interface ResignationListener {
    /**
     * Observes a resignation event.
     *
     * @param resignationEvent the resignation event that is observed
     */
    void observeResignationEvent(ResignationEvent resignationEvent);
}
