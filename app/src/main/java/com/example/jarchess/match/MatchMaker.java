package com.example.jarchess.match;

import com.example.jarchess.JarAccount;
import com.example.jarchess.match.participant.EasyAIOpponent;
import com.example.jarchess.match.participant.HardAIOpponent;
import com.example.jarchess.match.participant.LocalOpponent;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.Player;

//TODO javadocs
public class MatchMaker {
    private static MatchMaker instance = null;
    private final JarAccount account = JarAccount.getInstance();

    private MatchMaker() {
    }

    public static MatchMaker getInstance() {
        if (instance == null) {
            instance = new MatchMaker();
        }

        return instance;
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
}
