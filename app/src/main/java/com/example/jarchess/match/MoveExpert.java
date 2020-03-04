package com.example.jarchess.match;

import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.movement.CastleMovementPattern;
import com.example.jarchess.match.pieces.movement.MovementPattern;

import java.util.Collection;
import java.util.LinkedList;

public class MoveExpert {
    private final Gameboard gameboard;
    private final ChessColor colorOfMover;

    public MoveExpert(Gameboard gameboard, ChessColor colorOfMover) {
        this.gameboard = gameboard;
        this.colorOfMover = colorOfMover;
    }

    Collection<Coordinate> getLegalDestinations(Coordinate origin) {
        Collection<Coordinate> collection = new LinkedList<Coordinate>();
        Piece pieceToMove = gameboard.getPieceAt(origin);
        if (pieceToMove.getColor() != colorOfMover) {
            return collection; // return empty collection because there are no legal moves.
        }

        for (MovementPattern pattern : pieceToMove.getMovementPatterns()) {
            if (isLegalMove(pieceToMove, origin, pattern)) {
                collection.add(Coordinate.getDestination(origin, pattern, pieceToMove.getColor()));
            }
        }

        return null;//FIXME
    }

    private boolean isLegalMove(Piece pieceToMove, Coordinate origin, MovementPattern pattern) {
        if (pattern.mustBeFirstMoveOfPiece() && pieceToMove.hasMoved()) {
            return false;
        }

        if (pattern instanceof CastleMovementPattern) {
            Piece king, rook;
            //TODO set the values of king and rook
            king = pieceToMove;

            if (pattern.getKingwardOffset() > 0) {
                rook = gameboard.getPieceAt(null);//FIXME
            } else {
                rook = gameboard.getPieceAt(null);//FIXME
            }

            if (king.hasMoved() || rook.hasMoved()) {
                return false; //can't castle when king or rook has moved
            }

            // TODO check that the king can slide to destination without being in danger before, during, or after the slide.

            // TODO check that the rook can slide to destination.
        } else {
            // TODO check if setAsMoved would place piece out of bounds

            // TODO check if setAsMoved would leave the mover's king in check

            // TODO if pattern is slide, make sure nothing prevents the slide
        }

        return false;//FIXME
    }
}
