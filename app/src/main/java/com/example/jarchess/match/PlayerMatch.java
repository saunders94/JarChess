package com.example.jarchess.match;

import androidx.annotation.NonNull;

import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;
import com.example.jarchess.match.resignation.ResignationEvent;

//TODO javadocs
public class PlayerMatch extends Match {

    private final Player player;

    public PlayerMatch(@NonNull Player player, @NonNull MatchParticipant opponent) {
        super(player, opponent);
        this.player = player;

    }

    public Player getPlayer() {
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void observeResignationEvent(ResignationEvent resignationEvent) {
        //TODO
    }
}
