package com.example.jarchess.match;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.turn.Turn;

import java.util.Iterator;
import java.util.LinkedList;

import static androidx.constraintlayout.widget.Constraints.TAG;

//TODO javadocs
//TODO finish
public class MatchHistory implements Iterable<Turn> {

    private final LinkedList<Turn> turnList = new LinkedList<>();
    private final MatchParticipant white, black;
    private Chessboard chessboardAfterLastMove;
    private Chessboard chessboardBeforeLastMove;
    private Coordinate enPassantVulnerableCoordinate = null;
    private Coordinate enPassentRiskedPieceLocation = null;

    public MatchHistory(MatchParticipant white, MatchParticipant black) {
        this.white = white;
        this.black = black;
        chessboardAfterLastMove = new Chessboard();
        chessboardBeforeLastMove = null;
    }

    public void add(Turn turn) {
        Log.d(TAG, "add is running on thread: " + Thread.currentThread().getName());
        chessboardBeforeLastMove = chessboardAfterLastMove;
        chessboardAfterLastMove = chessboardBeforeLastMove.getCopyWithMovementsApplied(turn.getMove());
        turnList.addLast(turn);

        for (PieceMovement movement : turn.getMove()) {
            Piece piece = chessboardBeforeLastMove.getPieceAt(movement.getOrigin());

            if (piece instanceof Pawn) {
                int originRow = movement.getOrigin().getRow();
                int originColumn = movement.getOrigin().getColumn();
                int destinationRow = movement.getDestination().getRow();
                int singleMoveRow = piece.getColor() == ChessColor.WHITE ? originRow - 1 : originRow + 1;

                if (destinationRow != singleMoveRow) { // the move was a double forward move
                    enPassantVulnerableCoordinate = Coordinate.getByColumnAndRow(originColumn, singleMoveRow);
                    enPassentRiskedPieceLocation = Coordinate.getByColumnAndRow(originColumn, destinationRow);
                    Log.d(TAG, "add: enPassantVulnerableCoordinate = " + enPassantVulnerableCoordinate);
                    Log.d(TAG, "add: enPassantRiskedCoordinate = " + enPassentRiskedPieceLocation);
                } else {
                    enPassantVulnerableCoordinate = null;
                    enPassentRiskedPieceLocation = null;
                }
            }
        }
    }

    public Coordinate getEnPassantVulnerableCoordinate() {
        return enPassantVulnerableCoordinate;
    }

    public Coordinate getEnPassentRiskedPieceLocation() {
        return enPassentRiskedPieceLocation;
    }

    public Move getLastMove() {
        Log.d(TAG, "getLastMove is running on thread: " + Thread.currentThread().getName());
        return turnList.peekLast().getMove();
    }

    public Turn getlastTurn() {
        Log.d(TAG, "getlastTurn is running on thread: " + Thread.currentThread().getName());
        return turnList.peekLast();
    }

    @NonNull
    @Override
    public Iterator<Turn> iterator() {
        Log.d(TAG, "iterator is running on thread: " + Thread.currentThread().getName());
        return turnList.iterator();
    }
}
