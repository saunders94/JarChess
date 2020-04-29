package com.example.jarchess.match.styles.avatar;

public enum PlayerAvatarStyles {
    LEOPARD_PRINT(0, LeopardPrintAvatarStyle.getInstance()),
    YELLOW_BLACK_YELLOw_CIRCLE(1, YellowBlackYellowCircleAvatarStyle.getInstance()),
    HONEYCOMB(2, HoneycombAvatarStyle.getInstance(), 1);

    private final int intValue;
    private final AvatarStyle avatarStyle;
    private final int unlockedAt;

    PlayerAvatarStyles(int i, AvatarStyle avatarStyle) {
        this(i, avatarStyle, 0);
    }

    PlayerAvatarStyles(int i, AvatarStyle avatarStyle, int unlockedAt) {
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
