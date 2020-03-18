package com.example.jarchess.match;

import com.example.jarchess.JarAccount;
import com.example.jarchess.RemoteOpponentAccount;
import com.example.jarchess.match.participant.EasyAIOpponent;
import com.example.jarchess.match.participant.HardAIOpponent;
import com.example.jarchess.match.participant.LocalOpponent;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;
import com.example.jarchess.match.participant.RemoteOpponent;
import com.example.jarchess.online.move.DatapackageQueue;

import static com.example.jarchess.online.datapackage.MatchNetworkIO.DatapackageQueueAddapter;

//TODO javadocs
public class MatchStarter {
    private static MatchStarter instance = null;
    private final JarAccount account = JarAccount.getInstance();
    private DatapackageQueue queue = null;
    private RemoteOpponentAccount remoteOpponentAccount;


    private MatchStarter() {
    }

    public static MatchStarter getInstance() {
        if (instance == null) {
            instance = new MatchStarter();
        }

        return instance;
    }

    public void multiplayerSetup(DatapackageQueue queue, RemoteOpponentAccount remoteOpponentAccount) {

        this.queue = queue;
        this.remoteOpponentAccount = remoteOpponentAccount;
    }

    public Match startEasyAIMatch() {
        ChessColor playerColor = ChessColor.getRandom();
        Player player = new Player(playerColor);

        ChessColor opponentColor = ChessColor.getOther(playerColor);
        MatchParticipant opponent = new EasyAIOpponent(opponentColor);

        return new PlayerMatch(player, opponent);
    }

    public Match startHardAIMatch() {
        ChessColor playerColor = ChessColor.getRandom();
        Player player = new Player(playerColor);

        ChessColor opponentColor = ChessColor.getOther(playerColor);
        MatchParticipant opponent = new HardAIOpponent(opponentColor);

        return new PlayerMatch(player, opponent);
    }

    public Match startLocalMultiplayerMatch() {
        ChessColor playerColor = ChessColor.getRandom();
        Player player = new Player(playerColor);

        ChessColor opponentColor = ChessColor.getOther(playerColor);
        MatchParticipant opponent = new LocalOpponent(opponentColor);

        return new PlayerMatch(player, opponent);
    }

    public Match startRemoteMultiplayerMatch() {

        if (remoteOpponentAccount == null) {
            throw new IllegalStateException("MatchStarter.multiplayerSetup method must be called before match is started");
        }


        ChessColor playerColor = ChessColor.getRandom();
        Player player = new Player(playerColor);
        ChessColor opponentColor = ChessColor.getOther(playerColor);

        // addapts the queue to act as a sender and as a receiver of Datapackages
        DatapackageQueueAddapter addapter = new DatapackageQueueAddapter(queue);


        MatchParticipant opponent = new RemoteOpponent(opponentColor, remoteOpponentAccount, addapter, addapter);

        remoteOpponentAccount = null;
        return new PlayerMatch(player, opponent);
    }
}
