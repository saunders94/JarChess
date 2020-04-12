package com.example.jarchess.match.participant;

import android.util.Log;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.MoveExpert;
import com.example.jarchess.match.chessboard.ChessboardReader;
import com.example.jarchess.match.events.MatchResultIsInEventManager;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.styles.avatar.AIAvatarStyle;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.turn.Turn;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import static com.example.jarchess.match.pieces.Piece.Type.BISHOP;
import static com.example.jarchess.match.pieces.Piece.Type.KING;
import static com.example.jarchess.match.pieces.Piece.Type.KNIGHT;
import static com.example.jarchess.match.pieces.Piece.Type.PAWN;
import static com.example.jarchess.match.pieces.Piece.Type.QUEEN;
import static com.example.jarchess.match.pieces.Piece.Type.ROOK;

/**
 * An AI opponent is a match participant that is controlled by an algorithm.
 *
 * @author Joshua Zierman
 */
public abstract class AIOpponent implements MatchParticipant {
    private final ChessColor color;
    private final String name;

    /**
     * Creates an AI opponent.
     *
     * @param color the color of the AI opponent
     * @param name  the name of the AI opponent
     */
    public AIOpponent(ChessColor color, String name) {
        this.color = color;
        this.name = name;

        MatchResultIsInEventManager.getInstance().add(this);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void resign() {
        //TODO
    }

    protected class Minimax {
        private static final String TAG = "AIOpponent.Minimax";
        private final int checkMateValue;
        private final Map<Piece.Type, Integer> pieceValues;
        private final ChessColor myColor = color;
        private final ChessColor theirColor = ChessColor.getOther(color);
        private final MoveExpert moveExpert = MoveExpert.getInstance();

        public Minimax(int checkMateValue, int kingValue, int queenValue, int bishopValue, int knightValue, int rookValue, int pawnValue) {
            this.checkMateValue = checkMateValue;

            pieceValues = new EnumMap<Piece.Type, Integer>(Piece.Type.class);

            pieceValues.put(KING, kingValue);
            pieceValues.put(QUEEN, queenValue);
            pieceValues.put(BISHOP, bishopValue);
            pieceValues.put(KNIGHT, knightValue);
            pieceValues.put(ROOK, rookValue);
            pieceValues.put(PAWN, pawnValue);

        }

        protected int calculateValue(MatchHistory matchHistory) {
            int value = 0;
            ChessboardReader chessboardReader = matchHistory.getLastChessboardReader();

            for (Coordinate c : Coordinate.values()) {
                value += getValueOfPiece(chessboardReader.getPieceAt(c));
            }


            return value;
        }

        protected Move findMove(int depthLimit, MatchHistory matchHistory) {
            return new MinimaxNode(depthLimit, matchHistory).chosenMove;
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

        protected class MinimaxNode implements Comparable<MinimaxNode> {
            private final MatchHistory matchHistory;
            private final int value;
            private final Move chosenMove;
            private MinimaxNode min, max;

            public MinimaxNode(int depthLimit, MatchHistory matchHistory) {
                this(depthLimit, 0, null, null, matchHistory);
            }

            private MinimaxNode(int depthLimit, int depth, MinimaxNode bestMinChoice, MinimaxNode bestMaxChoice, MatchHistory matchHistory) {
                this.matchHistory = matchHistory;
                this.min = bestMinChoice;
                this.max = bestMaxChoice;

                if (depth >= depthLimit) {
                    value = calculateValue(matchHistory);
                    chosenMove = null;
                } else {
                    int tmp;

                    Collection<Move> moves;
                    MinimaxNode n;
                    moves = moveExpert.getAllLegalMoves(matchHistory.getNextTurnColor(), matchHistory);
                    if (depth % 2 == 0) {
                        // looking for max

                        MinimaxNode maxNode = null;
                        Move chosenMoveTmp = null;

                        for (Move move : moves) {
                            n = new MinimaxNode(depthLimit, depth + 1, min, max, matchHistory.getCopyWithMoveApplied(move));

                            //prune if possible
                            if (min != null && n.compareTo(min) >= 0) {
                                value = n.getValue();
                                chosenMove = move;
                                return;
                            }

                            // check for new max node
                            if (maxNode == null || maxNode.compareTo(n) < 0) {
                                // we found a new max
                                maxNode = n;
                                chosenMoveTmp = move;

                                if (max == null || maxNode.compareTo(max) > 0) {
                                    max = maxNode;
                                }
                            }
                        }

                        chosenMove = chosenMoveTmp;
                        if (maxNode == null) {
                            // there where no possible moves
                            value = calculateValue(matchHistory);
                        } else {
                            value = maxNode.getValue();
                        }

                    } else {
                        //looking for min
                        MinimaxNode minNode = null;
                        Move chosenMoveTmp = null;

                        for (Move move : moves) {
                            n = new MinimaxNode(depthLimit, depth + 1, min, max, matchHistory.getCopyWithMoveApplied(move));

                            //prune if possible
                            if (max != null && n.compareTo(max) <= 0) {
                                value = n.getValue();
                                chosenMove = move;
                                return;
                            }

                            // check for new min node
                            if (minNode == null || minNode.compareTo(n) > 0) {
                                // we found a new min
                                minNode = n;
                                chosenMoveTmp = move;

                                if (min == null || minNode.compareTo(min) < 0) {
                                    max = minNode;
                                }
                            }
                        }

                        chosenMove = chosenMoveTmp;
                        if (minNode == null) {
                            // there where no possible moves
                            value = calculateValue(matchHistory);
                        } else {
                            value = minNode.getValue();
                        }
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

            protected Turn getTurn() {
                return matchHistory.getLastTurn();
            }

            public int getValue() {
                return value;
            }
        }
    }

}
