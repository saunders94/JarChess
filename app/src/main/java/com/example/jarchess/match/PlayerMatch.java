package com.example.jarchess.match;

import androidx.annotation.NonNull;

import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.clock.MatchClockObserver;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;

//TODO javadocs
public class PlayerMatch extends Match {

    private final Player player;

    public PlayerMatch(@NonNull Player player, @NonNull MatchParticipant opponent, MatchClockChoice matchClockChoice, MatchClockObserver matchClockObserver) {
        super(player, opponent, matchClockChoice, matchClockObserver);
        this.player = player;

    }
    public Player getPlayer() {
        return player;
    }

}
