package com.example.jarchess.match.activity;

import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchMaker;

public class LocalMultiplayerMatchActivity extends MatchActivity {
    @Override
    public Match createMatch() {
        return MatchMaker.getInstance().startLocalMultiplayerMatch();
    }
}
