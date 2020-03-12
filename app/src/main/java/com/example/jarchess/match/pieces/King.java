package com.example.jarchess.match.pieces;

import android.util.Log;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.movementpatterns.CastleMovementPattern;
import com.example.jarchess.match.pieces.movementpatterns.MovementPattern;
import com.example.jarchess.match.pieces.movementpatterns.MovementPatternProducer;

import java.util.Collection;
import java.util.LinkedList;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A king is a chess piece that can move 1 square in any direction.
 * <p><ul>
 * <li>It can capture on its normal moves, but doesn't have to.
 * <li>It starts in file e.
 * <li>It starts in rank 8 if black and 1 if white.
 * <li>It can be involved in a special move called a castle where an unmoved rook in starting position
 * and the unmoved king can both be moved when there is no pieces between them. When this happens the king moves two spaces towards the rook and
 * the rook simultaneously moves to the opposite side of the king's destination. During the move, the king
 * must not start, end, or move though a square that is threatened by an enemy piece.
 * <li>If the piece is threatened by an enemy piece, it is in check.
 * <li>A player cannot make a move that would leave their king in check.
 * <li>If a player's that is placed in check cannot make a move to escape check on their turn, they lose the match by checkmate.
 * <li>If a player's that is not in check cannot make a legal move on their turn, the match ends in a draw by stalemate.
 * </ul>
 *
 * @author Joshua Zierman
 */
public class King extends Piece {//TODO write unit tests

    public static final char STARTING_FILE = 'e';
    public static final int BLACK_STARTING_RANK = 8;
    public static final int WHITE_STARTING_RANK = 1;
    public static final String NAME = "King";
    public static final Type TYPE = Type.KING;


    private final Collection<CastleMovementPattern> castleMovementPatterns = new LinkedList<CastleMovementPattern>();

    /**
     * Creates a king
     *
     * @param color the color of the king being created
     */
    public King(ChessColor color) {
        super(color, TYPE, makeStartingCoordinate(color));

        addAllMovementPatterns();

    }

    public King(King pieceToCopy) {
        super(pieceToCopy);
    }

    private void addAllMovementPatterns() {
        MovementPattern tmp;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    tmp = MovementPatternProducer.getNewSlideMovementPattern(
                            i,
                            j,
                            MovementPattern.CaptureType.CAN_CAPTURE,
                            false,
                            getColor()
                    );

                    add(tmp);

                }
            }
        }

        add(MovementPatternProducer.getAllKingCastleMovementPatterns(getColor()));
    }


    @Override
    void add(MovementPattern pattern) {
        Log.d(TAG, "add() called with: CastleMovementPattern pattern = [" + pattern + "]");
        super.add(pattern);
        if (pattern instanceof CastleMovementPattern)
            castleMovementPatterns.add((CastleMovementPattern) pattern);
    }

    /**
     * Makes the starting coordinate of a king of the given color.
     *
     * @param color the color of the king to get the starting coordinate of
     * @return the starting coordinate of a king of the given color
     */
    private static Coordinate makeStartingCoordinate(ChessColor color) {
        Coordinate startingCoordinate;
        int startingRank;

        // set the rank based on the color provided
        startingRank = color.equals(ChessColor.BLACK) ? BLACK_STARTING_RANK : WHITE_STARTING_RANK;

        // create and return the coordinate.
        startingCoordinate = Coordinate.getByFileAndRank(STARTING_FILE, startingRank);
        return startingCoordinate;
    }

    public Collection<CastleMovementPattern> getCastleMovementPatterns() {
        return castleMovementPatterns;
    }
}
