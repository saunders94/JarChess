package com.example.jarchess.match.participant;

import android.util.Log;

import com.example.jarchess.LoggedThread;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.DrawResponse;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.result.ExceptionResult;
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
    public static final int MAX_TIME_BEFORE_SHORTCUT_SECONDS = 60;
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
    private static final int DEPTH_LIMIT = 4;
    private final Minimax minimax;
    private static final String TAG = "HardAIOpponent";

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
    protected long getMaxTimeBeforeShortcutSeconds() {
        return MAX_TIME_BEFORE_SHORTCUT_SECONDS;
    }

    private synchronized Turn getTurn(final MatchHistory matchHistory) throws MatchOverException {
        turn = null;
        notifyAll();
        new LoggedThread(TAG, new Runnable() {
            @Override
            public void run() {
                synchronized (me) {
                    try {
                        long start, elapsed;
                        start = TestableCurrentTime.currentTimeMillis();
                        Minimax.MinimaxNode node = null;
                        node = minimax.find(DEPTH_LIMIT, matchHistory);
                        Move move = node.getChosenMove();
                        PromotionChoice choice = node.getPromotionChoice();
                        elapsed = TestableCurrentTime.currentTimeMillis() - start;
                        turn = new Turn(getColor(), move, elapsed, choice);
                        me.notifyAll();
                        Log.d(TAG, "run() returned: " + turn);
                    } catch (Minimax.IsCanceledException e) {
                        isCanceled = true;
                        me.notifyAll();
                    }
                }
            }
        }, "AI_turnDecisionThread").start();

        while(turn == null && !isCanceled){
            try {
                wait();
            } catch (InterruptedException e) {
                Log.e(TAG, "getTurn: ", e);
                throw new MatchOverException(new ExceptionResult(getColor(), "Thread Inturupt", e));
            }
        }

        if(isCanceled){
            throw new MatchOverException(null); // this should only happen if another cause ended the match
        }
        return turn;
    }

    @Override
    public Turn getNextTurn(MatchHistory matchHistory) throws MatchOverException {
        return getTurn(matchHistory);
    }

    @Override
    public synchronized DrawResponse getDrawResponse(final MatchHistory matchHistory) {
        new LoggedThread(TAG, new Runnable() {
            @Override
            public void run() {
                synchronized (me) {
                    int value = 0;
                    try {
                        value = minimax.find(DEPTH_LIMIT, matchHistory).getValue();
                        boolean accepted = value < DRAW_VALUE;
                        Log.i(TAG, "respondToDrawRequest: value of " + value + (accepted ? " <" : " >=") + " draw_value of " + DRAW_VALUE);
                        drawResponse = accepted ? DrawResponse.ACCEPT : DrawResponse.REJECT;
                        me.notifyAll();
                    } catch (Minimax.IsCanceledException e) {
                        isCanceled = true;
                        me.notifyAll();
                    }
                }
            }
        }, "AI_drawRequestEvaluationThread").start();

        long start = TestableCurrentTime.currentTimeMillis();
        while (drawResponse == null && !isCanceled) {
            try {
                wait();

            } catch (InterruptedException e) {
                return DrawResponse.REJECT;
            }
        }

        return drawResponse;
    }



}
