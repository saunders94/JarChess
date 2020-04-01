package com.example.jarchess.match;

import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.RemoteOpponent;
import com.example.jarchess.online.OnlineMatchInfoBundle;
import com.example.jarchess.online.datapackage.DatapackageReceiver;
import com.example.jarchess.online.datapackage.DatapackageSender;

public class OnlineMatch extends PlayerMatch {
    public final static MatchClockChoice ONLINE_MATCH_CLOCK_CHOICE = MatchClockChoice.CLASSIC_FIDE_MATCH_CLOCK;

    public OnlineMatch(OnlineMatchInfoBundle onlineMatchInfoBundle, DatapackageSender sender, DatapackageReceiver receiver, LocalParticipantController localParticipantController, MatchActivity matchActivity) {
        super(extractOpponent(onlineMatchInfoBundle, sender, receiver), ONLINE_MATCH_CLOCK_CHOICE, localParticipantController, matchActivity);
    }

    private static MatchParticipant extractOpponent(OnlineMatchInfoBundle onlineMatchInfoBundle, DatapackageSender sender, DatapackageReceiver receiver) {
        final String playerName;
        final String blackName;
        final String whiteName;
        final ChessColor playerColor;
        RemoteOpponentInfoBundle opponentInfoBundle = onlineMatchInfoBundle.getRemoteOpponentInfoBundle();

        return new RemoteOpponent(opponentInfoBundle.getColor(), opponentInfoBundle, sender, receiver);
    }

}
