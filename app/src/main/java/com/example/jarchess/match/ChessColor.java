package com.example.jarchess.match;

import java.util.Random;

/**
 * A chess color is the color of a square, piece, player, move, or turn in chess.
 * <p>
 * Possible chess colors:
 * <ul>
 * <li>BLACK
 * <li>WHITE
 * </ul>
 */
public enum ChessColor {

    BLACK(0), WHITE(1);

    private final static Random random = new Random();
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

    /**
     * Gets the integer value that represents this enum value.
     *
     * @return the integer representation of the enum, 0 for BLACK and 1 for WHITE
     */
    public int getIntValue() {
        return intValue;
    }
}
