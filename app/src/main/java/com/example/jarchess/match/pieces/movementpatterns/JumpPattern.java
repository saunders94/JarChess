package com.example.jarchess.match.pieces.movementpatterns;

import com.example.jarchess.match.ChessColor;

/**
 * A jump pattern is a movement pattern that is not blocked by any piece obstructing its jump path before reaching its destination.
 *
 * @author Joshua Zierman
 */
public class JumpPattern extends MovementPattern {//TODO write unit tests

    /**
     * Creates a movement pattern.
     *
     * @param kingwardOffset         the number of squares to the right (when viewing from white's starting
     *                               side) that the movement pattern will shift the piece
     * @param forwardOffset          the number of squares forward (away from its color's starting rows) that
     *                               the movement pattern will shift the piece
     * @param captureType            the capturing type of the movement pattern
     * @param mustBeFirstMoveOfPiece if the movement pattern only applies to the first movement of the piece
     * @param color                 the color of the piece this pattern if for
     */
    JumpPattern(int kingwardOffset, int forwardOffset, CaptureType captureType, boolean mustBeFirstMoveOfPiece, ChessColor color) {
        super(kingwardOffset, forwardOffset, captureType, mustBeFirstMoveOfPiece, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean isJump() {
        return true;
    }
}
