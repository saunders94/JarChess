package com.example.jarchess.jaraccount.styles.chessboard;

public enum ChessboardStyleChoice {

    MARBLE_1(0, MarbleChessboardStyle.getInstance()),
    WOOD_1(1, Wood1ChessboardStyle.getInstance()),
    WOOD_2(2, Wood2ChessboardStyle.getInstance()),
    SOLID_1(3, Solid1ChessboardStyle.getInstance());

    private final int intValue;
    private final ChessboardStyle chessboardStyle;
    private final int unlockedAt;

    ChessboardStyleChoice(int i, ChessboardStyle chessboardStyle) {
        this(i, 0, chessboardStyle);
    }

    ChessboardStyleChoice(int i, int unlockedAt, ChessboardStyle chessboardStyle) {
        intValue = i;
        this.chessboardStyle = chessboardStyle;
        this.unlockedAt = unlockedAt;
    }

    public static ChessboardStyleChoice getFromInt(int i) {
        return values()[i];
    }

    public ChessboardStyle getChessboardStyle() {
        return chessboardStyle;
    }

    public int getIntValue() {
        return intValue;
    }
}
