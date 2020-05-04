package com.example.jarchess.jaraccount.styles.avatar;

import androidx.annotation.NonNull;

import com.example.jarchess.R;

/**
 * A leopard print avatar style is an avatar style with a square leopard print image.
 *
 * @author Joshua Zierman
 */
public class HoneycombAvatarStyle implements AvatarStyle {

    private static final int AVATAR_PLAYER_HONEYCOMB = R.drawable.honeycomb;
    private static HoneycombAvatarStyle instance;


    /**
     * Creates an instance of <code>LeopardPrintAvatarStyle</code> to construct a singleton instance
     */
    private HoneycombAvatarStyle() {
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static HoneycombAvatarStyle getInstance() {
        if (instance == null) {
            instance = new HoneycombAvatarStyle();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getAvatarResourceID() {
        return AVATAR_PLAYER_HONEYCOMB;
    }

    @NonNull
    @Override
    public String getName() {
        return "Honeycomb";
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
