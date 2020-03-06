package com.example.jarchess.match.pieces.movementpatterns;

/**
 * A movement pattern is a coordinate offset with various conditions that can represent the movements of chess pieces.
 *
 * @author Joshua Zierman
 */
public abstract class MovementPattern {//TODO write unit tests

    private final int kingwardOffset;

    private final int forwardOffset;
    private final CaptureType captureType;
    private final boolean mustBeFirstMoveOfPiece;
    /**
     * Creates a movement pattern.
     *
     * @param kingwardOffset         The number of spaces right (when black starting position is at the top) that the movement pattern would shift the piece. A negative value indicates a queenward movement at the magnitude.
     * @param forwardOffset          The number of spaces up (when black starting position is at the top) that the movement pattern would shift the piece. A negative value indicates a backward movement at the magnitude.
     * @param captureType            the capturing type of the movement pattern
     * @param mustBeFirstMoveOfPiece if the movement pattern only applies to the first movement of the piece
     * @return the created slide movement pattern
     */
    MovementPattern(int kingwardOffset, int forwardOffset, CaptureType captureType, boolean mustBeFirstMoveOfPiece) {
        this.kingwardOffset = kingwardOffset;
        this.forwardOffset = forwardOffset;
        this.captureType = captureType;
        this.mustBeFirstMoveOfPiece = mustBeFirstMoveOfPiece;
    }

    /**
     * Gets the number of squares kingward that the movement pattern would shift the piece.
     *
     * @return The number of spaces right (when black starting position is at the top) that the movement pattern would shift the piece
     */
    public final int getKingwardOffset() {
        return kingwardOffset;
    }

    /**
     * Gets the number of squares queenward that the movement pattern would shift the piece.
     *
     * @return The number of spaces left (when black starting position is at the top) that the movement pattern would shift the piece
     */
    public final int getQueenwardOffset() {
        return -kingwardOffset;
    }

    /**
     * Gets the number of squares forward that the movement pattern would shift the piece.
     *
     * @return The number of spaces up (when black starting position is at the top) that the movement pattern would shift the piece
     */
    public final int getForwardOffset() {
        return forwardOffset;
    }

    /**
     * Gets the number of squares backward that the movement pattern would shift the piece.
     *
     * @return The number of spaces down (when black starting position is at the top) that the movement pattern would shift the piece
     */
    public final int getBackwardOffset() {
        return -forwardOffset;
    }

    /**
     * Gets the capture type. Some moves require capturing, can't capture, or can capture but don't have to.
     *
     * @return the capture type of this setAsMoved
     */
    public final CaptureType getCaptureType() {
        return captureType;
    }

    /**
     * checks if this movement pattern must be the first setAsMoved made by the piece(s) being moved
     *
     * @return true if the movement pattern can only be executed on the first movement of the piece, otherwise false
     */
    public final boolean mustBeFirstMoveOfPiece() {
        return mustBeFirstMoveOfPiece;
    }

    /**
     * Checks if the movement pattern is for a jump movement.
     * <p>
     * Jump movement patterns allow the piece to land at the destination regardless of blocking pieces as
     * long as the destination is not blocked.
     *
     * @return true if the movement pattern is for a jump, otherwise returns false
     */
    abstract boolean isJump();

    /**
     * Checks if the movement pattern is for a slide movment.
     * <p>
     * Slide movement patterns are blocked if any pieces obstruct the movement before the destination.
     *
     * @return true if the movment pattern is for a slide, otherwise returns false
     */
    public boolean isSlide() {
        return !isJump();
    }

    /**
     * Capture type represents the capturing capabilities and requirements of a movement pattern.
     * <p><ul>
     * <li><code>CAN_CAPTURE</code> means that a movement pattern can capture a piece, but does not need to in order to be used for a setAsMoved. Rook movments are exmaples of this type.
     * <li><code>CANNOT_CAPTURE</code> means that a movement pattern can not capture during the setAsMoved. Standard pawn movement patterns are examples of this type.
     * <li><code>MUST_CAPTURE</code> means that a movement pattern can only be used for a setAsMoved that captures a piece. A pawn's diagonal movement pattern is an example of this type.
     * </ul>
     */
    public enum CaptureType {

        /**
         * A movement pattern of this type can be used as part of a capturing setAsMoved, but doesn't need to.
         */
        CAN_CAPTURE,

        /**
         * A movement pattern of this type cannot be used as part of a capturing setAsMoved.
         */
        CANNOT_CAPTURE,

        /**
         * A movement pattern of this type can only be used as part of a capturing setAsMoved.
         */
        MUST_CAPTURE
    }
}
