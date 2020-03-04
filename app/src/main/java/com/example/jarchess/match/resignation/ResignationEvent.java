package com.example.jarchess.match.resignation;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.testmode.TestableCurrentTime;

/**
 * A resignation event is generated when a match participant resigns from a chess match. It is sent to all listeners so they can react
 *
 * @author Joshua Zierman
 */
public class ResignationEvent { //TODO write unit tests
    private final MatchParticipant resigningParticipant;
    private final long time;

    /**
     * Creates a resignation event
     *
     * @param resigningParticipant the resigning match participant
     */
    public ResignationEvent(@NonNull MatchParticipant resigningParticipant) {
        this.resigningParticipant = resigningParticipant;
        time = TestableCurrentTime.currentTimeMillis();
    }

    /**
     * Gets the color of the resigning participant
     *
     * @return the color of the resigning participant
     */
    public ChessColor getColorOfResigningParticipant() {
        if (resigningParticipant == null) {
            return null;
        }

        return resigningParticipant.getColor();
    }

    public long getTime() {
        return time;
    }
}
