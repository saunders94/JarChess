package com.example.jarchess.match.styles;

public enum PlayerAvatarStyles {
    LEOPARD_PRINT(0, LeopardPrintAvatarStyle.getInstance()),
    YELLOW_BLACK_YELLOw_CIRCLE(1, YellowBlackYellowCircleAvatarStyle.getInstance());

    private final int intValue;
    private final AvatarStyle avatarStyle;

    PlayerAvatarStyles(int i, AvatarStyle avatarStyle) {
        intValue = i;
        this.avatarStyle = avatarStyle;

    }

    public static PlayerAvatarStyles getFromInt(int i) {
        return values()[i];
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public int getIntValue() {
        return intValue;
    }
}
