package com.example.jarchess.match.events;

import com.example.jarchess.match.result.Result;

public class MatchResultIsInEvent {
    final Result matchResult;

    public MatchResultIsInEvent(Result matchResult) {
        this.matchResult = matchResult;
    }

    public Result getMatchResult() {
        return matchResult;
    }
}
