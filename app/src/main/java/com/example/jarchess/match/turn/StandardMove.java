package com.example.jarchess.match.turn;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.Piece;

/**
 * a standard move is a move with only one moving piece
 */
public class StandardMove implements Move {


    private final Piece.Type pieceType;
    private final Coordinate origin;
    private final Coordinate destination;
    private final ChessColor color;

    /**
     * Creates a standard move.
     *
     * @param pieceType   the type of the piece being moved
     * @param origin      the orginin of the piece being moved
     * @param destination the destination that the piece is being moved to
     * @param color       the color of the piece being moved
     */
    public StandardMove(
            Piece.Type pieceType,
            Coordinate origin,
            Coordinate destination,
            ChessColor color) {
        this.pieceType = pieceType;
        this.origin = origin;
        this.destination = destination;
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Piece.Type getPieceType() {
        return null;//FIXME
//        return pieceType;
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
//        return origin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate getDestination() {
        return null;//FIXME
//        return destination;
    }


//    //TODO remove this if it is unneeded
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public byte[] toBytes() {
//        return new byte[0];//FIXME
//    }
}
