package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.resignation.ResignationEvent;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.turn.Turn;

/**
 * A hard AI opponent is an AI opponent that is considered hard to play against.
 *
 * @author Joshua Zierman
 */
public class HardAIOpponent extends AIOpponent {//TODO write unit tests

    /**
     * Creates a hard AI opponent.
     *
     * @param color the color of the hard AI opponent
     */
    public HardAIOpponent(ChessColor color) {
        super(color, "Hard AI");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn takeFirstTurn() throws ResignationException {
        return null;//TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn takeTurn(Turn lastTurnFromOtherParticipant) throws ResignationException {
        return null;//TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void observeResignationEvent(ResignationEvent resignationEvent) {
        //TODO
    }
}
