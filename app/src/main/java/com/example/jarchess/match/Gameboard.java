package com.example.jarchess.match;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.pieces.Bishop;
import com.example.jarchess.match.pieces.King;
import com.example.jarchess.match.pieces.Knight;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.Queen;
import com.example.jarchess.match.pieces.Rook;

import java.util.Collection;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

/**
 * Gameboard is a container that holds Pieces
 * <p>
 * No game rules logic is handled by Gameboard
 *
 * @author Joshua Zierman
 */
public class Gameboard {
    private static final int ROW_COUNT = 8;
    private static final int COLUMN_COUNT = 8;
    private final Piece[][] pieces;
    private boolean isCopy;

    /**
     * Private constructor used to create the instance of gameboard
     */
    Gameboard() {
        pieces = new Piece[COLUMN_COUNT][ROW_COUNT];

        Piece tmp;

        for (char file : Coordinate.FILES) {
            add((tmp = new Pawn(BLACK, file)), tmp.getStartingPosition());
            add((tmp = new Pawn(WHITE, file)), tmp.getStartingPosition());
        }

        for (ChessColor c : ChessColor.values()) {
            add(new Rook(c, Rook.QUEENWARD_STARTING_FILE));
            add(new Rook(c, Rook.KINGWARD_STARTING_FILE));

            add(new Knight(c, Knight.QUEENWARD_STARTING_FILE));
            add(new Knight(c, Knight.KINGWARD_STARTING_FILE));

            add(new Bishop(c, Bishop.QUEENWARD_STARTING_FILE));
            add(new Bishop(c, Bishop.KINGWARD_STARTING_FILE));

            add(new Queen(c));
            add(new King(c));
        }

        isCopy = false;
    }

    private Gameboard(Gameboard gameboard) {
        pieces = new Piece[COLUMN_COUNT][ROW_COUNT];
        for (Coordinate coordinate : Coordinate.values()) {
            int column = coordinate.getColumn();
            int row = coordinate.getRow();

            Piece original = gameboard.getPieceAt(coordinate);

            if (original != null) {
                try {
                    pieces[column][row] = (Piece) original.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        isCopy = true;
    }

    public Gameboard getCopyWithMovementsApplied(Collection<PieceMovement> movements) {
        Gameboard copy = new Gameboard(this);
        for (PieceMovement movement : movements) {
            copy.move(movement.getOrigin(), movement.getDestination());
        }
        return copy;
    }

    public Gameboard getCopyWithMovementsApplied(Coordinate origin, Coordinate destination) {
        Gameboard copy = new Gameboard(this);
        copy.move(origin, destination);
        return copy;
    }

    public Gameboard getCopyWithMovementsApplied(Coordinate origin1, Coordinate destination1, Coordinate origin2, Coordinate destination2) {
        Gameboard copy = new Gameboard(this);
        copy.move(origin1, destination1);
        copy.move(origin2, destination2);
        return copy;
    }


    /**
     * Adds a piece to the gameboard at starting position
     *
     * @param piece the piece to add.
     */
    private void add(Piece piece) {
        add(piece, piece.getStartingPosition());
    }

    /**
     * Gets the Piece at a specified coordinate or null if there is no piece at that position
     *
     * @param coordinate of the location on the board that the piece will be returned
     * @return Piece at the specified coordinate
     */
    @Nullable
    public Piece getPieceAt(@NonNull Coordinate coordinate) {
        final int row = coordinate.getRow();
        final int column = coordinate.getColumn();
        return pieces[column][row];
    }

    /**
     * Adds a piece to a specified coordinate on the gameboard
     *
     * @param piece      The piece to be added to the gameboard
     * @param coordinate on the gameboard that the piece will be added to
     */
    private void add(@NonNull Piece piece, @NonNull Coordinate coordinate) {

        //noinspection ConstantConditions
        if (piece == null) {
            throw new IllegalArgumentException("Piece.add expected a non-null Piece argument but got null");
        }

        //noinspection ConstantConditions
        if (coordinate == null) {
            throw new IllegalArgumentException("Piece.add expected a non-null Coordinate argument but got null");
        }

        final int row = coordinate.getRow();
        final int column = coordinate.getColumn();

        pieces[column][row] = piece;
    }


    /**
     * Moves a piece from one coordinate to another on the gameboard
     *
     * @param originCoordinate      the coordinate of the piece to be moved
     * @param destinationCoordinate the coordinate of where the piece will be after the move
     */
    public void move(Coordinate originCoordinate, Coordinate destinationCoordinate) {

        Piece piece = getPieceAt(originCoordinate);

        add(piece, destinationCoordinate);
        remove(originCoordinate);
        piece.setAsMoved();
    }

    /**
     * Resets the gameboard to stating positions.
     */
    public void reset() {

        for (int column : Coordinate.COLUMNS) {
            for (int row : Coordinate.ROWS) {
                pieces[column][row] = null;
            }
        }


        Piece tmp;

        for (char file : Coordinate.FILES) {
            add((tmp = new Pawn(BLACK, file)), tmp.getStartingPosition());
            add((tmp = new Pawn(WHITE, file)), tmp.getStartingPosition());
        }

        for (ChessColor c : ChessColor.values()) {
            add(new Rook(c, Rook.QUEENWARD_STARTING_FILE));
            add(new Rook(c, Rook.KINGWARD_STARTING_FILE));

            add(new Knight(c, Knight.QUEENWARD_STARTING_FILE));
            add(new Knight(c, Knight.KINGWARD_STARTING_FILE));

            add(new Bishop(c, Bishop.QUEENWARD_STARTING_FILE));
            add(new Bishop(c, Bishop.KINGWARD_STARTING_FILE));

            add(new Queen(c));
            add(new King(c));
        }
    }


    public Piece remove(Coordinate coordinate) {

        Piece pieceRemoved = pieces[coordinate.getColumn()][coordinate.getRow()];
        pieces[coordinate.getColumn()][coordinate.getRow()] = null;

        return pieceRemoved;
    }


    /**
     * Checks to see if the board is empty at a specified coordinate
     *
     * @param coordinate the coordinate to check
     * @return true if the gameboard is empty at the specified coordinate
     */
    public boolean isEmptyAt(Coordinate coordinate) {   //TODO need tests
        return getPieceAt(coordinate) == null;
    }

}
