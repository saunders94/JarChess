package com.example.jarchess.match.resignation;

import androidx.annotation.NonNull;

import com.example.jarchess.match.participant.MatchParticipant;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A resignation manager collects listeners and notifies listeners when a participant of a match resigns.
 *
 * @author Joshua Zierman
 */
public class ResignationEventManager { //TODO write unit tests

    private final Collection<ResignationListener> listeners = new LinkedList<ResignationListener>();
    private ResignationEvent resignationEvent;

    /**
     * adds a listener that will be notified when a resignation is issued.
     * <p>
     * If the resignation listener is added after a resignation was already processed, that listener will be
     * notified immediately.
     *
     * @param listenerToAdd the resignation listener to be added
     * @return true if the listener collection was changed by the call, otherwise returns false
     */
    public synchronized boolean addListener(@NonNull ResignationListener listenerToAdd) {
        try {
            return listeners.add(listenerToAdd);
        } finally {
            if (resignationEvent != null) {
                listenerToAdd.observeResignationEvent(resignationEvent);
            }
        }
    }

    /**
     * Removes a listener if it is present in the listener collection.
     *
     * @param listenerToRemove the listener to remove
     * @return true if the listener collection was changed by this, otherwise returns false
     */
    public synchronized boolean removeListener(@NonNull ResignationListener listenerToRemove) {
        return listeners.remove(listenerToRemove);
    }

    /**
     * Resigns the participant, notifying all listeners.
     *
     * @param resigningParticipant the resigning participant
     */
    public synchronized void resign(@NonNull MatchParticipant resigningParticipant) {

        if (resignationEvent == null) {
            resignationEvent = new ResignationEvent(resigningParticipant);
            notifyListeners(resignationEvent);
        }
    }

    private synchronized void notifyListeners(@NonNull ResignationEvent resignationEvent) {

        for (ResignationListener listener : listeners) {
            listener.observeResignationEvent(resignationEvent);
        }
    }
}
