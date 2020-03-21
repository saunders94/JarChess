package com.example.jarchess.match;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.pieces.King;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.Rook;
import com.example.jarchess.match.pieces.movementpatterns.CastleMovementPattern;
import com.example.jarchess.match.pieces.movementpatterns.MovementPattern;
import com.example.jarchess.match.pieces.movementpatterns.SlidePattern;

import java.util.Collection;
import java.util.LinkedList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.jarchess.match.ChessColor.WHITE;
import static com.example.jarchess.match.pieces.Piece.Type.KING;
import static com.example.jarchess.match.pieces.movementpatterns.MovementPattern.CaptureType.CANNOT_CAPTURE;
import static com.example.jarchess.match.pieces.movementpatterns.MovementPattern.CaptureType.MUST_CAPTURE;

//TODO javadocs
public class MoveExpert {

    private static MoveExpert instance;
    private MatchHistory matchHistory;

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

    private boolean isLegalMoveIgnoringChecks(Piece pieceToMove, Coordinate origin, MovementPattern movementPattern, Chessboard chessboardToCheck) {

        if (matchHistory == null) {
            throw new IllegalStateException("matchHistory must be set for MoveExpert before calling isLegalMoveIgnoringChecks");
        }

        // if the move pattern requires the move be the first move of the piece and the piece has moved
        if (movementPattern.mustBeFirstMoveOfPiece() && pieceToMove.hasMoved()) {
            return false;
        }

        if (movementPattern instanceof CastleMovementPattern) {
            Log.d(TAG, "isLegalMoveIgnoringChecks: was instance of CastleMovmentPattern");
            return (getLegalCastleMovements(origin, movementPattern.getDestinationFrom(origin), chessboardToCheck).size() > 0);


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
            else {
                // check if en passant capture is possible at the coordinate
                if (matchHistory.getEnPassantVulnerableCoordinate() == destination && pieceToMove instanceof Pawn) {
                    return true;
                }
                return movementPattern.getCaptureType() != MUST_CAPTURE; // because the the pattern requires capturing and there is no piece to capture at destination
            }
        }

    }

    public Collection<PieceMovement> getLegalCastleMovements(@NonNull Coordinate kingOrigin, @NonNull Coordinate kingDestination, Chessboard chessboardToCheck) {
        final Collection<PieceMovement> movements = new LinkedList<PieceMovement>();

        Log.d(TAG, "getLegalCastleMovements() called with: kingOrigin = [" + kingOrigin + "], kingDestination = [" + kingDestination + "], chessboardToCheck = [" + chessboardToCheck + "]");
        final King king;
        final Rook rook;
        final ChessColor color;
        final int expectedRank;
        final char expectedStartingFile = King.STARTING_FILE;
        final Piece pieceAtOrigin = chessboardToCheck.getPieceAt(kingOrigin);

        CastleMovementPattern pattern = null;

        if (!(pieceAtOrigin instanceof King) || pieceAtOrigin.hasMoved()) {
            Log.d(TAG, "getLegalCastleMovements: not instance of king or it has moved");
            Log.d(TAG, "getLegalCastleMovements() returned: " + movements);
            return movements;
        }

        king = (King) pieceAtOrigin;
        color = king.getColor();
        switch (color) {

            case BLACK:
                expectedRank = King.BLACK_STARTING_RANK;
                break;
            case WHITE:
                expectedRank = King.WHITE_STARTING_RANK;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + color);
        }
        if (kingOrigin.getFile() != expectedStartingFile || kingOrigin.getRank() != expectedRank || kingDestination.getRank() != expectedRank) {
            Log.d(TAG, "getLegalCastleMovements: start coordinate check failed");
            Log.d(TAG, "getLegalCastleMovements() returned: " + movements);
            return movements;
        }


        for (CastleMovementPattern p : king.getCastleMovementPatterns()) {
            if (p.getDestinationFrom(kingOrigin) == kingDestination) {
                pattern = p;
            }
        }

        if (pattern == null) {
            Log.d(TAG, "getLegalCastleMovements: No pattern found");
            Log.d(TAG, "getLegalCastleMovements() returned: " + movements);
            return movements;
        }

        boolean kingMovesQueenward = kingDestination.getFile() < kingOrigin.getFile();

        char expectedEndFile = (char) (kingMovesQueenward ? expectedStartingFile - 2 : expectedStartingFile + 2);
        char rookStartingFile = kingMovesQueenward ? Rook.QUEENWARD_STARTING_FILE : Rook.KINGWARD_STARTING_FILE;
        char rookEndingFile = (char) (kingMovesQueenward ? expectedEndFile + 1 : expectedEndFile - 1);
        int rank = expectedRank;


        {
            Piece tmp = chessboardToCheck.getPieceAt(Coordinate.getByFileAndRank(rookStartingFile, rank));
            if (tmp instanceof Rook && !tmp.hasMoved()) {
                rook = (Rook) tmp;
            } else {
                Log.d(TAG, "getLegalCastleMovements: piece at " + Coordinate.getByFileAndRank(rookStartingFile, rank) + " was not a rook");
                Log.d(TAG, "getLegalCastleMovements() returned: " + movements);
                return movements;
            }
        }

        // check for any pieces between king and rook
        for (char file = expectedStartingFile; file != rookStartingFile; file += kingMovesQueenward ? -1 : 1) {
            if (file != expectedStartingFile && !chessboardToCheck.isEmptyAt(Coordinate.getByFileAndRank(file, rank))) {
                Log.d(TAG, "getLegalCastleMovements: piece was found between king and rook");
                Log.d(TAG, "getLegalCastleMovements() returned: " + movements);
                return movements;
            }
        }

        //check if king would be in check before, during, and after king move
        for (int i = 0; i <= 2; i++) {
            int offset = kingMovesQueenward ? -i : i;
            char f = (char) (expectedStartingFile + offset);
            Coordinate newPosition = Coordinate.getByFileAndRank(f, rank);
            Log.d(TAG, "getLegalCastleMovements: newPosition = " + newPosition);
            Chessboard tmpChessboard = chessboardToCheck.getCopyWithMovementsApplied(kingOrigin, newPosition);
            Log.d(TAG, "getLegalCastleMovements: tmpChessboard" + tmpChessboard);
            if (isInCheck(color, tmpChessboard)) {
                Log.d(TAG, "getLegalCastleMovements: king would be in danger before during or after move");
                Log.d(TAG, "getLegalCastleMovements() returned: " + movements);
                return movements;
            }
        }

        movements.add(new PieceMovement(kingOrigin, kingDestination));
        movements.add(new PieceMovement(Coordinate.getByFileAndRank(rookStartingFile, rank), Coordinate.getByFileAndRank(rookEndingFile, rank)));

        for (Object o : movements) {
            Log.d(TAG, "getLegalCastleMovements: added " + o + " to movements");
        }
        Log.d(TAG, "getLegalCastleMovements() returned: " + movements);
        return movements;
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

    public void setMatchHistory(MatchHistory matchHistory) {
        this.matchHistory = matchHistory;
    }
}
