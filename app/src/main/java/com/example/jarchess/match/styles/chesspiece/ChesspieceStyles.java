package com.example.jarchess.match.styles.chesspiece;

public enum ChesspieceStyles {

    NEON_LETTERS(0, NeonLetterChesspieceStyle.getInstance());

    private final int intValue;
    private final ChesspieceStyle chesspieceStyle;
    private final int unlockedAt;

    ChesspieceStyles(int i, ChesspieceStyle chesspieceStyle) {
        this(i, 0, chesspieceStyle);
    }

    ChesspieceStyles(int i, int unlockedAt, ChesspieceStyle chesspieceStyle) {
        intValue = i;
        this.chesspieceStyle = chesspieceStyle;
        this.unlockedAt = unlockedAt;
    }

    public static ChesspieceStyles getFromInt(int i) {
        return values()[i];
    }

    public ChesspieceStyle getChesspieceStyle() {
        return chesspieceStyle;
    }

    public int getIntValue() {
        return intValue;
    }
}
