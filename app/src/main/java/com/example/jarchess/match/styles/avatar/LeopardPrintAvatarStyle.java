package com.example.jarchess.match.styles.avatar;

import com.example.jarchess.R;

/**
 * A leopard print avatar style is an avatar style with a square leopard print image.
 *
 * @author Joshua Zierman
 */
public class LeopardPrintAvatarStyle implements AvatarStyle {

    private static final int AVATAR_PLAYER_LEOPARD_PRINT = R.drawable.avatar_player_leopard_print;
    private static LeopardPrintAvatarStyle instance;


    /**
     * Creates an instance of <code>LeopardPrintAvatarStyle</code> to construct a singleton instance
     */
    private LeopardPrintAvatarStyle() {

    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static LeopardPrintAvatarStyle getInstance() {
        if (instance == null) {
            instance = new LeopardPrintAvatarStyle();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getAvatarResourceID() {
        return AVATAR_PLAYER_LEOPARD_PRINT;
    }
}
