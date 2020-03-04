package com.example.jarchess.match;

import java.util.Random;

public enum ChessColor {
    BLACK(0), WHITE(1);

    private final static Random random = new Random();
    private final int intValue;

    ChessColor(int i) {
        intValue = i;
    }

    public static ChessColor getRandom() {
        if (random.nextBoolean()) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

    public static ChessColor getOther(ChessColor color) {
        if (color == BLACK) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

    public int getIntValue() {
        return intValue;
    }
}
