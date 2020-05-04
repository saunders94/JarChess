package com.example.jarchess.jaraccount.styles.chessboard;

public enum ChessboardStyles {

    MARBLE_1(0, MarbleChessboardStyle.getInstance()),
    WOOD_1(1, Wood1ChessboardStyle.getInstance()),
    SOLID_1(2, Solid1ChessboardStyle.getInstance());

    private final int intValue;
    private final ChessboardStyle chessboardStyle;
    private final int unlockedAt;

    ChessboardStyles(int i, ChessboardStyle chessboardStyle) {
        this(i, 0, chessboardStyle);
    }

    ChessboardStyles(int i, int unlockedAt, ChessboardStyle chessboardStyle) {
        intValue = i;
        this.chessboardStyle = chessboardStyle;
        this.unlockedAt = unlockedAt;
    }

    public static ChessboardStyles getFromInt(int i) {
        return values()[i];
    }

    public ChessboardStyle getChessboardStyle() {
        return chessboardStyle;
    }

    public int getIntValue() {
        return intValue;
    }
}
