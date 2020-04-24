package com.example.jarchess.match;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.JarAccount;
import com.example.jarchess.match.activity.EasyAIMatchActivity;
import com.example.jarchess.match.activity.HardAIMatchActivity;
import com.example.jarchess.match.activity.LocalMultiplayerMatchActivity;
import com.example.jarchess.match.activity.OnlineMultiplayerMatchActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.participant.EasyAIOpponent;
import com.example.jarchess.match.participant.HardAIOpponent;
import com.example.jarchess.match.participant.LocalOpponent;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.RemoteOpponentController;
import com.example.jarchess.online.OnlineMatchInfoBundle;

import static com.example.jarchess.match.MatchNetworkIO.DatapackageQueueAdapter;

//TODO javadocs
public class MatchBuilder {
    private static MatchBuilder instance = null;
    private final JarAccount account = JarAccount.getInstance();
    private OnlineMatchInfoBundle onlineMatchInfoBundle;
    private MatchClockChoice matchClockChoice = MatchClockChoice.CLASSIC_FIDE_MATCH_CLOCK;
    private static final String TAG = "MatchStarter";

    private MatchBuilder() {
    }

    public static MatchBuilder getInstance() {
        if (instance == null) {
            instance = new MatchBuilder();
        }

        return instance;
    }

    public void setMatchClockChoice(@NonNull MatchClockChoice matchClockChoice) {
        this.matchClockChoice = matchClockChoice;
    }

    public void multiplayerSetup(OnlineMatchInfoBundle onlineMatchInfoBundle) {
        Log.d(TAG, "multiplayerSetup() called with: onlineMatchInfoBundle = [" + onlineMatchInfoBundle + "]");
        Log.d(TAG, "multiplayerSetup is running on thread: " + Thread.currentThread().getName());
        this.onlineMatchInfoBundle = onlineMatchInfoBundle;
    }

    public Match startEasyAIMatch(EasyAIMatchActivity easyAIMatchActivity, LocalParticipantController localParticipantController) {
        ChessColor opponentColor = ChessColor.getRandom();
        MatchParticipant opponent = new EasyAIOpponent(opponentColor);

        return new PlayerMatch(opponent, matchClockChoice, localParticipantController, easyAIMatchActivity);
    }

    public Match startHardAIMatch(HardAIMatchActivity hardAIMatchActivity, LocalParticipantController localParticipantController) {

        ChessColor opponentColor = ChessColor.getRandom();
        MatchParticipant opponent = new HardAIOpponent(opponentColor);

        return new PlayerMatch(opponent, matchClockChoice, localParticipantController, hardAIMatchActivity);
    }

    public Match startLocalMultiplayerMatch(LocalMultiplayerMatchActivity localMultiplayerMatchActivity, LocalParticipantController localParticipantController) {
        ChessColor opponentColor = ChessColor.getRandom();
        MatchParticipant opponent = new LocalOpponent(opponentColor, localParticipantController);

        return new PlayerMatch(opponent, matchClockChoice, localParticipantController, localMultiplayerMatchActivity);
    }

    public Match startRemoteMultiplayerMatch(OnlineMultiplayerMatchActivity onlineMultiplayerMatchActivity, LocalParticipantController localParticipantController, RemoteOpponentController remoteOpponentController) {
        Log.d(TAG, "startRemoteMultiplayerMatch() called with: onlineMultiplayerMatchActivity = [" + onlineMultiplayerMatchActivity + "], localParticipantController = [" + localParticipantController + "]");
        Log.d(TAG, "startRemoteMultiplayerMatch is running on thread: " + Thread.currentThread().getName());
        if (onlineMatchInfoBundle == null) {
            throw new IllegalStateException("MatchStarter.multiplayer Setup method must be called before match is started");
        }

        // adapts the queue to act as a sender and as a receiver of Datapackages

        DatapackageQueueAdapter adapter = new DatapackageQueueAdapter(onlineMatchInfoBundle.getDatapackageQueue());


        return new OnlineMatch(onlineMatchInfoBundle, matchClockChoice, adapter, adapter, localParticipantController, remoteOpponentController, onlineMultiplayerMatchActivity);
    }
}
