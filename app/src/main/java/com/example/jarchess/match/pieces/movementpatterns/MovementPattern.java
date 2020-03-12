package com.example.jarchess.match.pieces.movementpatterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;

import static com.example.jarchess.match.ChessColor.WHITE;

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
    private final ChessColor color;
    /**
     * Creates a movement pattern.
     *
     * @param kingwardOffset         The number of spaces right (when black starting position is at the top) that the movement pattern would shift the piece. A negative value indicates a queenward movement at the magnitude.
     * @param forwardOffset          The number of spaces up (when black starting position is at the top) that the movement pattern would shift the piece. A negative value indicates a backward movement at the magnitude.
     * @param captureType            the capturing type of the movement pattern
     * @param mustBeFirstMoveOfPiece if the movement pattern only applies to the first movement of the piece
     * @param color                 the color of the piece this pattern if for
     * @return the created slide movement pattern
     */
    MovementPattern(int kingwardOffset, int forwardOffset, CaptureType captureType, boolean mustBeFirstMoveOfPiece, ChessColor color) {
        this.kingwardOffset = kingwardOffset;
        this.forwardOffset = forwardOffset;
        this.captureType = captureType;
        this.mustBeFirstMoveOfPiece = mustBeFirstMoveOfPiece;
        this.color = color;
    }

    /**
     * Gets a destination coordinate when applying a movement patter to an origin position for a piece of the indicated color.
     * <p>
     * The color of the piece is needed to determine what direction is considered forward.
     *
     * @param origin the starting position of the piece that would be moved, not null
     * @return the destination coordinate if it is a valid coordinate, othewise returns null to indicate that the pattern would
     * move a piece off the board
     */
    @Nullable
    public Coordinate getDestinationFrom(@NonNull Coordinate origin) { //TODO make unit tests

        int destinationColumn = origin.getColumn() + getKingwardOffset();
        int destinationRow = origin.getRow() + (color == WHITE ? getBackwardOffset() : getForwardOffset());

        if (Coordinate.MIN_COLUMN <= destinationColumn
                && destinationColumn <= Coordinate.MAX_COLUMN
                && Coordinate.MIN_ROW <= destinationRow
                && destinationRow <= Coordinate.MAX_ROW) {
            return Coordinate.getByColumnAndRow(destinationColumn, destinationRow);
        } else {
            return null;
        }

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
