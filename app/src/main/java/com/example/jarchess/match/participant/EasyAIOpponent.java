package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.activity.MatchActivity;
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
    public Turn getFirstTurn() throws MatchActivity.MatchOverException {
        return null;//TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getNextTurn(Turn lastTurnFromOtherParticipant) throws MatchActivity.MatchOverException {
        return null;//TODO
    }


}