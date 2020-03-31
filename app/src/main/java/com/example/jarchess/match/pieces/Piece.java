package com.example.jarchess.match.pieces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.pieces.movementpatterns.MovementPattern;
import com.example.jarchess.online.datapackage.JSONConverter;
import com.example.jarchess.online.datapackage.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A Piece is a representation of a chess game piece.
 *
 * @author Joshua Zierman
 */
public abstract class Piece implements Cloneable {

    /*
     * The starting position of the piece is important when a piece is captured and made visible on the captured pieces view.
     * The starting position of the piece is passed to the promoted version of a piece so it can be placed in the correct spot when captured.
     */
    private final Coordinate startingPosition;
    private final ChessColor color;
    private final Type type;
    private final Collection<MovementPattern> movementPatterns;
    private boolean hasMoved;
    private boolean isClone;

    /**
     * Creates a Piece
     *
     * @param color            the color of the piece
     * @param type             the type of the piece
     * @param startingPosition the starting position of the piece
     */
    public Piece(@NonNull ChessColor color, @NonNull Type type, @NonNull Coordinate startingPosition) {
        this.color = color;
        this.type = type;
        this.startingPosition = startingPosition;
        hasMoved = false;
        movementPatterns = new LinkedList<MovementPattern>();
        isClone = false;
    }

    public Piece(@NonNull Piece pieceToCopy) {
        this(pieceToCopy.color, pieceToCopy.type, pieceToCopy.startingPosition);
        hasMoved = pieceToCopy.hasMoved;
        movementPatterns.addAll(pieceToCopy.movementPatterns);
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
    void add(Iterable<MovementPattern> patterns) {
        for (MovementPattern pattern : patterns) {
            add(pattern);
        }
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
     * gets the movement patterns of this piece.
     *
     * @return the movement patterns of this piece
     */
    public Collection<MovementPattern> getMovementPatterns() {
        return movementPatterns;
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
     * Gets the type of this piece.
     *
     * @return the <code>Piece.Type</code> of this piece.
     */
    @NonNull
    public Type getType() {
        return type;
    }

    /**
     * Checks to see if this piece has moved
     *
     * @return true if this piece has moved, otherwise returns false
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public int hashCode() {
        int hashCode = type.hashCode();
        hashCode ^= color.hashCode();
        hashCode ^= startingPosition.hashCode();
        hashCode ^= Boolean.valueOf(hasMoved).hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this.getClass() == obj.getClass()) {
            Piece that = (Piece) obj;

            ChessColor c = this.getColor();

            if (this.getColor() == null) {
                throw new RuntimeException("hit");
            }

            boolean p1 = this.getColor().equals(that.getColor());
            boolean p2 = this.getType().equals(that.getType());
            boolean p3 = this.hasMoved == that.hasMoved;
            boolean p4 = this.startingPosition.equals(that.startingPosition);

            return p1
                    && p2
                    && p3
                    && p4;
        }

        return false;

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Piece clone = (Piece) super.clone();
        clone.isClone = true;
        return clone;
    }

    @NonNull
    @Override
    public String toString() {
        return type.toString();
    }


    /**
     * Sets the piece as moved.
     */
    public void setAsMoved() {
        if (!hasMoved) {
            this.hasMoved = true;
        }
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


        public static final String JSON_PROPERTY_NAME_NAME = "name";
        public static final String JSON_PROPERTY_NAME_INT_VALUE = "intValue";
        int intValue;

        /**
         * Constructs a piece type with a linked integer value.
         *
         * @param i an integer representation of the enum value
         */
        Type(int i) {
            intValue = i;
        }

        public static Type getFromInt(int i) {
            return Type.values()[i];
        }

        /**
         * Gets the integer value linked to this type.
         *
         * @return the integer value linked to this type
         */
        public int getIntValue() {
            return intValue;
        }

        @Override
        public String toString() {
            return this.name().substring(0, 1).toUpperCase() + this.name().substring(1);
        }
    }
}
