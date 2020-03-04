package com.example.jarchess.match.styles;

import com.example.jarchess.R;

/**
 * A leopard print avatar style is an avatar style with a square leopard print image.
 */
public class GuestAvatarStyle implements AvatarStyle {

    private static final int AVATAR_RESOURCE_ID = R.drawable.avatar_guest;
    private static GuestAvatarStyle instance;


    /**
     * Creates an instance of <code>GuestAvatarStyle</code> to construct a singleton instance
     */
    private GuestAvatarStyle() {
        //TODO
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static GuestAvatarStyle getInstance() {
        if (instance == null) {
            instance = new GuestAvatarStyle();
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
