package com.example.jarchess.match;

import android.util.Log;

import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.movementpatterns.CastleMovementPattern;
import com.example.jarchess.match.pieces.movementpatterns.MovementPattern;
import com.example.jarchess.match.pieces.movementpatterns.SlidePattern;

import java.util.Collection;
import java.util.LinkedList;

import static com.example.jarchess.match.ChessColor.WHITE;
import static com.example.jarchess.match.pieces.Piece.Type.KING;
import static com.example.jarchess.match.pieces.movementpatterns.MovementPattern.CaptureType.CANNOT_CAPTURE;
import static com.example.jarchess.match.pieces.movementpatterns.MovementPattern.CaptureType.MUST_CAPTURE;

//TODO javadocs
public class MoveExpert {

    private static MoveExpert instance;

    /**
     * Creates an instance of <code>MoveExpert</code> to construct a singleton instance
     */
    private MoveExpert() {
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static MoveExpert getInstance() {
        if (instance == null) {
            instance = new MoveExpert();
        }

        return instance;
    }

    public Collection<Coordinate> getLegalDestinations(Coordinate origin, Chessboard chessboardToCheck) {
        Collection<Coordinate> collection = new LinkedList<Coordinate>();
        Piece pieceToMove = chessboardToCheck.getPieceAt(origin);

        for (MovementPattern pattern : pieceToMove.getMovementPatterns()) {
            if (isLegalMove(pieceToMove, origin, pattern, chessboardToCheck)) {
                collection.add(pattern.getDestinationFrom(origin));
            }
        }

        return collection;
    }

    public boolean isLegalMove(Piece pieceToMove, Coordinate origin, MovementPattern movementPattern, Chessboard chessboardToCheck) {
        Coordinate destination = movementPattern.getDestinationFrom(origin);
        if (destination == null) {
            return false; // because the destination of that move is out of bounds
        }
        boolean isLegalIgnoringCheck = isLegalMoveIgnoringChecks(pieceToMove, origin, movementPattern, chessboardToCheck);

        if (isLegalIgnoringCheck) {
            boolean leavesKingInCheck = isInCheck(pieceToMove.getColor(), chessboardToCheck.getCopyWithMovementsApplied(origin, destination));

            return !leavesKingInCheck; // you can't make a move that would leave your king in check
        } else {
            return false;
        }
    }

    public boolean isInCheck(ChessColor colorOfPlayerThatMightBeInCheck, Chessboard chessboardToCheck) {

        final ChessColor threatColor = ChessColor.getOther(colorOfPlayerThatMightBeInCheck);
        Piece tmpPiece;
        Coordinate kingCoordinateToCheck = null;
        for (Coordinate c : Coordinate.values()) {
            tmpPiece = chessboardToCheck.getPieceAt(c);
            if (tmpPiece != null && colorOfPlayerThatMightBeInCheck == tmpPiece.getColor() && tmpPiece.getType() == KING) {
                kingCoordinateToCheck = c;
                break;
            }

        }
        if (kingCoordinateToCheck == null) {

        }

        for (Coordinate c : Coordinate.values()) {
            tmpPiece = chessboardToCheck.getPieceAt(c);
            if (tmpPiece != null && threatColor == tmpPiece.getColor()) {

                for (MovementPattern pattern : tmpPiece.getMovementPatterns()) {
                    if (pattern.getDestinationFrom(c) == kingCoordinateToCheck && isLegalMoveIgnoringChecks(tmpPiece, c, pattern, chessboardToCheck)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveIgnoringChecks(Piece pieceToMove, Coordinate origin, MovementPattern movementPattern, Chessboard chessboardToCheck) {

        // if the move pattern requires the move be the first move of the piece and the piece has moved
        if (movementPattern.mustBeFirstMoveOfPiece() && pieceToMove.hasMoved()) {
            return false;
        }

        if (movementPattern instanceof CastleMovementPattern) {
            return false; // FIXME all castle moves are considered illegal until fixed
//            Piece king, rook;
//            //TODO set the values of king and rook
//            king = pieceToMove;
//
//            if (pattern.getKingwardOffset() > 0) {
//                rook = chessboardToCheck.getPieceAt(null);//FIXME
//            } else {
//                rook = chessboardToCheck.getPieceAt(null);//FIXME
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
            Coordinate destination = movementPattern.getDestinationFrom(origin);

            // Check if the destination is on the board
            if (destination == null) {
                return false; // because the destination would be off the board
            }


            //Check if slide path is clear in the case that the pattern is a slide pattern.
            if (movementPattern.isSlide()) {

                final int x = ((SlidePattern) movementPattern).getKingwardSlideOffset();
                final int y = (pieceToMove.getColor().equals(WHITE) ? -1 : 1) * ((SlidePattern) movementPattern).getForwardSlideOffset();

                Coordinate tmp = origin;
                Coordinate next;
                next = Coordinate.getByColumnAndRow(tmp.getColumn() + x, tmp.getRow() + y);

                while (next != destination) {
                    tmp = next;
                    next = Coordinate.getByColumnAndRow(tmp.getColumn() + x, tmp.getRow() + y);

                    if (chessboardToCheck.getPieceAt(tmp) != null) {
                        return false; //  because the slide path is blocked.
                    }
                }
            }


            // check if the destination is occupied by a piece of the same color
            Piece pieceAtDestination = chessboardToCheck.getPieceAt(destination);
            if (pieceAtDestination != null && pieceAtDestination.getColor().equals(pieceToMove.getColor())) {
                return false; // because the destination would land on a piece of the same color
            }

            // check if the destination is a piece of the opposite color of the moving piece
            else if (pieceAtDestination != null) {

                return movementPattern.getCaptureType() != CANNOT_CAPTURE; // because the destination has a piece of the opposite color
            }

            // check if there is no piece at destination and pattern must capture.
            else
                return movementPattern.getCaptureType() != MUST_CAPTURE; // because the the pattern requires capturing and there is no piece to capture at destination
        }

    }



    private void log(Object msg) {
        Log.d("MoveExpert", msg.toString());
    }

    private void log(String msg) {
        Log.d("MoveExpert", msg);
    }

    public boolean hasMoves(ChessColor nextTurnColor, Chessboard chessboardToCheck) {
        for (Coordinate originCoordinate : Coordinate.values()) {
            Piece piece = chessboardToCheck.getPieceAt(originCoordinate);
            if (piece != null && piece.getColor() == nextTurnColor)
                if (!getLegalDestinations(originCoordinate, chessboardToCheck).isEmpty()) {
                    return true;
                }
        }

        return false;
    }
}
