package com.example.jarchess.match.pieces.movement;

import static java.lang.Math.abs;

/**
 * A slide pattern is a movement pattern that is blocked by any piece obstructing its slide path before reaching its destination.
 *
 * @author Joshua Zierman
 */
public class SlidePattern extends MovementPattern {//TODO write unit tests


    /**
     * creates a slide movement pattern
     *
     * @param kingwardOffset         the number of squares to the right (when viewing from white's starting
     *                               side) that the movement pattern will shift the piece.
     * @param forwardOffset          the number of squares forward (away from its color's starting rows) that
     *                               the movement pattern will shift the piece
     * @param captureType            the capturing type of the movement pattern.
     * @param mustBeFirstMoveOfPiece if the movement pattern only applies to the first movement of the piece.
     */
    SlidePattern(int kingwardOffset, int forwardOffset, CaptureType captureType, boolean mustBeFirstMoveOfPiece) {
        super(kingwardOffset, forwardOffset, captureType, mustBeFirstMoveOfPiece);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isJump() {
        return false;
    }


    /**
     * Gets the kingward (right when looking at the board with black at the top) slide direction offset.
     *
     * @return -1 if slide is queenward, 0 if there is no kingward or queenward movement, or 1 if the slide is kingward.
     */
    public int getKingwardSlideOffset() {
        int x = getKingwardOffset();

        return x != 0 ? x / abs(x) : 0;
    }

    /**
     * Gets the forward (away from the piece's color's starting rows) slide direction offset.
     *
     * @return -1 if slide is backward, 0 if there is no forward or backward movement, or 1 if the slide is forward.
     */
    public int getForwardSlideOffset() {

        int y = getForwardOffset();

        return y != 0 ? y / abs(y) : 0;
    }
}


