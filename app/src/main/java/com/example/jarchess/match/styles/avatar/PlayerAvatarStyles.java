package com.example.jarchess.match.styles.avatar;

public enum PlayerAvatarStyles {
    LEOPARD_PRINT(0, LeopardPrintAvatarStyle.getInstance()),
    YELLOW_BLACK_YELLOw_CIRCLE(1, YellowBlackYellowCircleAvatarStyle.getInstance());

    private final int intValue;
    private final AvatarStyle avatarStyle;
    private final int unlockedAt;

    PlayerAvatarStyles(int i, AvatarStyle avatarStyle) {
        this(i, 0, avatarStyle);
    }

    PlayerAvatarStyles(int i, int unlockedAt, AvatarStyle avatarStyle) {
        intValue = i;
        this.avatarStyle = avatarStyle;
        this.unlockedAt = unlockedAt;
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
