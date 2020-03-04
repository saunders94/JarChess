package com.example.jarchess.match.participant;

import com.example.jarchess.JarAccount;
import com.example.jarchess.match.ChessColor;

/**
 * A player is a match participant controlled by the person who is using the device.
 *
 * @author Joshua Zierman
 */
public class Player extends LocalPartipant {

    /**
     * Creates a player.
     *
     * @param color the color of the participant
     */
    public Player(ChessColor color) {
        super(
                JarAccount.getInstance().getName(),
                color,
                JarAccount.getInstance().getAvatarStyle()
        );
    }

}
