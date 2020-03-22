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
public abstract class Piece implements Cloneable { //TODO write unit tests

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
    void add(Collection<MovementPattern> patterns) {
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

    public enum PromotionChoice implements JSONConvertible<PromotionChoice> {
        PROMOTE_TO_ROOK(0, Type.ROOK),
        PROMOTE_TO_KNIGHT(1, Type.KNIGHT),
        PROMOTE_TO_BISHOP(2, Type.BISHOP),
        PROMOTE_TO_QUEEN(3, Type.QUEEN),
        ;

        public static final JSONConverter<PromotionChoice> JSON_CONVERTER = PromotionChoice.PromotionChoiceJSONConverter.getInstance();
        public static final String JSON_PROPERTY_NAME_NAME = "name";
        public static final String JSON_PROPERTY_NAME_INT_VALUE = "intValue";

        private final int intValue;
        private final Type pieceType;

        PromotionChoice(int intValue, Type pieceType) {
            this.intValue = intValue;
            this.pieceType = pieceType;
        }

        public static PromotionChoice getFromInt(int i) {
            return values()[i];
        }

        public int getIntValue() {
            return intValue;
        }

        @Override
        public JSONObject getJSONObject() throws JSONException {
            return new JSONObject().put(JSON_PROPERTY_NAME_NAME, toString()).put(JSON_PROPERTY_NAME_INT_VALUE, intValue);
        }

        public Type getPieceType() {
            return pieceType;
        }

        public static class PromotionChoiceJSONConverter extends JSONConverter<PromotionChoice> {

            private static PromotionChoiceJSONConverter instance;

            /**
             * Creates an instance of <code>PromotionChoiceJSONConverter</code> to construct a singleton instance
             */
            private PromotionChoiceJSONConverter() {

            }

            /**
             * Gets the instance.
             *
             * @return the instance.
             */
            public static PromotionChoiceJSONConverter getInstance() {
                if (instance == null) {
                    instance = new PromotionChoiceJSONConverter();
                }

                return instance;
            }


            @Override
            public PromotionChoice convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
                return PromotionChoice.getFromInt(jsonObject.getInt(JSON_PROPERTY_NAME_INT_VALUE));
            }
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
