package com.example.jarchess.match;

import com.example.jarchess.JarAccount;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.RemoteOpponent;
import com.example.jarchess.online.OnlineMatchInfoBundle;
import com.example.jarchess.online.datapackage.DatapackageReceiver;
import com.example.jarchess.online.datapackage.DatapackageSender;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

public class OnlineMatch extends PlayerMatch {
    public final static MatchClockChoice ONLINE_MATCH_CLOCK_CHOICE = MatchClockChoice.CLASSIC_FIDE_MATCH_CLOCK;

    public OnlineMatch(OnlineMatchInfoBundle onlineMatchInfoBundle, DatapackageSender sender, DatapackageReceiver receiver, LocalParticipantController localParticipantController) {
        super(extractOpponent(onlineMatchInfoBundle, sender, receiver), ONLINE_MATCH_CLOCK_CHOICE, localParticipantController);
    }

    private static MatchParticipant extractOpponent(OnlineMatchInfoBundle onlineMatchInfoBundle, DatapackageSender sender, DatapackageReceiver receiver) {
        final String playerName;
        final String blackName;
        final String whiteName;
        final ChessColor playerColor;
        playerName = JarAccount.getInstance().getName();
        blackName = onlineMatchInfoBundle.getBlackOpponentInfoBundle().getName();
        whiteName = onlineMatchInfoBundle.getWhiteOpponentInfoBundle().getName();


        if (playerName.equals(blackName)) {

            // opponent is white
            return new RemoteOpponent(WHITE, onlineMatchInfoBundle.getWhiteOpponentInfoBundle(), sender, receiver);

        } else if (playerName.equals(whiteName)) {
            // opponent is black
            return new RemoteOpponent(BLACK, onlineMatchInfoBundle.getBlackOpponentInfoBundle(), sender, receiver);
        } else {
            throw new RuntimeException("Provided online info bundle did not contain a participant info bundle with a name matching :" + playerName + "\n" +
                    "names received where " + blackName + " and " + whiteName + ".");

        }
    }

}
