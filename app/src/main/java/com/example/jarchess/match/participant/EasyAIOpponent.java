package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.testmode.TestableCurrentTime;

/**
 * An easy AI Opponent is an AI opponent that is considered easy difficulty to play against.
 *
 * @author Joshua Zierman
 */
public class EasyAIOpponent extends AIOpponent {

    private static final int DEPTH_LIMIT = 5;
    private final Minimax minimax;
    public static final boolean IS_IMPLEMENTED = false; // TODO change this when we implement Easy AI Opponent
    /**
     * Creates an easy AI opponent.
     *
     * @param color the color of the easy AI Opponent
     */
    public EasyAIOpponent(ChessColor color) {
        super(color, "Easy AI");
        minimax = new Minimax(10, 10, 5, 3, 3, 3, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getFirstTurn(MatchHistory matchHistory) throws MatchOverException {
        long start, elapsed;
        Move move;
        PromotionChoice choice = null;
        start = TestableCurrentTime.currentTimeMillis();
        move = minimax.findMove(DEPTH_LIMIT, matchHistory);
        //TODO get promotion choice
        elapsed = TestableCurrentTime.currentTimeMillis() - start;
        return new Turn(getColor(), move, elapsed, choice);//FIXME
    }

    /**
     * {@inheritDoc}
     * @param lastTurnFromOtherParticipant
     */
    @Override
    public Turn getNextTurn(MatchHistory lastTurnFromOtherParticipant) throws MatchOverException {
        return null;//FIXME
    }


    @Override
    public void observe(MatchResultIsInEvent event) {
        //FIXME
    }
}