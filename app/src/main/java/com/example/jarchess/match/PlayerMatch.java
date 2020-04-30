package com.example.jarchess.match;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.events.MatchEndingEvent;
import com.example.jarchess.match.events.MatchEndingEventManager;
import com.example.jarchess.match.participant.AIOpponent;
import com.example.jarchess.match.participant.LocalOpponent;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;
import com.example.jarchess.match.participant.RemoteOpponent;
import com.example.jarchess.match.result.AgreedUponDrawResult;
import com.example.jarchess.match.result.ChessMatchResult;
import com.example.jarchess.match.view.MatchView;

//TODO javadocs
public class PlayerMatch extends Match {

    private static final String TAG = "PlayerMatch";
    private final Player player;
    private final MatchParticipant opponent;
    private final Object lock = this;

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

    public void handlePlayerDrawRequest() throws InterruptedException {

        if (matchHistory.getNextTurnColor() == player.getColor()) {
            Log.d(TAG, "handlePlayerDrawRequest: a");
            DrawResponse response = opponent.getDrawResponse(matchHistory);
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

    public synchronized void handlePlayerPauseRequest() throws InterruptedException {
        Log.d(TAG, "handlePlayerPauseRequest() called");
        Log.d(TAG, "handlePlayerPauseRequest is running on thread: " + Thread.currentThread().getName());
        MatchView matchView = matchActivity.getMatchView();

        Log.i(TAG, "handlePlayerPauseRequest: active player made request");
        // if a local multiplayer or AI match, just pause
        if (opponent instanceof LocalOpponent ||
                opponent instanceof AIOpponent) {
            Log.i(TAG, "handlePlayerPauseRequest: automatic agreement");

            // we don't need to check for agreement
            if (matchClock.isRunning()) {
                matchClock.stop();
                matchView.showAcceptedPauseDialog();
            }
        } else if (opponent instanceof RemoteOpponent) {
            Log.d(TAG, "handlePlayerPauseRequest: remoteOpponent detected");
            if (matchHistory.getNextTurnColor() == player.getColor()) {

                final RemoteOpponent remoteOpponent = (RemoteOpponent) opponent;
                matchView.showPendingPauseDialog();
                boolean accepted;

                Log.i(TAG, "handlePlayerPauseRequest: trying to get pause response from remote opponent");
                accepted = remoteOpponent.getPauseResponse().isAccepted();
                Log.i(TAG, "handlePlayerPauseRequest: accepted = " + accepted);

                if (accepted) {
                    matchClock.stop();
                    Log.i(TAG, "handlePlayerPauseRequest: accepted !!!!!!!!!!");
                    matchView.showAcceptedPauseDialog();

                } else {
                    matchView.hidePendingPauseDialog();
                    Log.i(TAG, "handlePlayerPauseRequest: rejected !!!!!!!!!!");
                    final int duration = Toast.LENGTH_SHORT;
                    matchActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(matchActivity, "Pause request rejected. Make your move.", duration).show();
                        }
                    });
                }
            } else {
                Log.i(TAG, "handlePlayerPauseRequest: it is not the turn of the player making the request");
                final int duration = Toast.LENGTH_SHORT;
                matchActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(matchActivity, "Pause request rejected. It is not your turn.", duration).show();
                    }
                });
            }
        } else {
            throw new IllegalStateException("unexpected opponent type");
        }


    }


    public synchronized void handlePlayerResumeRequest() throws InterruptedException {
        Log.d(TAG, "handlePlayerResumeRequest() called");
        Log.d(TAG, "handlePlayerResumeRequest is running on thread: " + Thread.currentThread().getName());

        MatchView matchView = matchActivity.getMatchView();

        // if a local multiplayer or AI match, just resume
        if (opponent instanceof LocalOpponent ||
                opponent instanceof AIOpponent) {
            Log.i(TAG, "handlePlayerResumeRequest: automatic agreement");
            // we don't need to check for agreement
            if (!matchClock.isRunning()) {
                matchClock.resume();
                matchView.hidePendingDrawDialog();
            }
        } else if (opponent instanceof RemoteOpponent) {
            Log.i(TAG, "handlePlayerResumeRequest: remote opponent detected");
            final RemoteOpponent remoteOpponent = (RemoteOpponent) opponent;

            Log.i(TAG, "handlePlayerResumeRequest: showing resumeDialog");
            matchView.showPendingResumeDialog();
            Log.i(TAG, "handlePlayerResumeRequest: notifying and waiting about resume");
            remoteOpponent.notifyAndWaitForResume();
            Log.i(TAG, "handlePlayerResumeRequest: resuming match clock");
            matchClock.resume();
            Log.i(TAG, "handlePlayerResumeRequest: hide the pending pause dialog");
            matchView.hidePendingResumeDialog();

        } else {
            throw new IllegalStateException("unexpected opponent type");
        }
    }
}
