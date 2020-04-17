package com.example.jarchess.match;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

    public void handlePlayerDrawRequest() {

        if (matchHistory.getNextTurnColor() == player.getColor()) {
            Log.d(TAG, "handlePlayerDrawRequest: a");
            DrawResponse response = opponent.respondToDrawRequest(matchHistory);
            Log.d(TAG, "handlePlayerDrawRequest: b");
            if (response == null) {
                //do nothing
            } else if (response.isAccepted()) {
                Log.i(TAG, "handlePlayerDrawRequest: accepted");
                ChessMatchResult result = new AgreedUponDrawResult();
                MatchEndingEvent event = new MatchEndingEvent(result);
                MatchEndingEventManager.getInstance().notifyAllListeners(event);

            } else {
                Log.i(TAG, "handlePlayerDrawRequest: rejected");
                final int duration = Toast.LENGTH_SHORT;
                matchActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(matchActivity, "Draw request rejected. Make your move.", duration).show();

                    }
                });
            }
        } else {

            final int duration = Toast.LENGTH_SHORT;
            matchActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(matchActivity, "Draw requests can only be issued when it is your move.", duration).show();

                }
            });
        }
    }
}
