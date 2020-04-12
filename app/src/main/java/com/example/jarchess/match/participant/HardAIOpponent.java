package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.testmode.TestableCurrentTime;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A hard AI opponent is an AI opponent that is considered hard to play against.
 *
 * @author Joshua Zierman
 */
public class HardAIOpponent extends AIOpponent {

    public static final boolean IS_IMPLEMENTED = true;
    private static final int DEPTH_LIMIT = 5;
    private static final Collection<PromotionChoice> PROMOTION_OPTIONS = new LinkedList<>();
    private final Minimax minimax;

    {
        PROMOTION_OPTIONS.add(PromotionChoice.PROMOTE_TO_QUEEN);
        PROMOTION_OPTIONS.add(PromotionChoice.PROMOTE_TO_KNIGHT);
    }

    /**
     * Creates a hard AI opponent.
     *
     * @param color the color of the hard AI opponent
     */
    public HardAIOpponent(ChessColor color) {
        super(color, "Hard AI");
        minimax = new Minimax(10, 1, 10, 5, 3, 3, 3, 1, PROMOTION_OPTIONS);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getFirstTurn(MatchHistory matchHistory) throws MatchOverException {
        return getTurn(matchHistory);
    }

    @Override
    public Turn getNextTurn(MatchHistory matchHistory) throws MatchOverException {
        return getTurn(matchHistory);
    }

    private Turn getTurn(MatchHistory matchHistory) {
        long start, elapsed;
        start = TestableCurrentTime.currentTimeMillis();
        Minimax.MinimaxNode node = minimax.find(DEPTH_LIMIT, matchHistory);
        Move move = node.getChosenMove();
        PromotionChoice choice = node.getPromotionChoice();
        elapsed = TestableCurrentTime.currentTimeMillis() - start;
        return new Turn(getColor(), move, elapsed, choice);
    }

    @Override
    public void observe(MatchResultIsInEvent event) {
        //TODO
    }

}
