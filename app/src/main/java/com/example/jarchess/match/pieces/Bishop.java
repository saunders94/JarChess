package com.example.jarchess.match.pieces;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.movementpatterns.MovementPatternProducer;

/**
 * A bishop is a chess piece that can move any number of diagonal squares.
 * <p><ul>
 * <li>It can capture on all moves, but doesn't have to.
 * <li>It starts in file c or f.
 * <li>It starts in rank 8 if black and 1 if white.
 * <li>It can be placed on the board by promotion of a pawn.
 * </ul>
 *
 * @author Joshua Zierman
 */
public class Bishop extends Piece {//TODO write unit tests


    public static final char QUEENWARD_STARTING_FILE = 'c';
    public static final char KINGWARD_STARTING_FILE = 'f';
    public static final int BLACK_STARTING_RANK = 8;
    public static final int WHITE_STARTING_RANK = 1;
    public static final String NAME = "Bishop";
    public static final Type TYPE = Type.BISHOP;

    /**
     * Creates a bishop
     *
     * @param color        the color of the bishop being created
     * @param startingFile the starting file of the bishop being created
     */
    public Bishop(ChessColor color, char startingFile) {
        super(color, TYPE, makeStartingCoordinate(color, startingFile));

        add(MovementPatternProducer.getAllDiagonalSlideMovementPatterns());

    }

    /**
     * Creates a bishop from a pawn that is being promoted.
     * <p>
     * The starting position of the pawn is used for the starting position of the new bishop.
     *
     * @param pawnBeingPromoted the pawn being promoted into the bishop
     */
    public Bishop(Pawn pawnBeingPromoted) {
        super(
                pawnBeingPromoted.getColor(),
                TYPE,
                pawnBeingPromoted.getStartingPosition()
        );
        add(MovementPatternProducer.getAllDiagonalSlideMovementPatterns());
    }

    /**
     * Makes a starting coordinate for a bishop given the color and starting file of a piece.
     *
     * @param color        the color of the bishop
     * @param startingFile the starting file of the bishop
     * @return the starting coordinate of the bishop
     */
    private static Coordinate makeStartingCoordinate(ChessColor color, char startingFile) {
        Coordinate startingCoordinate;
        int startingRank;


        startingFile = Character.toLowerCase(startingFile);

        // check to see if the provided starting file is valid for a bishop.
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
}
