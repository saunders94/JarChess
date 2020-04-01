package com.example.jarchess.match.activity;

import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchBuilder;

public class HardAIMatchActivity extends MatchActivity {
    @Override
    public Match createMatch() {
        return MatchBuilder.getInstance().startHardAIMatch(this, this);
    }
}
