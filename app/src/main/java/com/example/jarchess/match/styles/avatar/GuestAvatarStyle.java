package com.example.jarchess.match.styles.avatar;

import androidx.annotation.NonNull;

import com.example.jarchess.R;

/**
 * A leopard print avatar style is an avatar style with a square leopard print image.
 *
 * @author Joshua Zierman
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

    @NonNull
    @Override
    public String getName() {
        return "Guest";
    }
}
