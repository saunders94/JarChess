package com.example.jarchess.match;

import androidx.annotation.NonNull;

import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;

//TODO javadocs
public class PlayerMatch extends Match {

    private final Player player;

    public PlayerMatch(@NonNull MatchParticipant opponent, MatchClockChoice matchClockChoice, LocalParticipantController localParticipantController, MatchActivity matchActivity) {
        super(new Player(ChessColor.getOther(opponent.getColor()), localParticipantController), opponent, matchClockChoice, matchActivity);
        this.player = (Player) (opponent.getColor() == ChessColor.WHITE ? getBlackParticipant() : getWhiteParticipant());
    }

    @Override
    public ChessColor getForceExitWinningColor() {
        return ChessColor.getOther(player.getColor());
    }

    public Player getPlayer() {
        return player;
    }

}
