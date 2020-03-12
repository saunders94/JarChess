package com.example.jarchess.match.pieces;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.movementpatterns.MovementPattern;
import com.example.jarchess.match.pieces.movementpatterns.MovementPatternProducer;

import static com.example.jarchess.match.Coordinate.MAX_FILE;
import static com.example.jarchess.match.Coordinate.MIN_FILE;

/**
 * A pawn is a chess piece that can move 1 squares forward if not capturing, or 1 forward and 1 to the side if capturing.
 * <p><ul>
 * <li>It can move two squares forward when not capturing instead of 1 on its first movement, but is vulnerable to capture from the square it would
 * have ended in if only moving one in addition to the square it is currently in during the next turn.
 * <li>It starts in file a, b, c, d, e, f, g, or h.
 * <li>It starts in rank 7 if black and 2 if white.
 * <li>It can be promoted if it reaches the furthest rank. When promoted, the piece is replaced with the controlling
 * player's choice of a rook, knight, bishop, or queen of the same color.
 * </ul>
 *
 * @author Joshua Zierman
 */
public class Pawn extends Piece {//TODO write unit tests
    public final static int BLACK_STARTING_RANK = 7;
    public final static int WHITE_STARTING_RANK = 2;
    public static final String NAME = "Pawn";
    public static final Type TYPE = Type.PAWN;

    /**
     * Creates a Pawn
     *
     * @param color        the color of the pawn to be created
     * @param startingFile the starting file of the pawn to be created
     */
    public Pawn(ChessColor color, char startingFile) {
        super(color, TYPE, makeStartingCoordinate(color, startingFile));

        addAllMovementPatterns();


    }

    public Pawn(Pawn pieceToCopy) {
        super(pieceToCopy);
    }

    private void addAllMovementPatterns() {
        add(MovementPatternProducer.getNewSlideMovementPattern(0, 1, MovementPattern.CaptureType.CANNOT_CAPTURE, false, getColor()));
        add(MovementPatternProducer.getNewSlideMovementPattern(0, 2, MovementPattern.CaptureType.CANNOT_CAPTURE, true, getColor()));
        add(MovementPatternProducer.getNewSlideMovementPattern(-1, 1, MovementPattern.CaptureType.MUST_CAPTURE, false, getColor()));
        add(MovementPatternProducer.getNewSlideMovementPattern(1, 1, MovementPattern.CaptureType.MUST_CAPTURE, false, getColor()));
    }

    /**
     * Makes a starting coordinate for a Pawn of a given color and starting file.
     *
     * @param color        the color of the pawn to be created
     * @param startingFile the starting file of the pawn to be created
     * @return the coordante of the starting position of the pawn with the given color and starting file
     */
    private static Coordinate makeStartingCoordinate(ChessColor color, char startingFile) {
        Coordinate startingCoordinate;
        int startingRank;
        startingFile = Character.toLowerCase(startingFile);

        // check to see if the provided starting file is valid for a pawn
        if (startingFile < MIN_FILE || startingFile > MAX_FILE) {
            throw new IllegalArgumentException("When constructing " + NAME + ", a file in range "
                    + "[" + MIN_FILE + ", "
                    + MAX_FILE + "] expected but got "
                    + startingFile + "."
            );
        }

        // set the rank based on the color provided
        startingRank = color.equals(ChessColor.BLACK) ? BLACK_STARTING_RANK : WHITE_STARTING_RANK;

        // create and return the coordinate.
        startingCoordinate = Coordinate.getByFileAndRank(startingFile, startingRank);
        return startingCoordinate;
    }

}
