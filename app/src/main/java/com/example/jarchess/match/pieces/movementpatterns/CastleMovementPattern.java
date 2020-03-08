package com.example.jarchess.match.pieces.movementpatterns;

import com.example.jarchess.match.ChessColor;

/**
 * A castle movement pattern is a special movement pattern for the castle move that involves a rook and king piece.
 */
public class CastleMovementPattern extends SlidePattern {//TODO write unit tests


    /**
     * Creates a movement pattern.
     *
     * @param kingwardOffset The number of spaces right (when black starting position is at the top) that the movement pattern would shift the piece. A negative value indicates a queenward movement at the magnitude.
     * @param color                 the color of the piece this pattern if for
     */
    CastleMovementPattern(int kingwardOffset, ChessColor color) {

        super(kingwardOffset, 0, CaptureType.CANNOT_CAPTURE, true, color);
    }


}
