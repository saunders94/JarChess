package com.example.jarchess.jaraccount.styles.avatar;

public enum PlayerAvatarStyles {
    LEOPARD_PRINT(0, LeopardPrintAvatarStyle.getInstance()),
    YELLOW_BLACK_YELLOw_CIRCLE(1, YellowBlackYellowCircleAvatarStyle.getInstance()),
    HONEYCOMB(2, HoneycombAvatarStyle.getInstance(), 1);

    private final int intValue;
    private final AvatarStyle avatarStyle;
    private final int unlockedAtValue;

    PlayerAvatarStyles(int i, AvatarStyle avatarStyle) {
        this(i, avatarStyle, 0);
    }

    PlayerAvatarStyles(int i, AvatarStyle avatarStyle, int unlockedAtValue) {
        intValue = i;
        this.avatarStyle = avatarStyle;
        this.unlockedAtValue = unlockedAtValue;
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

    public int getUnlockedAtValue() {
        return unlockedAtValue;
    }
}
