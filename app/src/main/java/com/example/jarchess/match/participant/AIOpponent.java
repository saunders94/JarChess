package com.example.jarchess.match.participant;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.DrawResponse;
import com.example.jarchess.match.MoveExpert;
import com.example.jarchess.match.chessboard.ChessboardReader;
import com.example.jarchess.match.events.MatchEndingEvent;
import com.example.jarchess.match.events.MatchEndingEventListener;
import com.example.jarchess.match.events.MatchEndingEventManager;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.styles.avatar.AIAvatarStyle;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.testmode.TestableCurrentTime;
import com.example.jarchess.testmode.TestableRandom;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

import static com.example.jarchess.match.pieces.Piece.Type.BISHOP;
import static com.example.jarchess.match.pieces.Piece.Type.KING;
import static com.example.jarchess.match.pieces.Piece.Type.KNIGHT;
import static com.example.jarchess.match.pieces.Piece.Type.PAWN;
import static com.example.jarchess.match.pieces.Piece.Type.QUEEN;
import static com.example.jarchess.match.pieces.Piece.Type.ROOK;
import static java.lang.Math.abs;

/**
 * An AI opponent is a match participant that is controlled by an algorithm.
 *
 * @author Joshua Zierman
 */
public abstract class AIOpponent implements MatchParticipant, MatchEndingEventListener {
    private static final String TAG = "AIOpponent";
    private static final long TIME_BETWEEN_WAITS_MILLIS = 400L;
    private final ChessColor color;
    private final String name;
    protected volatile boolean isCanceled = false;
    protected DrawResponse drawResponse = null;
    protected Turn turn = null;
    protected AIOpponent me = this;
    private Long lastWaitTimeMillis = null;
    private long lastNodeNumber = 0; // used for log
    private long pruneCount = 0; // used for log

    /**
     * Creates an AI opponent.
     *
     * @param color the color of the AI opponent
     * @param name  the name of the AI opponent
     */
    public AIOpponent(ChessColor color, String name) {
        this.color = color;
        this.name = name;
        MatchEndingEventManager.getInstance().add(this);
    }

    @Override
    public void observe(MatchEndingEvent flagFallEvent) {
        isCanceled = true;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public AvatarStyle getAvatarStyle() {
        return AIAvatarStyle.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChessColor getColor() {
        return color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    protected abstract long getMaxTimeBeforeShortcutSeconds();

    protected class Minimax {

        public static final int NODES_BETWEEN_WAIT = 1000;
        private static final String TAG = "AIOpponent.Minimax";
        private static final long WAIT_LENGTH_MILLIS = 10;
        private final int checkMateValue;
        private final int checkValue;
        private final Map<Piece.Type, Integer> pieceValues;
        private final ChessColor myColor = color;
        private final ChessColor theirColor = ChessColor.getOther(color);
        private final MoveExpert moveExpert = MoveExpert.getInstance();
        private final Collection<PromotionChoice> promotionChoicesToConsider;
        private final int drawValue;
        private final int repetitionValue;

        public Minimax(int checkMateValue, int checkValue, int drawValue, int repetitionValue, int pawnValue, int rookValue, int knightValue, int bishopValue, int queenValue, int kingValue, Collection<PromotionChoice> promotionChoicesToConsider) {
            this.checkMateValue = abs(checkMateValue);
            this.checkValue = abs(checkValue);
            this.drawValue = abs(drawValue);
            this.repetitionValue = abs(repetitionValue);
            this.promotionChoicesToConsider = promotionChoicesToConsider;

            pieceValues = new EnumMap<Piece.Type, Integer>(Piece.Type.class);

            pieceValues.put(KING, abs(kingValue));
            pieceValues.put(QUEEN, abs(queenValue));
            pieceValues.put(BISHOP, abs(bishopValue));
            pieceValues.put(KNIGHT, abs(knightValue));
            pieceValues.put(ROOK, abs(rookValue));
            pieceValues.put(PAWN, abs(pawnValue));

        }


        protected int calculateValue(MatchHistory matchHistory) {
            int value = 0;
            ChessboardReader chessboardReader = matchHistory.getLastChessboardReader();
            int repetitions = matchHistory.getRepetitions();
            int movesSinceCaptureOrPawnMovement = matchHistory.getMovesSinceCaptureOrPawnMovement(matchHistory.getNextTurnColor());

            if (moveExpert.isInCheck(theirColor, matchHistory)) {
                if (!moveExpert.hasMoves(theirColor, matchHistory)) {
                    value += checkMateValue;
                } else {
                    value += checkValue;
                }

            } else if (moveExpert.isInCheck(myColor, matchHistory)) {
                if (!moveExpert.hasMoves(myColor, matchHistory)) {
                    value -= checkMateValue;
                } else {
                    value -= checkValue;
                }
            }

            Turn turn = matchHistory.getLastTurn();
            ChessColor turnColor = turn == null ? null : turn.getColor();

            if (turn != null && turnColor == myColor) {
                value -= repetitions * repetitionValue;

                if (movesSinceCaptureOrPawnMovement > 50) {
                    return -drawValue;
                } else if (repetitions > 3) {
                    return -drawValue;
                }
            }


            // add the sum of the values of the pieces
            for (Coordinate c : Coordinate.values()) {
                value += getValueOfPiece(chessboardReader.getPieceAt(c));
            }


            return value;
        }

        protected MinimaxNode find(int depthLimit, MatchHistory matchHistory) throws IsCanceledException {

            return new MinimaxNode(depthLimit, matchHistory);
        }

        protected int getValueOfPiece(Piece piece) {
            if (piece == null) {
                return 0;
            } else {
                Integer value = pieceValues.get(piece.getType());
                if (value == null) {
                    // if this ever happens we have a piece type that has no value, which should not happen.
                    String msg = piece + " did not have a value in the map";
                    RuntimeException e = new RuntimeException(msg);
                    Log.wtf(TAG, "getValueOfPiece: ", e);
                    throw e;
                }
                if (piece.getColor() == myColor) {
                    return value;
                } else {
                    return -value;
                }
            }
        }


        protected class IsCanceledException extends Exception {

        }

        protected class MinimaxNode implements Comparable<MinimaxNode> {

            private final MatchHistory matchHistory;
            private final int value;
            private final int depth;
            private final int depthLimit;
            private final Move chosenMove;
            private final PromotionChoice promotionChoice;
            private final TestableRandom random;

            /**
             * the choice out of explored nodes that maximizes AI value
             */
            private MinimaxNode alpha;

            /**
             * the choice out of explored nodes that minimizes the AI's Opponent's Value
             */
            private MinimaxNode beta;

            private MinimaxNode chosen;

            private long nodeNumber;
            private Long startMillis = null;

            private MinimaxNode(int depthLimit, MatchHistory matchHistory) throws IsCanceledException {
                this(depthLimit, 0, null, null, matchHistory);

                Log.i(TAG, "Minimax: generated " + lastNodeNumber + " nodes recursively");
                Log.i(TAG, "Minimax: pruneCount = " + pruneCount);
                Log.i(TAG, "Minimax: selected node's Value Change: " + (this.value - calculateValue(matchHistory)));
                Log.i(TAG, "Minimax: selected node's Chosen Move: " + this.chosenMove);
                Log.i(TAG, "Minimax: " + this);
                pruneCount = 0;
                lastNodeNumber = 0;
            }

            private MinimaxNode(int depthLimit, int depth, MinimaxNode alpha, MinimaxNode beta, MatchHistory matchHistory) throws IsCanceledException {

                synchronized (AIOpponent.this) {
                    if (startMillis == null) {
                        startMillis = TestableCurrentTime.currentTimeMillis();
                    }
                    this.depth = depth;
                    this.depthLimit = depthLimit;
                    this.nodeNumber = getNextNodeNumber();
                    this.matchHistory = matchHistory;
                    this.alpha = alpha;
                    this.beta = beta;
                    random = TestableRandom.getInstance();


                    if (depth < 0) {
                        throw new IllegalArgumentException("depth was less than zero");
                    }
                    if (depthLimit < 1) {
                        throw new IllegalArgumentException("depth limit was less than one");
                    }

                    if (lastWaitTimeMillis == null) {
                        lastWaitTimeMillis = TestableCurrentTime.currentTimeMillis();
                    } else if (TestableCurrentTime.currentTimeMillis() - lastWaitTimeMillis > TIME_BETWEEN_WAITS_MILLIS) {

                        try {
                            AIOpponent.this.wait(WAIT_LENGTH_MILLIS);
                        } catch (InterruptedException e) {
                            isCanceled = true;
                            AIOpponent.this.notifyAll();
                        }
                        lastWaitTimeMillis = TestableCurrentTime.currentTimeMillis();
                    }


                    if (isCanceled) {
                        Log.i(TAG, "MinimaxNode: canceled!");
                        throw new IsCanceledException();
                    }
                    if (depth >= depthLimit || TestableCurrentTime.currentTimeMillis() - startMillis > getMaxTimeBeforeShortcutSeconds() * 1000) {
                        chosen = this;
                        value = calculateValue(matchHistory);
                        chosenMove = null;
                        promotionChoice = null;
                    } else {
                        int tmp;

                        Collection<Move> moves;
                        MinimaxNode n;
                        moves = moveExpert.getAllLegalMoves(matchHistory.getNextTurnColor(), matchHistory);
                        Move chosenMoveTmp = null;
                        PromotionChoice chosenPromotionChoiceTmp = null;
                        MinimaxNode chosenNode = null;

                        for (Move move : moves) {
                            Collection<PromotionChoice> choices = new LinkedList<>();
                            if (moveExpert.moveRequiresPromotion(move, matchHistory)) {
                                choices.addAll(promotionChoicesToConsider);
                            } else {
                                choices.add(null);
                            }
                            for (PromotionChoice choice : choices) {

                                if (matchHistory.getNextTurnColor() == myColor) {
                                    // looking for max value

                                    n = new MinimaxNode(depthLimit, depth + 1, this.alpha, this.beta, matchHistory.getCopyWithMoveApplied(move, choice));

                                    //prune if possible
                                    if (this.beta != null && (n.compareTo(this.beta) > 0 || (n.compareTo(this.beta) == 0 && random.getNextBoolean()))) {
                                        pruneCount++;
                                        chosen = n;
                                        value = n.value;
                                        chosenMove = move;
                                        promotionChoice = choice;
                                        return;
                                    }

                                    // check for new max node
                                    if (chosenNode == null || chosenNode.compareTo(n) < 0 || (chosenNode.compareTo(n) == 0 && random.getNextBoolean())) {

                                        // we found a new max node
                                        chosenNode = n;
                                        chosenMoveTmp = move;
                                        chosenPromotionChoiceTmp = choice;

                                        if (this.alpha == null || chosenNode.compareTo(this.alpha) > 0 || (chosenNode.compareTo(this.alpha) == 0 && random.getNextBoolean())) {
                                            this.alpha = chosenNode;
                                        }
                                    }


                                } else {
                                    //looking for min value

                                    n = new MinimaxNode(depthLimit, depth + 1, this.alpha, this.beta, matchHistory.getCopyWithMoveApplied(move, choice));

                                    //prune if possible
                                    if (this.alpha != null && (n.compareTo(this.alpha) < 0 || (n.compareTo(this.alpha) == 0 && random.getNextBoolean()))) {
                                        pruneCount++;
                                        chosen = n;
                                        value = n.getValue();
                                        chosenMove = move;
                                        promotionChoice = choice;
                                        return;
                                    }

                                    // check for new min node
                                    if (chosenNode == null || chosenNode.compareTo(n) > 0 || (chosenNode.compareTo(n) == 0 && random.getNextBoolean())) {

                                        // we found a new min node
                                        chosenNode = n;
                                        chosenMoveTmp = move;
                                        chosenPromotionChoiceTmp = choice;

                                        if (this.beta == null || chosenNode.compareTo(this.beta) < 0 || (chosenNode.compareTo(this.beta) == 0 && random.getNextBoolean())) {
                                            this.beta = chosenNode;
                                        }
                                    }
                                }
                            }
                        }
                        if (chosenNode == null) {
                            // there where no possible moves
                            chosen = this;
                            value = calculateValue(matchHistory);
                        } else {
                            chosen = chosenNode;
                            value = chosenNode.getValue();
                        }
                        chosenMove = chosenMoveTmp;
                        promotionChoice = chosenPromotionChoiceTmp;
                    }
                }
            }

            @Override
            public int compareTo(MinimaxNode o) {
                return value - o.value;
            }

            public Move getChosenMove() {
                return chosenMove;
            }

            protected Turn getLastTurn() {
                return matchHistory.getLastTurn();
            }

            private synchronized long getNextNodeNumber() {
                return lastNodeNumber++;
            }

            protected PromotionChoice getPromotionChoice() {
                return promotionChoice;
            }

            public int getValue() {
                return value;
            }


            @NonNull
            @Override
            public String toString() {
                StringBuilder s = new StringBuilder("\n");
                s.append(matchHistory.getLastChessboardReader()).append("\n");
                s.append("node[").append(nodeNumber).append("]").append(" ");
                s.append("value[").append(value).append("] ");
                s.append("move[").append(chosenMove).append("]\n");
                if (chosen != this) {
                    s.append(chosen);
                }
                return s.toString();
            }
        }
    }


}
