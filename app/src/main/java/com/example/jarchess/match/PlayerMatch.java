package com.example.jarchess.match;

import androidx.annotation.NonNull;

import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;

//TODO javadocs
public class PlayerMatch extends Match {

    private final Player player;

    public PlayerMatch(@NonNull MatchParticipant opponent, MatchClockChoice matchClockChoice, LocalParticipantController localParticipantController) {
        super(new Player(ChessColor.getOther(opponent.getColor()), localParticipantController), opponent, matchClockChoice);
        this.player = (Player) (opponent.getColor() == ChessColor.WHITE ? getBlackPlayer() : getWhitePlayer());
    }
    public Player getPlayer() {
        return player;
    }

}
