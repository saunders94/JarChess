package com.example.jarchess.match.chessboard;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.Piece;

public class ChessboardReader {
    private static final String TAG = "ChessboardReader";
    private final Chessboard chessboard;

    public ChessboardReader(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public Chessboard getCopy() {
        return chessboard.getCopy();
    }

    public Chessboard getCopyWithMovementsApplied(@NonNull Coordinate origin, @NonNull Coordinate destination) {
        return chessboard.getCopyWithMovementsApplied(origin, destination);
    }

    public Chessboard getCopyWithMovementsApplied(@NonNull Coordinate origin, @NonNull Coordinate destination, @Nullable Coordinate captureCoordinate) {
        return chessboard.getCopyWithMovementsApplied(origin, destination, captureCoordinate);
    }

    @NonNull
    @Override
    public String toString() {
        return chessboard.toString();
    }

    @Nullable
    public Piece getPieceAt(@NonNull Coordinate coordinate) {
        Piece original = chessboard.getPieceAt(coordinate);
        if (original == null) {
            return null;
        }
        try {
            return (Piece) original.clone();
        } catch (CloneNotSupportedException e) {
            Log.e(TAG, "getPieceAt: clone should be supported for all pieces", e);
            throw new RuntimeException(e);
        }
    }

    public boolean isEmptyAt(Coordinate coordinate) {
        return chessboard.isEmptyAt(coordinate);
    }
}
