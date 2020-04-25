package com.example.jarchess.match.participant;

import com.example.jarchess.match.DrawResponse;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.PauseResponse;

public interface RemoteOpponentController {
    DrawResponse getDrawRequestResponseForRemoteOpponent() throws MatchOverException, InterruptedException;

    PauseResponse getPauseRequestResponseForRemoteOpponent() throws MatchOverException, InterruptedException;

}
