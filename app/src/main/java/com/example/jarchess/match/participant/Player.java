package com.example.jarchess.match.participant;

import com.example.jarchess.jaraccount.JarAccount;
import com.example.jarchess.match.ChessColor;

/**
 * A player is a match participant controlled by the person who is using the device.
 *
 * @author Joshua Zierman
 */
public class Player extends LocalParticipant {

    /**
     * Creates a player.
     *
     * @param color the color of the participant
     * @param localParticipantController
     */
    public Player(ChessColor color, LocalParticipantController localParticipantController) {
        super(
                JarAccount.getInstance().getName(),
                color,
                JarAccount.getInstance().getAvatarStyle(),
                localParticipantController);
    }

}
