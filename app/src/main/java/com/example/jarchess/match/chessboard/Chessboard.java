package com.example.jarchess.match.chessboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
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
 * Chessboard is a container that holds Pieces
 * <p>
 * No game rules logic is handled by Chessboard
 *
 * @author Joshua Zierman
 */
public class Chessboard {
    private static final int ROW_COUNT = 8;
    private static final int COLUMN_COUNT = 8;
    private final Piece[][] pieces;
    private boolean isCopy;

    /**
     * Private constructor used to create the instance of chessboard
     */
    public Chessboard() {
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

    private Chessboard(Chessboard chessboard) {
        pieces = new Piece[COLUMN_COUNT][ROW_COUNT];
        for (Coordinate coordinate : Coordinate.values()) {
            int column = coordinate.getColumn();
            int row = coordinate.getRow();

            Piece original = chessboard.getPieceAt(coordinate);

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

    /**
     * Adds a piece to the chessboard at starting position
     *
     * @param piece the piece to add.
     */
    private void add(Piece piece) {
        add(piece, piece.getStartingPosition());
    }

    /**
     * Adds a piece to a specified coordinate on the chessboard
     *
     * @param piece      The piece to be added to the chessboard
     * @param coordinate on the chessboard that the piece will be added to
     */
    public void add(@NonNull Piece piece, @NonNull Coordinate coordinate) {

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

    public Chessboard getCopy() {
        return new Chessboard(this);
    }


    public Chessboard getCopyWithMovementsApplied(@NonNull Coordinate origin, @NonNull Coordinate destination) {
        return getCopyWithMovementsApplied(origin, destination, null);
    }

    public Chessboard getCopyWithMovementsApplied(@NonNull Coordinate origin, @NonNull Coordinate destination, @Nullable Coordinate captureCoordinate) {
        Chessboard copy = new Chessboard(this);
        if (captureCoordinate != null) {
            copy.remove(captureCoordinate);
        }
        copy.move(origin, destination);
        return copy;
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
     * Checks to see if the board is empty at a specified coordinate
     *
     * @param coordinate the coordinate to check
     * @return true if the chessboard is empty at the specified coordinate
     */
    public boolean isEmptyAt(Coordinate coordinate) {   //TODO need tests
        return getPieceAt(coordinate) == null;
    }

    /**
     * Moves a piece from one coordinate to another on the chessboard
     *
     * @param originCoordinate      the coordinate of the piece to be moved
     * @param destinationCoordinate the coordinate of where the piece will be after the move
     */
    public void move(Coordinate originCoordinate, Coordinate destinationCoordinate) {

        if (originCoordinate != destinationCoordinate) {
            Piece piece = getPieceAt(originCoordinate);

            add(piece, destinationCoordinate);
            remove(originCoordinate);
            piece.setAsMoved();
        }
    }

    public Piece remove(Coordinate coordinate) {

        Piece pieceRemoved = pieces[coordinate.getColumn()][coordinate.getRow()];
        pieces[coordinate.getColumn()][coordinate.getRow()] = null;

        return pieceRemoved;
    }

    /**
     * Resets the chessboard to stating positions.
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

    @NonNull
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        s.append("\n");
        for (int r : Coordinate.ROWS) {
            for (int c : Coordinate.COLUMNS) {
                Coordinate coordinate = Coordinate.getByColumnAndRow(c, r);
                Piece p = getPieceAt(coordinate);

                if (p == null) {
                    s.append("[ ]");
                } else {
                    char ch;

                    switch (p.getType()) {

                        case PAWN:
                            ch = 'p';
                            break;
                        case ROOK:
                            ch = 'r';
                            break;
                        case KNIGHT:
                            ch = 'n';
                            break;
                        case BISHOP:
                            ch = 'b';
                            break;
                        case QUEEN:
                            ch = 'q';
                            break;
                        case KING:
                            ch = 'k';
                            break;

                        default:
                            throw new IllegalStateException("Unexpected value: " + p.getType());
                    }

                    if (p.getColor() == BLACK) {
                        ch = Character.toUpperCase(ch);
                    }

                    s.append("[" + ch + "]");
                }
            }
            s.append("\n");
        }

        return s.toString();
    }
}
