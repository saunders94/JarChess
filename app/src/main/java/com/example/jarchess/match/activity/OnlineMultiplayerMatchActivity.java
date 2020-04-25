package com.example.jarchess.match.activity;

import com.example.jarchess.match.DrawResponse;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchBuilder;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.PauseResponse;
import com.example.jarchess.match.participant.RemoteOpponentController;

public class OnlineMultiplayerMatchActivity extends MatchActivity implements RemoteOpponentController {
    @Override
    public Match createMatch() {

        return MatchBuilder.getInstance().startRemoteMultiplayerMatch(this, this, this);
    }

    @Override
    public DrawResponse getDrawRequestResponseForRemoteOpponent() throws MatchOverException, InterruptedException {

        return getDrawRequestResponse();
    }

    @Override
    public PauseResponse getPauseRequestResponseForRemoteOpponent() throws MatchOverException, InterruptedException {
        return getPauseRequestResponse();
    }

}
