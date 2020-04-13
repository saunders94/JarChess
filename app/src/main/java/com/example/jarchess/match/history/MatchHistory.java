package com.example.jarchess.match.history;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.MoveExpert;
import com.example.jarchess.match.chessboard.Chessboard;
import com.example.jarchess.match.chessboard.ChessboardReader;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Bishop;
import com.example.jarchess.match.pieces.Knight;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.pieces.Queen;
import com.example.jarchess.match.pieces.Rook;
import com.example.jarchess.match.turn.Turn;

import java.util.Iterator;
import java.util.LinkedList;

import static com.example.jarchess.match.ChessColor.WHITE;

/**
 * Match History keeps track of all turns that have been made, the en passant details,
 * and draw conditions like 50 move rule and repetition rule.
 *
 * @author Joshua Zierman
 */
public class MatchHistory implements Iterable<Turn> {

    private static final String TAG = "MatchHistory";
    private final LinkedList<Turn> turnList = new LinkedList<>();
    private final MatchParticipant white, black;
    private final RepeatTracker repeatTracker;
    private final int[] movesSinceCaptureOrPawnMovement = new int[]{0, 0};
    private Chessboard chessboardAfterLastMove;
    private Chessboard chessboardBeforeLastMove;
    private Coordinate enPassantVulnerableCoordinate = null;
    private Coordinate enPassantRiskedPieceLocation = null;
    private boolean addHasBeenCalled = false;
    private final boolean isCopy;

    /**
     * Creates a new instance of MatchHistory
     *
     * @param white the white participant
     * @param black the black participant
     */
    public MatchHistory(MatchParticipant white, MatchParticipant black) {
        this.white = white;
        this.black = black;
        chessboardAfterLastMove = new Chessboard();
        chessboardBeforeLastMove = null;
        repeatTracker = new RepeatTracker();
        isCopy = false;
    }

    /**
     * @return the last move taken
     */
    public Move getLastMove() {
        if (!isCopy) {
            Log.v(TAG, "getLastMove is running on thread: " + Thread.currentThread().getName());
        }
        return turnList.peekLast().getMove();
    }

    private MatchHistory(MatchHistory original) {
        white = original.white;
        black = original.black;
        chessboardAfterLastMove = original.chessboardAfterLastMove.getCopy();
        if (original.chessboardBeforeLastMove != null) {
            chessboardBeforeLastMove = original.chessboardBeforeLastMove.getCopy();
        } else {
            chessboardBeforeLastMove = null;
        }
        turnList.addAll(original.turnList);
        repeatTracker = original.repeatTracker.getCopy();
        enPassantRiskedPieceLocation = original.enPassantRiskedPieceLocation;
        enPassantVulnerableCoordinate = original.enPassantVulnerableCoordinate;
        addHasBeenCalled = original.addHasBeenCalled;
        isCopy = true;
    }

    /**
     * adds a pre-validated turn to the history.
     *
     * @param turn the valid turn that was taken, not null
     */
    public void add(Turn turn) {

        // Needs to add the starting state before adding anything else but can't do it during construction
        if (!addHasBeenCalled) {
            addHasBeenCalled = true;
            repeatTracker.add(WHITE, MoveExpert.getInstance().getAllPossibleMovements(WHITE, this));
        }

        MoveExpert moveExpert = MoveExpert.getInstance();


        ChessColor color = turn.getColor();
        ChessColor nextColor = ChessColor.getOther(color);

        movesSinceCaptureOrPawnMovement[color.getIntValue()]++;
        chessboardBeforeLastMove = chessboardAfterLastMove.getCopy();

        //execute turn on copy chessboard
        Piece capturedPiece = null;
        Coordinate capturedPieceCoordinate = null;
        Move move = turn.getMove();
        for (PieceMovement movement : move) {

            Coordinate origin = movement.getOrigin();
            Coordinate destination = movement.getDestination();

            if (chessboardAfterLastMove.getPieceAt(destination) != null) {
                //perform normal capture
                capturedPieceCoordinate = destination;
                capturedPiece = chessboardAfterLastMove.remove(capturedPieceCoordinate);
            } else if (getEnPassantVulnerableCoordinate() == destination && chessboardAfterLastMove.getPieceAt(origin) instanceof Pawn) {
                // perform en passant capture
                capturedPieceCoordinate = getEnPassantRiskedPieceLocation();
                capturedPiece = chessboardAfterLastMove.remove(getEnPassantRiskedPieceLocation());
            }
            chessboardAfterLastMove.move(origin, destination);
            PromotionChoice choice = turn.getPromotionChoice();
            if (choice != null) {
                // promote the pawn
                Piece oldPiece = chessboardAfterLastMove.getPieceAt(destination);

                if (oldPiece instanceof Pawn && destination.getRank() == 1 || destination.getRank() == 8) {
                    Pawn pawn = (Pawn) oldPiece;
                    Piece newPiece;

                    switch (choice.getPieceType()) {
                        case ROOK:
                            newPiece = new Rook(pawn);
                            break;
                        case KNIGHT:
                            newPiece = new Knight(pawn);
                            break;
                        case BISHOP:
                            newPiece = new Bishop(pawn);
                            break;
                        case QUEEN:
                            newPiece = new Queen(pawn);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value piece type from promotion choice: " + choice.getPieceType());
                    }

                    chessboardAfterLastMove.remove(destination);
                    chessboardAfterLastMove.add(newPiece, destination);
                }
            }
        }

        // add the turn to the turn list
        turnList.addLast(turn);

        // track en passant vulnerabilities
        enPassantVulnerableCoordinate = null;
        enPassantRiskedPieceLocation = null;
        for (PieceMovement movement : turn.getMove()) {
            Piece movingPiece = chessboardBeforeLastMove.getPieceAt(movement.getOrigin());

            if (movingPiece instanceof Pawn) {
                movesSinceCaptureOrPawnMovement[color.getIntValue()] = 0;
                int originRow = movement.getOrigin().getRow();
                int originColumn = movement.getOrigin().getColumn();
                int destinationRow = movement.getDestination().getRow();
                int singleMoveRow = movingPiece.getColor() == WHITE ? originRow - 1 : originRow + 1;

                if (destinationRow != singleMoveRow) { // the move was a double forward move
                    enPassantVulnerableCoordinate = Coordinate.getByColumnAndRow(originColumn, singleMoveRow);
                    enPassantRiskedPieceLocation = Coordinate.getByColumnAndRow(originColumn, destinationRow);

                    if (!isCopy) {
                        Log.v(TAG, "add: enPassantVulnerableCoordinate = " + enPassantVulnerableCoordinate);
                        Log.v(TAG, "add: enPassantRiskedCoordinate = " + enPassantRiskedPieceLocation);
                    }
                }
            }
        }

        // track draw conditions
        if (capturedPiece != null) {
            movesSinceCaptureOrPawnMovement[color.getIntValue()] = 0;
        }

        if (movesSinceCaptureOrPawnMovement[color.getIntValue()] == 0) {
            movesSinceCaptureOrPawnMovement[nextColor.getIntValue()] = 0;
            repeatTracker.clear();

            if (!isCopy) {
                Log.i(TAG, "add: repeatTracker cleared");
            }
        }


        if (!isCopy) {
            Log.i(TAG, "add: moves since capture or pawn move: " + movesSinceCaptureOrPawnMovement[color.getIntValue()]);
        }

        if (!isCopy) {
            int repeats = repeatTracker.add(nextColor, MoveExpert.getInstance().getAllPossibleMovements(nextColor, this));
            Log.i(TAG, repeats + " repeats detected for " + nextColor);
        }
    }

    public MatchHistory getCopyWithMoveApplied(@NonNull Move move, @NonNull PromotionChoice desiredPromotionChoice) {
        MatchHistory matchHistory = new MatchHistory(this);
        PromotionChoice promotionChoice = null;

        //check color of moving pieces
        for (PieceMovement movement : move) {
            Piece p = chessboardAfterLastMove.getPieceAt(movement.getOrigin());

            if (p == null) {
                throw new RuntimeException("no piece at origin of movement " + movement);
            } else if (p.getColor() != getNextTurnColor()) {
                throw new RuntimeException("piece color would not be able to move");
            }
        }

        // handle promotion
        if (MoveExpert.getInstance().moveRequiresPromotion(move, this)) {
            promotionChoice = desiredPromotionChoice;
        }

        Turn turn = new Turn(getNextTurnColor(), move, 0L, promotionChoice);

        matchHistory.add(turn);

        return matchHistory;
    }

    /**
     * @return the coordinate that holds the vulnerable pawn that can be captured en passant
     */
    public Coordinate getEnPassantRiskedPieceLocation() {
        return enPassantRiskedPieceLocation;
    }

    /**
     * @return the coordinate that a pawn can make a capturing movement into to capture en passant
     */
    public Coordinate getEnPassantVulnerableCoordinate() {
        return enPassantVulnerableCoordinate;
    }

    public ChessboardReader getLastChessboardReader() {
        return chessboardAfterLastMove.getReader();
    }

    public boolean isCopy() {
        return isCopy;
    }

    /**
     * gets the last turn that was taken
     *
     * @return the last turn that was taken
     */
    public Turn getLastTurn() {
        return turnList.peekLast();
    }

    /**
     * gets the last turn that was taken
     *
     * @return the last turn that was taken
     */
    public Turn getLastTurn() {
        Log.v(TAG, "getlastTurn is running on thread: " + Thread.currentThread().getName());
        return turnList.peekLast();
    }

    /**
     * gets the last move that was made
     *
     * @param chessColor the turn color to check for
     * @return the number of turns that have passed for the player of the given color since a pawn has moved or a piece has been captured.
     */
    public int getMovesSinceCaptureOrPawnMovement(ChessColor chessColor) {
        return movesSinceCaptureOrPawnMovement[chessColor.getIntValue()];
    }

    public ChessColor getNextTurnColor() {
        if (getLastTurn() == null) {
            return WHITE;
        } else {
            return ChessColor.getOther(getLastTurn().getColor());
        }
    }

    public int getRepetitions() {
        return repeatTracker.getLastRepetitionCount();
    }

    @NonNull
    @Override
    public Iterator<Turn> iterator() {
        if (!isCopy) {
            Log.v(TAG, "iterator is running on thread: " + Thread.currentThread().getName());
        }
        return turnList.iterator();
    }
}
