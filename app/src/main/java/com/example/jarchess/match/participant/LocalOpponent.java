package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.styles.GuestAvatarStyle;

/**
 * a local opponent is a match participant that is controlled by a person using the same device as the player
 *
 * @author Joshua Zierman
 */
public class LocalOpponent extends LocalParticipant {


    /**
     * Creates a local participant.
     *
     * @param color the color of the participant
     */
    public LocalOpponent(ChessColor color) {
        super("Guest", color, GuestAvatarStyle.getInstance());
    }
}
