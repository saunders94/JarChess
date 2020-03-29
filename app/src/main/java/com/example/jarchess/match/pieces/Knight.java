package com.example.jarchess.match.pieces;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.movementpatterns.MovementPattern;
import com.example.jarchess.match.pieces.movementpatterns.MovementPatternProducer;

/**
 * A knight is a chess piece that can move 2 squares in any direction and 1 in the other as a single jump.
 * <p><ul>
 * <li>It can capture on all moves, but doesn't have to.
 * <li>It starts in file b or g.
 * <li>It starts in rank 8 if black and 1 if white.
 * <li>It can be placed on the board by promotion of a pawn.
 * </ul>
 *
 * @author Joshua Zierman
 */
public class Knight extends Piece {
    public static final char QUEENWARD_STARTING_FILE = 'b';
    public static final char KINGWARD_STARTING_FILE = 'g';
    public static final int BLACK_STARTING_RANK = 8;
    public static final int WHITE_STARTING_RANK = 1;
    public static final String NAME = "Knight";
    public static final Type TYPE = Type.KNIGHT;

    public Knight(ChessColor color, char startingFile) {
        super(color, TYPE, makeStartingCoordinate(color, startingFile));

        addMovementPatterns();

    }

    public Knight(Knight pieceToCopy) {
        super(pieceToCopy);
    }

    /**
     * Creates a knight from a pawn that is being promoted.
     * <p>
     * The starting position of the pawn is used for the starting position of the new knight.
     *
     * @param pawnBeingPromoted the pawn being promoted into the bishop
     */
    public Knight(Pawn pawnBeingPromoted) {
        super(
                pawnBeingPromoted.getColor(),
                TYPE,
                pawnBeingPromoted.getStartingPosition()
        );
        addMovementPatterns();
    }

    /**
     * Makes a starting coordinate for a knight given the color and starting file of a piece.
     *
     * @param color        the color of the knight
     * @param startingFile the starting file of the knight
     * @return the starting coordinate of the knight
     */
    private static Coordinate makeStartingCoordinate(ChessColor color, char startingFile) {
        Coordinate startingCoordinate;
        int startingRank;
        startingFile = Character.toLowerCase(startingFile);

        // check to see if the provided starting file is valid for a knight
        if (startingFile != QUEENWARD_STARTING_FILE && startingFile != KINGWARD_STARTING_FILE) {
            throw new IllegalArgumentException("When constructing " + NAME + ", a file of "
                    + "'" + QUEENWARD_STARTING_FILE + "' or "
                    + "'" + KINGWARD_STARTING_FILE + "' expected but got "
                    + "'" + startingFile + "'."
            );
        }

        // set the rank based on the color provided
        startingRank = color.equals(ChessColor.BLACK) ? BLACK_STARTING_RANK : WHITE_STARTING_RANK;

        // create and return the coordinate.
        startingCoordinate = Coordinate.getByFileAndRank(startingFile, startingRank);
        return startingCoordinate;
    }

    /**
     * adds all the possible movement patterns for a knight
     */
    private void addMovementPatterns() {
        for (int[] tmp : new int[][]{
                {-2, -1},
                {-2, 1},
                {2, -1},
                {2, 1},
                {-1, -2},
                {-1, 2},
                {1, -2},
                {1, 2}
        }) {
            int x = tmp[0];
            int y = tmp[1];

            add(MovementPatternProducer.getNewJumpMovementPattern(x, y, MovementPattern.CaptureType.CAN_CAPTURE, false, getColor()));

        }
    }
}
