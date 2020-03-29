package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.events.MatchResultIsInEventListener;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.turn.Turn;

/**
 * A match participant represents a entity participating in a chess match.
 *
 * @author Joshua Zierman
 */
public interface MatchParticipant extends MatchResultIsInEventListener {//TODO write unit tests

    /**
     * Gets the avatar style for this participant
     *
     * @return the avatar style for this participant
     */
    AvatarStyle getAvatarStyle();

    /**
     * Gets the color of this participant.
     *
     * @return the color of this participant
     */
    ChessColor getColor();

    /**
     * Takes the first turn from stating position.
     *
     * @return the turn that this participant takes
     */
    Turn getFirstTurn() throws InterruptedException, MatchActivity.MatchOverException;

    /**
     * Gets the name of this participant.
     *
     * @return the name of this participant
     */
    String getName();

    /**
     * Resigns from the match.
     */
    void resign();

    /**
     * Takes a turn in response to the last turn from the other participant.
     *
     * @param lastTurnFromOtherParticipant the turn that happened immediately before by the other participant
     * @return the turn that this participant takes
     */
    Turn getNextTurn(Turn lastTurnFromOtherParticipant) throws InterruptedException, MatchActivity.MatchOverException;
}
