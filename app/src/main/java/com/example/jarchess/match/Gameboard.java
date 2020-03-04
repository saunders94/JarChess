package com.example.jarchess.match;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.pieces.Bishop;
import com.example.jarchess.match.pieces.King;
import com.example.jarchess.match.pieces.Knight;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.Queen;
import com.example.jarchess.match.pieces.Rook;

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
    private static Gameboard instance;
    private final Piece[][] pieces = new Piece[ROW_COUNT][COLUMN_COUNT];

    Gameboard() {


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

    public static Gameboard getInstance() {
        if (instance == null) {
            instance = new Gameboard();
        }
        return instance;
    }

    private void add(Piece piece) {
        add(piece, piece.getStartingPosition());
    }

    public void destroy() {
        instance = null;
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
        return pieces[row][column];
    }

    /**
     * Adds a piece to a specified coordinate on the gameboard
     *
     * @param piece      The piece to be added to the gameboard
     * @param coordinate on the gameboard that the piece will be added to
     * @throws AlreadyOccupiedException if the coordinate provided already has a Piece in it
     */
    private void add(@NonNull Piece piece, @NonNull Coordinate coordinate) throws AlreadyOccupiedException {

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

        if (getPieceAt(coordinate) != null) {
            throw new AlreadyOccupiedException(coordinate);
        }

        pieces[row][column] = piece;
    }


    /**
     * Moves a piece from one coordinate to another on the gameboard
     *
     * @param originCoordinate      the coordinate of the piece to be moved
     * @param destinationCoordinate the coordinate of where the piece will be after the setAsMoved
     * @throws AlreadyOccupiedException if the destination is occupied
     */
    public void move(Coordinate originCoordinate, Coordinate destinationCoordinate) throws AlreadyOccupiedException, AlreadyEmptyException {
        if (!isEmptyAt(destinationCoordinate)) {
            throw new AlreadyOccupiedException(destinationCoordinate);
        }
        //TODO complete by finishing writing tests and making them all work

        //setAsMoved piece to new spot

        //call setAsMoved method on piece to mark it as moved
    }


    /**
     * Moves a piece from one coordinate to another on the gameboard
     *
     * @param coordinate the coordinate to remove a piece from;
     * @throws AlreadyOccupiedException if the destination is occupied
     */
    public void remove(Coordinate coordinate) throws AlreadyOccupiedException, AlreadyEmptyException {
        if (isEmptyAt(coordinate)) {
            throw new AlreadyEmptyException(coordinate);
        }
        //TODO complete by finishing writing tests and making them all work
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

    /**
     * An exception to be thrown when a piece is attempted to be added or moved to an occupied space
     *
     * @author Joshua Zierman
     */
    public static class AlreadyOccupiedException extends IllegalArgumentException {

        private Coordinate coordinateThatWasAlreadyOccupied;

        /**
         * Creates an AlreadyOccupiedException object
         *
         * @param coordinate the coordinate that was already occupied
         */
        public AlreadyOccupiedException(@NonNull Coordinate coordinate) {
            super(makeMessage(coordinate));

            coordinateThatWasAlreadyOccupied = coordinate;
        }

        /**
         * Makes an exception message that describes the problem
         *
         * @param coordinate the coordinate that was already occupied
         * @return the String exception message describing what went wrong
         * @throws IllegalArgumentException if an invalid or null coordinate is provided
         */
        private static String makeMessage(Coordinate coordinate) throws IllegalArgumentException {

            if (coordinate == null) {
                throw new IllegalArgumentException("AlreadyOccupiedException cannot be constructed with null coordinate");
            }

            final int column = coordinate.getColumn();
            final int row = coordinate.getRow();

            if (column < 0 || column >= COLUMN_COUNT) {
                throw new IllegalArgumentException("AlreadyOccupiedException cannot be constructed with coordinate that returns column out of range [0, 8)");
            }

            if (row < 0 || row >= ROW_COUNT) {
                throw new IllegalArgumentException("AlreadyOccupiedException cannot be constructed with coordinate that returns row out of range [0, 8)");
            }

            return coordinate.toString() + " is already occupied.";
        }

        /**
         * Gets the coordinate that was already occupied, causing the exception to be thrown
         *
         * @return the coordinate that was already occupied
         */
        public Coordinate getCoordinateThatWasAlreadyOccupied() {
            return coordinateThatWasAlreadyOccupied;
        }
    }

    /**
     * An exception to be thrown when a piece is attempted to be moved or removed from an empty space
     *
     * @author Joshua Zierman
     */
    public static class AlreadyEmptyException extends IllegalArgumentException {   //TODO needs tests

        private Coordinate coordinateThatWasAlreadyEmpty;

        /**
         * Constructs an instance of a AlreadyEmptyException
         *
         * @param coordinate the coordinate that was already empty.
         */
        public AlreadyEmptyException(@NonNull Coordinate coordinate) {
            super(makeMessage(coordinate));

            coordinateThatWasAlreadyEmpty = coordinate;
        }

        /**
         * Makes a exception message
         *
         * @param coordinate the coordinate that was empty
         * @return the String exception message describing what went wrong
         * @throws IllegalArgumentException if a invalid or null coordinate was detected
         */
        private static String makeMessage(Coordinate coordinate) throws IllegalArgumentException {

            if (coordinate == null) {
                throw new IllegalArgumentException("AlreadyEmptyException cannot be constructed with null coordinate");
            }

            final int column = coordinate.getColumn();
            final int row = coordinate.getRow();

            if (column < 0 || column >= COLUMN_COUNT) {
                throw new IllegalArgumentException("AlreadyEmptyException cannot be constructed with coordinate that returns column out of range [0, 8)");
            }

            if (row < 0 || row >= ROW_COUNT) {
                throw new IllegalArgumentException("AlreadyEmptyException cannot be constructed with coordinate that returns row out of range [0, 8)");
            }

            return coordinate.toString() + " is already empty.";
        }

        /**
         * Gets the coordinate that was already empty, causing the exception to be thrown
         *
         * @return the coordinate that was already empty
         */
        public Coordinate getCoordinateThatWasAlreadyEmpty() {
            return coordinateThatWasAlreadyEmpty;
        }
    }
}
