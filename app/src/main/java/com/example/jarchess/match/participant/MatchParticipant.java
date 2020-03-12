package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.resignation.ResignationListener;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.turn.Turn;

/**
 * A match participant represents a entity participating in a chess match.
 *
 * @author Joshua Zierman
 */
public interface MatchParticipant extends ResignationListener {//TODO write unit tests

    /**
     * Gets the name of this participant.
     *
     * @return the name of this participant
     */
    String getName();

    /**
     * Takes the first turn from stating position.
     *
     * @return the turn that this participant takes
     * @throws ResignationException if a resignation was detected.
     */
    Turn takeFirstTurn() throws ResignationException, InterruptedException;

    /**
     * Takes a turn in response to the last turn from the other participant.
     *
     * @param lastTurnFromOtherParticipant the turn that happened immediately before by the other participant
     * @return the turn that this participant takes
     * @throws ResignationException if a resignation was detected.
     */
    Turn takeTurn(Turn lastTurnFromOtherParticipant) throws ResignationException, InterruptedException;

    /**
     * Resigns from the match.
     */
    void resign();

    /**
     * Gets the color of this participant.
     *
     * @return the color of this participant
     */
    ChessColor getColor();

    /**
     * Gets the avatar style for this participant
     *
     * @return the avatar style for this participant
     */
    AvatarStyle getAvatarStyle();
}
