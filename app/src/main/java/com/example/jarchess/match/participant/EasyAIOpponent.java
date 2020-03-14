package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.resignation.ResignationEvent;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.turn.Turn;

/**
 * An easy AI Opponent is an AI opponent that is considered easy difficulty to play against.
 *
 * @author Joshua Zierman
 */
public class EasyAIOpponent extends AIOpponent {//TODO write unit tests

    /**
     * Creates an easy AI opponent.
     *
     * @param color the color of the easy AI Opponent
     */
    public EasyAIOpponent(ChessColor color) {
        super(color, "Easy AI");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getFirstTurn() throws ResignationException {
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