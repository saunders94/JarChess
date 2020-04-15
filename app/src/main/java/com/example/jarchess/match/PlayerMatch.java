package com.example.jarchess.match;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.LoggedThread;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.events.MatchEndingEvent;
import com.example.jarchess.match.events.MatchEndingEventManager;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;
import com.example.jarchess.match.result.AgreedUponDrawResult;
import com.example.jarchess.match.result.ChessMatchResult;

//TODO javadocs
public class PlayerMatch extends Match {

    private static final String TAG = "PlayerMatch";

    private final Player player;
    private final MatchParticipant opponent;

    public PlayerMatch(@NonNull MatchParticipant opponent, MatchClockChoice matchClockChoice, LocalParticipantController localParticipantController, MatchActivity matchActivity) {
        super(new Player(ChessColor.getOther(opponent.getColor()), localParticipantController), opponent, matchClockChoice, matchActivity);
        this.player = (Player) (opponent.getColor() == ChessColor.WHITE ? getBlackParticipant() : getWhiteParticipant());
        this.opponent = opponent;
    }

    @Override
    public ChessColor getForceExitWinningColor() {
        return ChessColor.getOther(player.getColor());
    }

    public MatchParticipant getOpponent() {
        return opponent;
    }

    public Player getPlayer() {
        return player;
    }

    public synchronized void requestDraw() {
        if (getCurrentTurnColor() == player.getColor()) {
            Log.i(TAG, "requestDraw: player requested draw");

            // TODO show draw request pending view

            new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    DrawResponse response = opponent.respondToDrawRequest(getMatchHistory());
                    // TODO hide draw request pending view
                    if (response.isAccepted()) {
                        Log.i(TAG, "requestDraw: draw was accepted");
                        ChessMatchResult result = new AgreedUponDrawResult();
                        MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(result));
                    } else {
                        Log.i(TAG, "requestDraw: draw was rejected");
                    }
                }
            }, "drawRequestThread").start();
        }
    }
}
