package com.example.jarchess.match.pieces;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.movementpatterns.MovementPatternProducer;

/**
 * A queen is a chess piece that can move any number of straight squares left, right, up, or down of any number of diagonal squares.
 * <p><ul>
 * <li>It can capture on all moves, but doesn't have to.
 * <li>It starts in file d.
 * <li>It starts in rank 8 if black and 1 if white.
 * <li>It can be placed on the board by promotion of a pawn.
 * </ul>
 *
 * @author Joshua Zierman
 */
public class Queen extends Piece {


    public static final char STARTING_FILE = 'd';
    public static final int BLACK_STARTING_RANK = 8;
    public static final int WHITE_STARTING_RANK = 1;
    public static final String NAME = "Queen";
    public static final Type TYPE = Type.QUEEN;

    /**
     * Creates a queen.
     *
     * @param color the color of the queen to be created
     */
    public Queen(ChessColor color) {//TODO write unit tests
        super(color, TYPE, makeStartingCoordinate(color));

        addAllMovementPatterns();

    }

    public Queen(Queen pieceToCopy) {
        super(pieceToCopy);
    }

    /**
     * Creates a queen from a pawn that is being promoted.
     * <p>
     * The starting position of the pawn is used for the starting position of the new queen.
     *
     * @param pawnBeingPromoted the pawn being promoted into the queen
     */
    public Queen(Pawn pawnBeingPromoted) {
        super(
                pawnBeingPromoted.getColor(),
                TYPE,
                pawnBeingPromoted.getStartingPosition()
        );


        addAllMovementPatterns();

    }

    /**
     * Makes the starting coordinate of a queen of the given color.
     *
     * @param color the color of the queen to get the starting coordinate of
     * @return the starting coordinate of a queen of the given color
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

    private void addAllMovementPatterns() {
        add(MovementPatternProducer.getAllDiagonalSlideMovementPatterns(getColor()));
        add(MovementPatternProducer.getAllStraightSlideMovementPatterns(getColor()));
    }
}
