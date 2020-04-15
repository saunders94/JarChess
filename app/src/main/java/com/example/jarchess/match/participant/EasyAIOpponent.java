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
 * An easy AI Opponent is an AI opponent that is considered easy difficulty to play against.
 *
 * @author Joshua Zierman
 */
public class EasyAIOpponent extends AIOpponent {

    public static final boolean IS_IMPLEMENTED = true;
    public static final int CHECK_MATE_VALUE = 10;
    public static final int CHECK_VALUE = 1;
    public static final int REPETITION_VALUE = 2;
    public static final int PAWN_VALUE = 1;
    public static final int ROOK_VALUE = 3;
    public static final int KNIGHT_VALUE = 3;
    public static final int BISHOP_VALUE = 3;
    public static final int QUEEN_VALUE = 5;
    public static final int KING_VALUE = 10;
    private static final int DEPTH_LIMIT = 2;
    private static final int DRAW_VALUE = -5;
    private final Minimax minimax;
    private static final Collection<PromotionChoice> PROMOTION_OPTIONS = new LinkedList<>();

    {
        PROMOTION_OPTIONS.add(PromotionChoice.PROMOTE_TO_QUEEN);
    }

    /**
     * Creates an easy AI opponent.
     *
     * @param color the color of the easy AI Opponent
     */
    public EasyAIOpponent(ChessColor color) {
        super(color, "Easy AI");
        minimax = new Minimax(CHECK_MATE_VALUE, CHECK_VALUE, DRAW_VALUE, REPETITION_VALUE, PAWN_VALUE, ROOK_VALUE, KNIGHT_VALUE, BISHOP_VALUE, QUEEN_VALUE, KING_VALUE, PROMOTION_OPTIONS);
    }

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
        //do nothing
    }
}