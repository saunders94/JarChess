package com.example.jarchess.match.history;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.MoveExpert;
import com.example.jarchess.match.chessboard.Chessboard;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.turn.Turn;

import java.util.Iterator;
import java.util.LinkedList;

import static com.example.jarchess.match.ChessColor.WHITE;

//TODO javadocs
//TODO finish
public class MatchHistory implements Iterable<Turn> {

    private final LinkedList<Turn> turnList = new LinkedList<>();
    private final MatchParticipant white, black;
    private static final String TAG = "MatchHistory";
    private final RepeatTracker repeatTracker = new RepeatTracker();
    private Chessboard chessboardAfterLastMove;
    private Chessboard chessboardBeforeLastMove;
    private Coordinate enPassantVulnerableCoordinate = null;
    private Coordinate enPassentRiskedPieceLocation = null;
    private final int[] movesSinceCaptureOrPawnMovement = new int[]{0, 0};
    private boolean addHasBeenCalled = false;

    public MatchHistory(MatchParticipant white, MatchParticipant black) {
        this.white = white;
        this.black = black;
        chessboardAfterLastMove = new Chessboard();
        chessboardBeforeLastMove = null;
    }

    public void add(Turn turn, Chessboard updatedChessboard, Piece capturedPiece, Coordinate capturedPieceCoordinate) {
        Log.v(TAG, "add is running on thread: " + Thread.currentThread().getName());

        if (!addHasBeenCalled) {
            addHasBeenCalled = true;
            repeatTracker.add(WHITE, MoveExpert.getInstance().getAllPossibleMovements(WHITE, this, chessboardAfterLastMove));
        }
        ChessColor color = turn.getColor();
        ChessColor nextColor = ChessColor.getOther(color);
        enPassantVulnerableCoordinate = null;
        enPassentRiskedPieceLocation = null;

        movesSinceCaptureOrPawnMovement[color.getIntValue()]++;
        chessboardBeforeLastMove = chessboardAfterLastMove;
        chessboardAfterLastMove = updatedChessboard.getCopy();
        turnList.addLast(turn);

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
                    enPassentRiskedPieceLocation = Coordinate.getByColumnAndRow(originColumn, destinationRow);
                    Log.v(TAG, "add: enPassantVulnerableCoordinate = " + enPassantVulnerableCoordinate);
                    Log.v(TAG, "add: enPassantRiskedCoordinate = " + enPassentRiskedPieceLocation);
                }
            }
        }

        if (capturedPiece != null) {
            movesSinceCaptureOrPawnMovement[color.getIntValue()] = 0;
        }

        if (movesSinceCaptureOrPawnMovement[color.getIntValue()] == 0) {
            movesSinceCaptureOrPawnMovement[nextColor.getIntValue()] = 0;
            repeatTracker.clear();
            Log.i(TAG, "add: repeatTracker cleared");
        }

        Log.i(TAG, "add: moves since capture or pawn move: " + movesSinceCaptureOrPawnMovement[color.getIntValue()]);

        int repeats = repeatTracker.add(nextColor, MoveExpert.getInstance().getAllPossibleMovements(nextColor, this, chessboardAfterLastMove));
        Log.i(TAG, repeats + " repeats detected for " + nextColor);
    }

    public Coordinate getEnPassantVulnerableCoordinate() {
        return enPassantVulnerableCoordinate;
    }

    public Coordinate getEnPassentRiskedPieceLocation() {
        return enPassentRiskedPieceLocation;
    }

    public Move getLastMove() {
        Log.v(TAG, "getLastMove is running on thread: " + Thread.currentThread().getName());
        return turnList.peekLast().getMove();
    }

    public int getMovesSinceCaptureOrPawnMovement(ChessColor chessColor) {
        return movesSinceCaptureOrPawnMovement[chessColor.getIntValue()];
    }

    public Turn getlastTurn() {
        Log.v(TAG, "getlastTurn is running on thread: " + Thread.currentThread().getName());
        return turnList.peekLast();
    }

    @NonNull
    @Override
    public Iterator<Turn> iterator() {
        Log.v(TAG, "iterator is running on thread: " + Thread.currentThread().getName());
        return turnList.iterator();
    }
}
