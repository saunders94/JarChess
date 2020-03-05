package com.example.jarchess.match;

import android.util.Log;

import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.movement.CastleMovementPattern;
import com.example.jarchess.match.pieces.movement.MovementPattern;
import com.example.jarchess.match.pieces.movement.SlidePattern;

import java.util.Collection;
import java.util.LinkedList;

import static com.example.jarchess.match.ChessColor.WHITE;
import static com.example.jarchess.match.pieces.movement.MovementPattern.CaptureType.MUST_CAPTURE;

//TODO javadocs
public class MoveExpert {
    private final Gameboard gameboard;

    public MoveExpert(Gameboard gameboard) {
        this.gameboard = gameboard;
    }

    Collection<Coordinate> getLegalDestinations(Coordinate origin) {
        Collection<Coordinate> collection = new LinkedList<Coordinate>();
        Piece pieceToMove = gameboard.getPieceAt(origin);

        for (MovementPattern pattern : pieceToMove.getMovementPatterns()) {
            if (isLegalMove(pieceToMove, origin, pattern)) {
                collection.add(Coordinate.getDestination(origin, pattern, pieceToMove.getColor()));
            }
        }

        return collection;
    }

    private boolean isLegalMove(Piece pieceToMove, Coordinate origin, MovementPattern pattern) {
        if (pattern.mustBeFirstMoveOfPiece() && pieceToMove.hasMoved()) {
            return false;
        }

        if (pattern instanceof CastleMovementPattern) {
            return false; // FIXME all castle moves are considered illegal until fixed
//            Piece king, rook;
//            //TODO set the values of king and rook
//            king = pieceToMove;
//
//            if (pattern.getKingwardOffset() > 0) {
//                rook = gameboard.getPieceAt(null);//FIXME
//            } else {
//                rook = gameboard.getPieceAt(null);//FIXME
//            }
//
//            if (king.hasMoved() || rook.hasMoved()) {
//                return false; //can't castle when king or rook has moved
//            }
//
//            // TODO check that the king can slide to destination without being in danger before, during, or after the slide.
//
//            // TODO check that the rook can slide to destination.


        } else {
            Coordinate destination = Coordinate.getDestination(origin, pattern, pieceToMove.getColor());

            // Check if the destination is on the board
            if (destination == null) {
                return false; // because the destination would be off the board
            }

            // Check if moving from origin to destination would put the king of the moving piece's color in check
            // TODO check if move would leave the mover's king in check


            //FIXME

            //Check if slide path is clear in the case that the pattern is a slide pattern.
            if (pattern.isSlide()) {

                final int x = ((SlidePattern) pattern).getKingwardSlideOffset();
                final int y = (pieceToMove.getColor().equals(WHITE) ? -1 : 1) * ((SlidePattern) pattern).getForwardSlideOffset();

                Coordinate tmp = origin;
                Coordinate next;
                next = Coordinate.getByColumnAndRow(tmp.getColumn() + x, tmp.getRow() + y);

                while (next != destination) {
                    tmp = next;
                    next = Coordinate.getByColumnAndRow(tmp.getColumn() + x, tmp.getRow() + y);

                    if (gameboard.getPieceAt(tmp) != null) {
                        return false; //  because the slide path is blocked.
                    }
                }
            }


            // check if the destination is occupied by a piece of the same color
            Piece pieceAtDestination = gameboard.getPieceAt(destination);
            if (pieceAtDestination != null && pieceAtDestination.getColor().equals(pieceToMove.getColor())) {
                return false; // because the destination would land on a piece of the same color
            }

            // check if the destination is a piece of the opposite color of the moving piece
            else if (pieceAtDestination != null){

                if(pattern.getCaptureType() == MovementPattern.CaptureType.CANNOT_CAPTURE){
                    return  false; // because the destination has a piece of the opposite color
                }
            }

            // check if there is no piece at destination and pattern must capture.
            else if (pattern.getCaptureType() == MUST_CAPTURE){
                return false; // because the the pattern requires capturing and there is no piece to capture at destination
            }
        }

        return true;
    }

    private void log(Object msg) {
        Log.d("MoveExpert", msg.toString());
    }

    private void log(String msg) {
        Log.d("MoveExpert", msg);
    }
}
