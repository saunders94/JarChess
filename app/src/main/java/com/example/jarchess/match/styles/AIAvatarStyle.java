package com.example.jarchess.match.styles;

import com.example.jarchess.R;

/**
 * A leopard print avatar style is an avatar style with a square leopard print image.
 */
public class AIAvatarStyle implements AvatarStyle {

    private static final int AVATAR_RESOURCE_ID = R.drawable.avatar_ai;
    private static AIAvatarStyle instance;

    /**
     * Creates an instance of <code>AIAvatarStyle</code> to construct a singleton instance
     */
    private AIAvatarStyle() {
        //TODO
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static AIAvatarStyle getInstance() {
        if (instance == null) {
            instance = new AIAvatarStyle();
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
