package com.example.jarchess.match;

import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.participant.RemoteOpponent;
import com.example.jarchess.match.participant.RemoteOpponentController;
import com.example.jarchess.match.result.ChessMatchResult;
import com.example.jarchess.online.OnlineMatchInfoBundle;
import com.example.jarchess.online.datapackage.DatapackageReceiver;
import com.example.jarchess.online.datapackage.DatapackageSender;

public class OnlineMatch extends PlayerMatch {

    public OnlineMatch(OnlineMatchInfoBundle onlineMatchInfoBundle, MatchClockChoice clockChoice, DatapackageSender sender, DatapackageReceiver receiver, LocalParticipantController localParticipantController, RemoteOpponentController remoteOpponentController, MatchActivity matchActivity) {
        super(extractOpponent(onlineMatchInfoBundle, sender, receiver, remoteOpponentController), clockChoice, localParticipantController, matchActivity);
    }

    private static MatchParticipant extractOpponent(OnlineMatchInfoBundle onlineMatchInfoBundle, DatapackageSender sender, DatapackageReceiver receiver, RemoteOpponentController remoteOpponentController) {
        final String playerName;
        final String blackName;
        final String whiteName;
        final ChessColor playerColor;
        RemoteOpponentInfoBundle opponentInfoBundle = onlineMatchInfoBundle.getRemoteOpponentInfoBundle();

        return new RemoteOpponent(opponentInfoBundle.getColor(), opponentInfoBundle, sender, receiver, remoteOpponentController);
    }

    @Override
    public RemoteOpponent getOpponent() {
        return (RemoteOpponent) super.getOpponent();
    }

    @Override
    protected void setChessMatchResult(ChessMatchResult chessMatchResult) {
        if (this.chessMatchResult == null) {
            super.setChessMatchResult(chessMatchResult);
        }
    }

    @Override
    protected void setMatchChessMatchResult(ChessMatchResult chessMatchResult, MatchHistory matchHistory) {
        if (this.chessMatchResult == null) {
            getOpponent().sendLastTurn(matchHistory);
            getOpponent().send(chessMatchResult);
            super.setMatchChessMatchResult(chessMatchResult, matchHistory);
        }
    }
}
