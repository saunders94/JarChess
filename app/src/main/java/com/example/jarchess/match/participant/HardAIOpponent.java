package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.turn.Turn;

/**
 * A hard AI opponent is an AI opponent that is considered hard to play against.
 *
 * @author Joshua Zierman
 */
public class HardAIOpponent extends AIOpponent {
    public static final boolean IS_IMPLEMENTED = false; // TODO change this when we implement Hard AI Opponent

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
    public Turn getFirstTurn() throws MatchOverException {
        return null;//TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getNextTurn(Turn lastTurnFromOtherParticipant) throws MatchOverException {
        return null;//TODO
    }

    @Override
    public void observe(MatchResultIsInEvent event) {
        //TODO
    }
}
