package com.example.jarchess.match.turn;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.King;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.Rook;

/**
 * A castle move is a specail type of move that involves moving a rook and king of the same color in a single move
 *
 * @author Joshua Zierman
 */
public final class CastleMove implements Move {

    private static final char KING_START_FILE = King.STARTING_FILE;
    private static final char KING_QUEENWARD_END_FILE = (char) (KING_START_FILE - 2);
    private static final char KING_KINGWARD_END_FILE = (char) (KING_START_FILE + 2);
    private static final char ROOK_QUEENWARD_START_FILE = Rook.QUEENWARD_STARTING_FILE;
    private static final char ROOK_QUEENWARD_END_FILE = (char) (ROOK_QUEENWARD_START_FILE + 3);
    private static final char ROOK_KINGWARD_STARTING_FILE = Rook.KINGWARD_STARTING_FILE;
    private static final char ROOK_KINGWARD_END_FILE = (char) (ROOK_KINGWARD_STARTING_FILE - 2);
    private final static byte DIRECTION_MASK = 0b01;
    private final static byte QUEENWORD_MASK = 0b00;
    private final static byte KINGWORD_MASK = 0b01;
    private final static byte COLOR_MASK = 0b10;
    private final static byte BLACK_MASK = 0b00;
    private final static byte WHITE_MASK = 0b10;
    private final static int KING_INDEX = 0;
    private final static int ROOK_INDEX = 1;
    private final Coordinate[] origins = new Coordinate[2];
    private final Coordinate[] destinations = new Coordinate[2];
    private final ChessColor color;
    private final KingMovementDirection direction;

    /**
     * Creates a castle move.
     *
     * @param color                 the color of the moving pieces
     * @param kingMovementDirection the direction the king will move
     */
    private CastleMove(ChessColor color, KingMovementDirection kingMovementDirection) {

        this.color = color;
        this.direction = kingMovementDirection;

        final char rookStartingFile;
        final char rookEndingFile;
        final char kingEndingFile;
        final int rank;

        // determine the rank by the color.
        switch (color) {
            case BLACK:
                rank = King.BLACK_STARTING_RANK;
                break;
            case WHITE:

                rank = King.WHITE_STARTING_RANK;
                break;

            default:
                throw new IllegalStateException("Unexpected color value: " + color);
        }


        // determine the starting and ending files based on the king movement direction.
        switch (kingMovementDirection) {
            case QUEENWARD:
                rookStartingFile = ROOK_QUEENWARD_START_FILE;
                rookEndingFile = ROOK_QUEENWARD_END_FILE;
                kingEndingFile = KING_QUEENWARD_END_FILE;
                break;
            case KINGWARD:
                rookStartingFile = ROOK_KINGWARD_STARTING_FILE;
                rookEndingFile = ROOK_KINGWARD_END_FILE;
                kingEndingFile = KING_KINGWARD_END_FILE;
                break;
            default:
                throw new IllegalStateException("Unexpected direction value: " + kingMovementDirection);
        }

        // set the origin and destination coordinates
        origins[KING_INDEX] = Coordinate.getByFileAndRank(KING_START_FILE, rank);
        origins[ROOK_INDEX] = Coordinate.getByFileAndRank(rookStartingFile, rank);
        destinations[KING_INDEX] = Coordinate.getByFileAndRank(kingEndingFile, rank);
        destinations[ROOK_INDEX] = Coordinate.getByFileAndRank(rookEndingFile, rank);
    }

//    //TODO remove this if it ends up being unneeded.
//    private static KingMovementDirection directionFromByte(byte[] bytes) {
//        if (bytes.length != 1)
//            throw new IllegalArgumentException("castle setAsMoved construction with byte array of incorrect length");
//        if (QUEENWORD_MASK == (DIRECTION_MASK & bytes[0])) {
//
//            return KingMovementDirection.QUEENWARD;
//
//        } else if (KINGWORD_MASK == (DIRECTION_MASK & bytes[0])) {
//
//            return KingMovementDirection.KINGWARD;
//
//        } else {
//
//            throw new IllegalArgumentException("Unexpected byte value: " + bytes[0]);
//
//        }
//    }
//
//
//    private static ChessColor colorFromByte(byte[] bytes) {
//        if (bytes.length != 1)
//            throw new IllegalArgumentException("castle setAsMoved construction with byte array of incorrect length");
//
//        byte b = bytes[0];
//
//        if (BLACK_MASK == (COLOR_MASK & b)) {
//
//            return ChessColor.BLACK;
//
//        } else if (WHITE_MASK == (COLOR_MASK & b)) {
//
//            return ChessColor.WHITE;
//
//        } else {
//
//            throw new IllegalArgumentException("Unexpected byte value: " + b);
//
//        }
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Piece.Type getPieceType() {
        return null;//FIXME
//        return Piece.Type.KING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChessColor getPieceColor() {
        return null;//FIXME
//        return color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate getOrigin() {
        return null;//FIXME
//        return origins[KING_INDEX];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate getDestination() {
        return null;//FIXME
//        return destinations[KING_INDEX];
    }

    //TODO remove this if it is unneeded
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public byte[] toBytes() {
//
//        byte b = 0b00;
//
//        switch (color) {
//
//            case BLACK:
//                b |= BLACK_MASK;
//                break;
//            case WHITE:
//                b |= WHITE_MASK;
//                break;
//        }
//
//        switch (direction) {
//
//            case QUEENWARD:
//                b |= QUEENWORD_MASK;
//                break;
//            case KINGWARD:
//                b |= KINGWORD_MASK;
//                break;
//        }
//
//        return new byte[]{b};
//    }

    /**
     * Gets the type of the other piece moved during this move.
     *
     * @return the type of the other piece moved during this move
     */
    public Piece.Type getOtherPieceType() {
        return null;//FIXME
//        return Piece.Type.ROOK;

    }

    /**
     * Gets the color of the other piece being moved during this move.
     *
     * @return the color of the other moving piece
     */
    public ChessColor getOtherPieceColor() {
        return null;//FIXME
//        return color;
    }


    /**
     * Gets the origin coordinate of the other moving piece during this move.
     *
     * @return the coordinate where the other moving piece was before this move.
     */
    public Coordinate getOtherOrigin() {
        return null;//FIXME
//        return origins[ROOK_INDEX];
    }


    /**
     * Gets the destination coordinate of the other moving piece during this move.
     *
     * @return the destination coordinate where the other piece will be after this move.
     */
    public Coordinate getOtherDestination() {
        return null;//FIXME
//        return destinations[ROOK_INDEX];
    }

    public KingMovementDirection getKingMovementDirection() {
        return null;//FIXME
//        return direction;
    }

    public enum KingMovementDirection {QUEENWARD, KINGWARD}
}
