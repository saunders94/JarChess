package com.example.jarchess.match;

import com.example.jarchess.match.datapackage.JSONConvertable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * A chess color is the color of a square, piece, player, move, or turn in chess.
 * <p>
 * Possible chess colors:
 * <ul>
 * <li>BLACK
 * <li>WHITE
 * </ul>
 *
 * @author Joshua Zierman
 */
public enum ChessColor implements JSONConvertable<ChessColor> {

    BLACK(0), WHITE(1);

    private final static Random random = new Random();
    public static final String JSON_PROPERTY_NAME_INT_VALUE = "intValue";
    public static final String JSON_PROPERTY_NAME_NAME = "name";
    private final int intValue;

    /**
     * Creates a chess color
     *
     * @param i the integer representation of the enum value.
     */
    ChessColor(int i) {
        intValue = i;
    }


    /**
     * Gets a random color
     *
     * @return BLACK or WHITE
     */
    public static ChessColor getRandom() {
        if (random.nextBoolean()) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

    /**
     * Gets the other color from the given color.
     *
     * @param color the color provided
     * @return the color that is opposite of the provided color.
     */
    public static ChessColor getOther(ChessColor color) {
        if (color == BLACK) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

    public static ChessColor getFromInt(int colorInt) {
        return values()[colorInt];
    }

    public int getIntValue() {
        return intValue;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JSON_PROPERTY_NAME_INT_VALUE, intValue);
        jsonObject.put(JSON_PROPERTY_NAME_NAME, name());
        return jsonObject;
    }
}
