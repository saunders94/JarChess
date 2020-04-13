package com.example.jarchess.match;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.chessboard.Chessboard;
import com.example.jarchess.match.chessboard.ChessboardReader;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.pieces.King;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.pieces.Rook;
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

    private static final String TAG = "MoveExpert";
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

    public Collection<Move> getAllLegalMoves(ChessColor color, MatchHistory matchHistory) {
        ChessboardReader chessboard = matchHistory.getLastChessboardReader();
        Collection<Move> moves = new LinkedList<>();
        for (Coordinate origin : Coordinate.values()) {
            Piece movingPiece = chessboard.getPieceAt(origin);
            if (movingPiece != null && movingPiece.getColor() == color) {
                for (Coordinate destination : getLegalDestinations(origin, matchHistory)) {
                    Collection<PieceMovement> castleMovements = getLegalCastleMovements(origin, destination, matchHistory);
                    if (castleMovements != null && !castleMovements.isEmpty()) {
                        moves.add(new Move(castleMovements));
                    } else {
                        moves.add(new Move(origin, destination));
                    }
                }
            }
        }

        return moves;
    }

    public Collection<PieceMovement> getAllPossibleMovements(ChessColor color, MatchHistory matchHistory) {
        ChessboardReader chessboard = matchHistory.getLastChessboardReader();
        Collection<PieceMovement> movements = new LinkedList<>();
        for (Coordinate origin : Coordinate.values()) {
            Piece movingPiece = chessboard.getPieceAt(origin);
            if (movingPiece != null && movingPiece.getColor() == color) {
                for (Coordinate destination : getLegalDestinations(origin, matchHistory)) {
                    movements.add(new PieceMovement(origin, destination));
                }
            }
        }

        return movements;
    }

    public Collection<Move> getAllLegalMoves(ChessColor color, MatchHistory matchHistory) {
        ChessboardReader chessboard = matchHistory.getLastChessboardReader();
        Collection<Move> moves = new LinkedList<>();
        for (Coordinate origin : Coordinate.values()) {
            Piece movingPiece = chessboard.getPieceAt(origin);
            if (movingPiece != null && movingPiece.getColor() == color) {
                for (Coordinate destination : getLegalDestinations(origin, matchHistory)) {
                    Collection<PieceMovement> castleMovements = getLegalCastleMovements(origin, destination, matchHistory);
                    if (castleMovements != null && !castleMovements.isEmpty()) {
                        moves.add(new Move(castleMovements));
                    } else {
                        moves.add(new Move(origin, destination));
                    }
                }
            }
        }

        return moves;
    }

    public Collection<PieceMovement> getLegalCastleMovements(@NonNull Coordinate kingOrigin, @NonNull Coordinate kingDestination, MatchHistory matchHistory) {
        final Collection<PieceMovement> movements = new LinkedList<PieceMovement>();
        final ChessboardReader chessboardToCheck = matchHistory.getLastChessboardReader();
        final King king;
        final Rook rook;
        final ChessColor color;
        final int expectedRank;
        final char expectedStartingFile = King.STARTING_FILE;
        final Piece pieceAtOrigin = chessboardToCheck.getPieceAt(kingOrigin);

        CastleMovementPattern pattern = null;

        if (!(pieceAtOrigin instanceof King) || pieceAtOrigin.hasMoved()) {
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
            return movements;
        }


        for (CastleMovementPattern p : king.getCastleMovementPatterns()) {
            if (p.getDestinationFrom(kingOrigin) == kingDestination) {
                pattern = p;
            }
        }

        if (pattern == null) {
            return movements;
        }

        boolean kingMovesQueenward = kingDestination.getFile() < kingOrigin.getFile();

        char expectedEndFile = (char) (kingMovesQueenward ? expectedStartingFile - 2 : expectedStartingFile + 2);
        char rookStartingFile = kingMovesQueenward ? Rook.QUEENWARD_STARTING_FILE : Rook.KINGWARD_STARTING_FILE;
        char rookEndingFile = (char) (kingMovesQueenward ? expectedEndFile + 1 : expectedEndFile - 1);
        int rank = expectedRank;

        //set and check rook
        {
            Piece tmp = chessboardToCheck.getPieceAt(Coordinate.getByFileAndRank(rookStartingFile, rank));
            if (tmp instanceof Rook && !tmp.hasMoved()) {
                rook = (Rook) tmp;
            } else {
                return movements;
            }
        }

        // check for any pieces between king and rook
        for (char file = expectedStartingFile; file != rookStartingFile; file += kingMovesQueenward ? -1 : 1) {
            if (file != expectedStartingFile && !chessboardToCheck.isEmptyAt(Coordinate.getByFileAndRank(file, rank))) {

                return movements;
            }
        }

        //check if king would be in check before, during, and after king move
        for (int i = 0; i <= 2; i++) {
            int offset = kingMovesQueenward ? -i : i;
            char f = (char) (expectedStartingFile + offset);
            Coordinate newPosition = Coordinate.getByFileAndRank(f, rank);

            Chessboard tmpChessboard = chessboardToCheck.getCopyWithMovementsApplied(kingOrigin, newPosition);

            if (isInCheck(color, matchHistory)) {
                return movements;
            }
        }

        movements.add(new PieceMovement(kingOrigin, kingDestination));
        movements.add(new PieceMovement(Coordinate.getByFileAndRank(rookStartingFile, rank), Coordinate.getByFileAndRank(rookEndingFile, rank)));
        return movements;
    }

    public Collection<Coordinate> getLegalDestinations(Coordinate origin, MatchHistory matchHistory) {
        ChessboardReader chessboardToCheck = matchHistory.getLastChessboardReader();
        Collection<Coordinate> collection = new LinkedList<Coordinate>();
        Piece pieceToMove = chessboardToCheck.getPieceAt(origin);

        for (MovementPattern pattern : pieceToMove.getMovementPatterns()) {
            if (isLegalMovement(origin, pattern, matchHistory)) {
                collection.add(pattern.getDestinationFrom(origin));
            }
        }

        return collection;
    }

    public boolean hasMoves(ChessColor movingColor, MatchHistory matchHistory) {
        ChessboardReader chessboardToCheck = matchHistory.getLastChessboardReader();
        for (Coordinate originCoordinate : Coordinate.values()) {
            Piece piece = chessboardToCheck.getPieceAt(originCoordinate);
            if (piece != null && piece.getColor() == movingColor)
                if (!getLegalDestinations(originCoordinate, matchHistory).isEmpty()) {
                    return true;
                }
        }

        return false;
    }

    public boolean isInCheck(ChessColor colorOfPlayerThatMightBeInCheck, MatchHistory matchHistory) {
        ChessboardReader chessboardToCheck = matchHistory.getLastChessboardReader();

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
                    if (pattern.getDestinationFrom(c) == kingCoordinateToCheck && isLegalMovementIgnoringChecks(tmpPiece, c, pattern, matchHistory)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isLegalMove(Move move, MatchHistory matchHistory) {
        if (move.size() == 2) {
            return moveIsLegalCastle(move, matchHistory);
        } else if (move.size() == 1) {
            for (PieceMovement movement : move) {
                ChessboardReader chessboardToCheck = matchHistory.getLastChessboardReader();
                if (chessboardToCheck == null) {
                    throw new IllegalStateException("Match history should never return null chessboard");
                }
                Coordinate origin = movement.getOrigin();
                Coordinate destination = movement.getDestination();
                if (destination == null || origin == null) {
                    return false; // because the destination or origin of that move is out of bounds
                }
                Piece pieceToMove = chessboardToCheck.getPieceAt(origin);
                if (pieceToMove == null) {
                    return false; // because the origin must contain a piece to move
                } else if (pieceToMove.getColor() != matchHistory.getNextTurnColor()) {
                    return false; // because the piece that is trying to be moved is the same color as the last turn
                }
                boolean isLegalIgnoringCheck = isLegalMoveIgnoringChecks(move, matchHistory);

                if (isLegalIgnoringCheck) {
                    boolean leavesKingInCheck = isInCheck(pieceToMove.getColor(), matchHistory.getCopyWithMoveApplied(move, PromotionChoice.PROMOTE_TO_QUEEN));

                    return !leavesKingInCheck; // you can't make a move that would leave your king in check
                } else {
                    return false; // not legal ignoring checks
                }
            }
            // this should be unreachable code, but return or throw is required because compiler doesn't
            // see that it is unreachable.
            String msg = "Reached a line of code that should be unreachable";
            Log.wtf(TAG, "isLegalMove: " + msg);
            throw new RuntimeException(msg);
        } else {
            return false;
        }
    }

    private boolean isLegalMoveIgnoringChecks(@NonNull Move move, @NonNull MatchHistory matchHistory) {

        ChessboardReader chessboardToCheck = matchHistory.getLastChessboardReader();
        if (move.size() == 2) {

            return moveIsLegalCastle(move, matchHistory);

        } else if (move.size() == 1) {
            for (PieceMovement movement : move) {
                final Coordinate origin = movement.getOrigin();
                if (origin == null) {
                    return false;
                }

                final Coordinate destination = movement.getDestination();
                if (destination == null) {
                    return false;
                }

                final Piece piece = chessboardToCheck.getPieceAt(origin);
                if (piece == null) {
                    return false;
                }

                MovementPattern matchedPattern = null;
                for (MovementPattern checkedPattern : piece.getMovementPatterns()) {
                    if (destination == checkedPattern.getDestinationFrom(origin)) {
                        matchedPattern = checkedPattern;
                        break;
                    }
                }
                if (matchedPattern == null) {
                    return false;
                }

                return isLegalMovementIgnoringChecks(piece, origin, matchedPattern, matchHistory);
            }
            // this should be unreachable code, but return or throw is required because compiler doesn't
            // see that it is unreachable.
            String msg = "Reached a line of code that should be unreachable";
            Log.wtf(TAG, "isLegalMoveIgnoringChecks: " + msg);
            throw new RuntimeException(msg);
        } else {
            return false;
        }
    }

    private boolean isLegalMovement(Coordinate origin, MovementPattern pattern, MatchHistory matchHistory) {
        return isLegalMove(new Move(origin, pattern.getDestinationFrom(origin)), matchHistory);
    }

    private boolean isLegalMovementIgnoringChecks(@NonNull Piece pieceToMove, @NonNull Coordinate origin, @NonNull MovementPattern movementPattern, @NonNull MatchHistory matchHistory) {

        ChessboardReader chessboardToCheck = matchHistory.getLastChessboardReader();
        if (chessboardToCheck == null) {
            throw new IllegalStateException("matchHistory's lastChessboardReader method returned null");
        }

        // if the move pattern requires the move be the first move of the piece and the piece has moved
        if (movementPattern.mustBeFirstMoveOfPiece() && pieceToMove.hasMoved()) {
            return false;
        }

        if (movementPattern instanceof CastleMovementPattern) {
            return (getLegalCastleMovements(origin, movementPattern.getDestinationFrom(origin), matchHistory).size() > 0);


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

    private boolean moveIsLegalCastle(Move move, MatchHistory matchHistory) {
        ChessboardReader chessboardToCheck = matchHistory.getLastChessboardReader();
        Coordinate kOrigin = null;
        Coordinate kDestination = null;
        for (PieceMovement movement : move) {
            if (chessboardToCheck.getPieceAt(movement.getOrigin()) instanceof King) {
                kOrigin = movement.getOrigin();
                kDestination = movement.getDestination();
                break;
            }
        }
        Collection<PieceMovement> castleMovements = null;
        if (kOrigin != null && kDestination != null) {
            castleMovements = getLegalCastleMovements(kOrigin, kDestination, matchHistory);
            return castleMovements.containsAll(move);
        }

        return false;
    }

    public boolean moveRequiresPromotion(Move move, MatchHistory matchHistory) {
        ChessboardReader reader = matchHistory.getLastChessboardReader();

        for (PieceMovement movement : move) {
            Coordinate origin = movement.getOrigin();
            Coordinate destination = movement.getDestination();
            Piece p = reader.getPieceAt(origin);
            if (p instanceof Pawn && (destination.getRank() == 1 || destination.getRank() == 8)) {
                return true;
            }
        }
        return false;
    }
}
