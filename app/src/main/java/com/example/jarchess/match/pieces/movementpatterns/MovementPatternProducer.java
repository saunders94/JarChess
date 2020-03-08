package com.example.jarchess.match.pieces.movementpatterns;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.pieces.Rook;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A movement pattern producer can generate new movement patterns and collections of movement patterns.
 * <p>
 * The goal of this class is just to make life a little easier when generating movement patterns for the chess pieces.
 *
 * @author Joshua Zierman
 */
public class MovementPatternProducer {//TODO write unit tests

    private static final char QUEENWARD_ROOK_FILE = Rook.QUEENWARD_STARTING_FILE;
    private static final char KINGWARD_ROOK_FILE = Rook.KINGWARD_STARTING_FILE;
    private static final int QUEENWARD_ROOK_CASTLE_OFFSET = 3;
    private static final int KINGWARD_ROOK_CASTLE_OFFSET = -2;
    private static final int KING_CASTLE_OFFSET_MAGNITUDE = 2;
    private static final int MAX_OFFSET = 7;

    /**
     * Creates a slide movement pattern.
     *
     * @param kingwardOffset         the number of squares to the right (when viewing from white's starting
     *                               side) that the movement pattern will shift the piece.
     * @param forwardOffset          the number of squares forward (away from its color's starting rows) that
     *                               the movement pattern will shift the piece
     * @param captureType            the capturing type of the movement pattern
     * @param mustBeFirstMoveOfPiece if the movement pattern only applies to the first movement of the piece
     * @param color                 the color of the piece this pattern if for
     * @return the created slide movement pattern
     */
    public static MovementPattern getNewSlideMovementPattern(int kingwardOffset, int forwardOffset, MovementPattern.CaptureType captureType, boolean mustBeFirstMoveOfPiece, ChessColor color) {
        return new SlidePattern(kingwardOffset, forwardOffset, captureType, mustBeFirstMoveOfPiece, color);
    }

    /**
     * Creates a jump movement pattern.
     *
     * @param kingwardOffset         the number of squares to the right (when viewing from white's starting
     *                               side) that the movement pattern will shift the piece.
     * @param forwardOffset          the number of squares forward (away from its color's starting rows) that
     *                               the movement pattern will shift the piece
     * @param captureType            the capturing type of the movement pattern
     * @param mustBeFirstMoveOfPiece if the movement pattern only applies to the first movement of the piece
     * @return the created jump movement pattern
     */
    public static MovementPattern getNewJumpMovementPattern(int kingwardOffset, int forwardOffset, MovementPattern.CaptureType captureType, boolean mustBeFirstMoveOfPiece, ChessColor color) {
        return new JumpPattern(kingwardOffset, forwardOffset, captureType, mustBeFirstMoveOfPiece, color);
    }

    /**
     * gets all straight slide movement patterns like that of a rook.
     *
     * @return a collection of all straight slide movement patterns
     */
    public static Collection<MovementPattern> getAllStraightSlideMovementPatterns(ChessColor color) {
        Collection<MovementPattern> collection = new LinkedList<MovementPattern>();
        MovementPattern tmp;
        for (int i = 1; i <= MovementPatternProducer.MAX_OFFSET; i++) {
            tmp = new SlidePattern(0, i, MovementPattern.CaptureType.CAN_CAPTURE, false, color);
            collection.add(tmp);
            tmp = new SlidePattern(i, 0, MovementPattern.CaptureType.CAN_CAPTURE, false, color);
            collection.add(tmp);
            tmp = new SlidePattern(0, -i, MovementPattern.CaptureType.CAN_CAPTURE, false, color);
            collection.add(tmp);
            tmp = new SlidePattern(-i, 0, MovementPattern.CaptureType.CAN_CAPTURE, false, color);
            collection.add(tmp);
        }

        return collection;
    }

    /**
     * gets all diagonal slide movement patterns like that of a bishop.
     *
     * @return a collection of all diagonal slide movement patterns
     */
    public static Collection<MovementPattern> getAllDiagonalSlideMovementPatterns(ChessColor color) {
        Collection<MovementPattern> collection = new LinkedList<MovementPattern>();
        MovementPattern tmp;
        for (int i = 1; i <= MAX_OFFSET; i++) {
            tmp = new SlidePattern(i, i, MovementPattern.CaptureType.CAN_CAPTURE, false, color);
            collection.add(tmp);
            tmp = new SlidePattern(i, -i, MovementPattern.CaptureType.CAN_CAPTURE, false, color);
            collection.add(tmp);
            tmp = new SlidePattern(-i, i, MovementPattern.CaptureType.CAN_CAPTURE, false, color);
            collection.add(tmp);
            tmp = new SlidePattern(-i, -i, MovementPattern.CaptureType.CAN_CAPTURE, false, color);
            collection.add(tmp);
        }

        return collection;
    }

    /**
     * gets all the king's castle movement patters.
     * @param color                 the color of the piece this pattern if for
     *
     * @return a collection of all the king's castle movement patters
     */
    public static Collection<MovementPattern> getAllKingCastleMovementPatterns(ChessColor color) {
        Collection<MovementPattern> collection;
        collection = new LinkedList<MovementPattern>();
        CastleMovementPattern tmp;

        tmp = new CastleMovementPattern(-KING_CASTLE_OFFSET_MAGNITUDE, color);
        collection.add(tmp);

        tmp = new CastleMovementPattern(KING_CASTLE_OFFSET_MAGNITUDE, color);
        collection.add(tmp);

        return collection;
    }

    /**
     * Gets all the castle movement patterns for a rook at a specified file.
     *
     * @param file the file of the rook
     * @param color                 the color of the piece this pattern if for
     * @return a collection of all the castle movement patterns for a rook at the specified file
     */
    public static MovementPattern getRookCastleMovementPattern(char file, ChessColor color) {
        int offset;
        file = Character.toLowerCase(file);

        switch (file) {
            case QUEENWARD_ROOK_FILE:
                offset = QUEENWARD_ROOK_CASTLE_OFFSET;
                break;
            case KINGWARD_ROOK_FILE:
                offset = KINGWARD_ROOK_CASTLE_OFFSET;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + file);
        }

        return new CastleMovementPattern(offset, color);
    }
}
