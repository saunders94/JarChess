package com.example.jarchess.match.styles;

import com.example.jarchess.R;

/**
 * A leopard print avatar style is an avatar style with a square leopard print image.
 */
public class YellowBlackYellowCircleAvatarStyle implements AvatarStyle {

    private static final int AVATAR_RESOURCE_ID = R.drawable.avatar_yellow_black_yellow_circles;
    private static YellowBlackYellowCircleAvatarStyle instance;

    /**
     * Creates an instance of <code>YellowBlackYellowCircleAvatarStyle</code> to construct a singleton instance
     */
    private YellowBlackYellowCircleAvatarStyle() {

    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static YellowBlackYellowCircleAvatarStyle getInstance() {
        if (instance == null) {
            instance = new YellowBlackYellowCircleAvatarStyle();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getAvatarResourceID() {
        return AVATAR_RESOURCE_ID;
    }
}
