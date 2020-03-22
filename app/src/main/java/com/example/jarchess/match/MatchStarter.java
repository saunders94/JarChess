package com.example.jarchess.match;

import androidx.annotation.NonNull;

import com.example.jarchess.JarAccount;
import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.activity.EasyAIMatchActivity;
import com.example.jarchess.match.activity.HardAIMatchActivity;
import com.example.jarchess.match.activity.LocalMultiplayerMatchActivity;
import com.example.jarchess.match.activity.OnlineMultiplayerMatchActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.participant.EasyAIOpponent;
import com.example.jarchess.match.participant.HardAIOpponent;
import com.example.jarchess.match.participant.LocalOpponent;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;
import com.example.jarchess.match.participant.RemoteOpponent;
import com.example.jarchess.online.OnlineMatch;

import static com.example.jarchess.online.datapackage.MatchNetworkIO.DatapackageQueueAdapter;

//TODO javadocs
public class MatchStarter {
    private static MatchStarter instance = null;
    private final JarAccount account = JarAccount.getInstance();
    private OnlineMatch onlineMatch;
    private MatchClockChoice matchClockChoice = MatchClockChoice.CLASSIC_FIDE_MATCH_CLOCK;


    private MatchStarter() {
    }

    public static MatchStarter getInstance() {
        if (instance == null) {
            instance = new MatchStarter();
        }

        return instance;
    }

    public void setMatchClockChoice(@NonNull MatchClockChoice matchClockChoice) {
        this.matchClockChoice = matchClockChoice;
    }

    public void multiplayerSetup(OnlineMatch onlineMatch) {
        this.onlineMatch = onlineMatch;
    }

    public Match startEasyAIMatch(EasyAIMatchActivity easyAIMatchActivity) {
        ChessColor playerColor = ChessColor.getRandom();
        Player player = new Player(playerColor);

        ChessColor opponentColor = ChessColor.getOther(playerColor);
        MatchParticipant opponent = new EasyAIOpponent(opponentColor);

        return new PlayerMatch(player, opponent, matchClockChoice, easyAIMatchActivity);
    }

    public Match startHardAIMatch(HardAIMatchActivity hardAIMatchActivity) {
        ChessColor playerColor = ChessColor.getRandom();
        Player player = new Player(playerColor);

        ChessColor opponentColor = ChessColor.getOther(playerColor);
        MatchParticipant opponent = new HardAIOpponent(opponentColor);

        return new PlayerMatch(player, opponent, matchClockChoice, hardAIMatchActivity);
    }

    public Match startLocalMultiplayerMatch(LocalMultiplayerMatchActivity localMultiplayerMatchActivity) {
        ChessColor playerColor = ChessColor.getRandom();
        Player player = new Player(playerColor);

        ChessColor opponentColor = ChessColor.getOther(playerColor);
        MatchParticipant opponent = new LocalOpponent(opponentColor);

        return new PlayerMatch(player, opponent, matchClockChoice, localMultiplayerMatchActivity);
    }

    public Match startRemoteMultiplayerMatch(OnlineMultiplayerMatchActivity onlineMultiplayerMatchActivity) {

        if (onlineMatch == null) {
            throw new IllegalStateException("MatchStarter.multiplayerSetup method must be called before match is started");
        }

        // adapts the queue to act as a sender and as a receiver of Datapackages
        DatapackageQueueAdapter addapter = new DatapackageQueueAdapter(onlineMatch.getDatapackageQueue());

        RemoteOpponentInfoBundle remoteOpponentInfoBundle = null;
        ChessColor opponentColor = null;

        if (onlineMatch.getBlackOpponentInfoBundle().getName().equals(JarAccount.getInstance().getName())) {
            remoteOpponentInfoBundle = onlineMatch.getWhiteOpponentInfoBundle();
            opponentColor = ChessColor.WHITE;
        } else if (onlineMatch.getWhiteOpponentInfoBundle().getName().equals(JarAccount.getInstance().getName())) {
            remoteOpponentInfoBundle = onlineMatch.getBlackOpponentInfoBundle();
            opponentColor = ChessColor.BLACK;
        } else {
            throw new RuntimeException("Expecting one of the onlineMatch bundles to have an opponent " +
                    "info bundle with a name matching this account's name of " + JarAccount.getInstance().getName() +
                    ", but found " + onlineMatch.getWhiteOpponentInfoBundle().getName() + " and " +
                    onlineMatch.getBlackOpponentInfoBundle().getName());
        }


        MatchParticipant opponent = new RemoteOpponent(opponentColor, remoteOpponentInfoBundle, addapter, addapter);
        Player player = new Player(ChessColor.getOther(opponentColor));
        return new PlayerMatch(player, opponent, matchClockChoice, onlineMultiplayerMatchActivity);
    }
}
