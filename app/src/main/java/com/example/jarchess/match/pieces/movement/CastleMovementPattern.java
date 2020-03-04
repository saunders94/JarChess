package com.example.jarchess.match.pieces.movement;

/**
 * A castle movement pattern is a special movement pattern for the castle move that involves a rook and king piece.
 */
public class CastleMovementPattern extends SlidePattern {//TODO write unit tests


    /**
     * Creates a movement pattern.
     *
     * @param kingwardOffset The number of spaces right (when black starting position is at the top) that the movement pattern would shift the piece. A negative value indicates a queenward movement at the magnitude.
     */
    CastleMovementPattern(int kingwardOffset) {

        super(kingwardOffset, 0, CaptureType.CANNOT_CAPTURE, true);
    }


}
