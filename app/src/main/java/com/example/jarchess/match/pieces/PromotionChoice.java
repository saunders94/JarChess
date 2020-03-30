package com.example.jarchess.match.pieces;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;
import com.example.jarchess.online.datapackage.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

public enum PromotionChoice implements JSONConvertible<PromotionChoice> {
    PROMOTE_TO_ROOK(0, Piece.Type.ROOK),
    PROMOTE_TO_KNIGHT(1, Piece.Type.KNIGHT),
    PROMOTE_TO_BISHOP(2, Piece.Type.BISHOP),
    PROMOTE_TO_QUEEN(3, Piece.Type.QUEEN),
    ;

    public static final JSONConverter<PromotionChoice> JSON_CONVERTER = PromotionChoiceJSONConverter.getInstance();
    public static final String JSON_PROPERTY_NAME_NAME = "name";
    public static final String JSON_PROPERTY_NAME_INT_VALUE = "intValue";

    private final int intValue;
    private final Piece.Type pieceType;

    PromotionChoice(int intValue, Piece.Type pieceType) {
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

    public Piece.Type getPieceType() {
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
