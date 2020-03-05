package com.example.jarchess.match.turn;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.Piece;

/**
 * A move is a representation of the set of piece movements the are part of a single turn in chess
 *
 * @author Joshua Zierman
 */
public interface Move {

//    /**
//     * Gets the type of the piece moved during this move.
//     *
//     * @return the type of the piece moved during this move
//     */
//    Piece.Type getPieceType();
//
//    /**
//     * Gets the color of the piece being moved during this move.
//     *
//     * @return the color of the moving piece
//     */
//    ChessColor getPieceColor();

    /**
     * Gets the origin coordinate of the moving piece during this move.
     *
     * @return the coordinate where the moving piece was before this move.
     */
    Coordinate getOrigin();

    /**
     * Gets the destination coordinate of the moving piece during this move.
     *
     * @return the destination coordinate where the piece will be after this move.
     */
    Coordinate getDestination();
}
