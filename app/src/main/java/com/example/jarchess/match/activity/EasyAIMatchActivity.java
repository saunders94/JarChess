package com.example.jarchess.match.activity;

import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchStarter;

public class EasyAIMatchActivity extends MatchActivity {
    @Override
    public Match createMatch() {
        return MatchStarter.getInstance().startEasyAIMatch(this);
    }
}
