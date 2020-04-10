package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.turn.Turn;

/**
 * An easy AI Opponent is an AI opponent that is considered easy difficulty to play against.
 *
 * @author Joshua Zierman
 */
public class EasyAIOpponent extends AIOpponent {

    public static final boolean IS_IMPLEMENTED = false; // TODO change this when we implement Easy AI Opponent
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
    public Turn getFirstTurn() throws MatchOverException {
        return null;//FIXME
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getNextTurn(Turn lastTurnFromOtherParticipant) throws MatchOverException {
        return null;//FIXME
    }


    @Override
    public void observe(MatchResultIsInEvent event) {
        //FIXME
    }
}