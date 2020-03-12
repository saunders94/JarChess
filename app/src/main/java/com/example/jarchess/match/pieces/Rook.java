package com.example.jarchess.match.pieces;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.movementpatterns.MovementPatternProducer;

/**
 * A rook is a chess piece that can move any number of straight squares left, right, up, or down.
 * <p><ul>
 * <li>It can capture on all moves, but doesn't have to.
 * <li>It starts in file a or h.
 * <li>It starts in rank 8 if black and 1 if white.
 * <li>It can be placed on the board by promotion of a pawn.
 * <li>It can be involved in a special move called a castle where an unmoved rook in starting position
 * and the unmoved king can both be moved when there is no pieces between them. When this happens the king moves two spaces towards the rook and
 * the rook simultaneously moves to the opposite side of the king's destination. During the move, the king
 * must not start, end, or move though a square that is threatened by an enemy piece.
 * </ul>
 *
 * @author Joshua Zierman
 */
public class Rook extends Piece {//TODO write unit tests
    public static final char QUEENWARD_STARTING_FILE = 'a';
    public static final char KINGWARD_STARTING_FILE = 'h';
    public static final int BLACK_STARTING_RANK = 8;
    public static final int WHITE_STARTING_RANK = 1;
    public static final String NAME = "Rook";
    public static final Type TYPE = Type.ROOK;

    public Rook(ChessColor color, char startingFile) {
        super(color, TYPE, makeStartingCoordinate(color, startingFile));

        add(MovementPatternProducer.getAllStraightSlideMovementPatterns(getColor()));
//        add(MovementPatternProducer.getRookCastleMovementPattern(startingFile, getColor()));

    }

    public Rook(Rook pieceToCopy) {
        super(pieceToCopy);
    }

    /**
     * Creates a rook from a pawn that is being promoted.
     * <p>
     * The starting position of the pawn is used for the starting position of the new rook.
     *
     * @param pawnBeingPromoted the pawn being promoted into the rook
     */
    public Rook(Pawn pawnBeingPromoted) {
        super(
                pawnBeingPromoted.getColor(),
                TYPE,
                pawnBeingPromoted.getStartingPosition()
        );
        setAsMoved(); // this is so we don't need to do extra work when checking for castle eligibility.
        add(MovementPatternProducer.getAllStraightSlideMovementPatterns(getColor()));
    }


    /**
     * Makes a starting coordinate for a rook given the color and starting file of a piece.
     *
     * @param color        the color of the rook
     * @param startingFile the starting file of the rook
     * @return the starting coordinate of the rook
     */
    private static Coordinate makeStartingCoordinate(ChessColor color, char startingFile) {
        Coordinate startingCoordinate;
        int startingRank;
        startingFile = Character.toLowerCase(startingFile);

        // check to see if the provided starting file is valid for a rook
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
