package com.example.jarchess.match.pieces;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.movementpatterns.MovementPattern;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A Piece is a representation of a chess game piece.
 *
 * @author Joshua Zierman
 */
public abstract class Piece { //TODO write unit tests

    /*
     * The starting position of the piece is important when a piece is captured and made visible on the captured pieces view.
     * The starting position of the piece is passed to the promoted version of a piece so it can be placed in the correct spot when captured.
     */
    private final Coordinate startingPosition;
    private final ChessColor color;
    private final Type type;
    private final Collection<MovementPattern> movementPatterns;
    private boolean hasMoved;

    /**
     * Creates a Piece
     *
     * @param color            the color of the piece
     * @param type             the type of the piece
     * @param startingPosition the starting position of the piece
     */
    public Piece(ChessColor color, Type type, Coordinate startingPosition) {
        this.color = color;
        this.type = type;
        this.startingPosition = startingPosition;
        hasMoved = false;
        movementPatterns = new LinkedList<MovementPattern>();
    }

    /**
     * Gets the color of this piece
     *
     * @return the color of this piece
     */
    public ChessColor getColor() {
        return color;
    }

    /**
     * Gets the type of this piece.
     *
     * @return the <code>Piece.Type</code> of this piece.
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the starting position of this piece, or in the event of a promoted piece, the starting position of the pawn this piece was promoted from.
     *
     * @return the starting position of this piece
     */
    public Coordinate getStartingPosition() {
        return startingPosition;
    }

    /**
     * Sets the piece as moved.
     */
    public void setAsMoved() {
        this.hasMoved = true;
    }

    /**
     * adds a movement pattern to the piece's collection of movement patterns.
     *
     * @param pattern the pattern to add
     */
    void add(MovementPattern pattern) {
        movementPatterns.add(pattern);
    }

    /**
     * adds a collection of movement patterns to the piece's collection of movement patterns.
     *
     * @param patterns a collection of movement patterns to add
     */
    void add(Collection<MovementPattern> patterns) {
        for (MovementPattern pattern : patterns) {
            add(pattern);
        }
    }

    /**
     * Checks to see if this piece has moved
     *
     * @return true if this piece has moved, otherwise returns false
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * gets the movement patterns of this piece.
     *
     * @return the movement patterns of this piece
     */
    public Collection<MovementPattern> getMovementPatterns() {
        return movementPatterns;
    }

    /**
     * The types of chess pieces.
     */
    public enum Type {
        PAWN(0),
        ROOK(1),
        KNIGHT(2),
        BISHOP(3),
        QUEEN(4),
        KING(5);

        int intValue;

        /**
         * Constructs a piece type with a linked integer value.
         *
         * @param i an integer representation of the enum value
         */
        Type(int i) {
            intValue = i;
        }

        /**
         * Gets the integer value linked to this type.
         *
         * @return the integer value linked to this type
         */
        public int getIntValue() {
            return intValue;
        }
    }
}
