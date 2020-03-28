package com.example.jarchess.match.events;

import com.example.jarchess.match.result.Result;

public class MatchEndingEvent {
    final Result matchResult;

    public MatchEndingEvent(Result matchResult) {
        this.matchResult = matchResult;
    }

    public Result getMatchResult() {
        return matchResult;
    }
}
