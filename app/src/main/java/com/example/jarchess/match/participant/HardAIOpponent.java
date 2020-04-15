package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.DrawResponse;
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
    private static final int DEPTH_LIMIT = 3;
    private static final Collection<PromotionChoice> PROMOTION_OPTIONS = new LinkedList<>();
    private static final int DRAW_VALUE = -5;
    private static final int CHECK_MATE_VALUE = 100;
    private static final int CHECK_VALUE = 0;
    private static final int REPETITION_VALUE = 1;
    private static final int PAWN_VALUE = 2;
    private static final int ROOK_VALUE = 6;
    private static final int KNIGHT_VALUE = 6;
    private static final int BISHOP_VALUE = 6;
    private static final int QUEEN_VALUE = 14;
    private static final int KING_VALUE = 100;
    private final Minimax minimax;

    {
        PROMOTION_OPTIONS.add(PromotionChoice.PROMOTE_TO_QUEEN);
    }

    /**
     * Creates a hard AI opponent.
     *
     * @param color the color of the hard AI opponent
     */
    public HardAIOpponent(ChessColor color) {
        super(color, "Hard AI");
        minimax = new Minimax(CHECK_MATE_VALUE, CHECK_VALUE, DRAW_VALUE, REPETITION_VALUE, PAWN_VALUE, ROOK_VALUE, KNIGHT_VALUE, BISHOP_VALUE, QUEEN_VALUE, KING_VALUE, PROMOTION_OPTIONS);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getFirstTurn(MatchHistory matchHistory) throws MatchOverException {
        return getTurn(matchHistory);
    }

    @Override
    public DrawResponse respondToDrawRequest(MatchHistory matchHistory) {
        boolean accepted = minimax.find(DEPTH_LIMIT, matchHistory).getValue() < DRAW_VALUE;
        return new DrawResponse(accepted);
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
