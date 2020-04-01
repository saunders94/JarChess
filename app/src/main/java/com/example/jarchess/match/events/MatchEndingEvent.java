package com.example.jarchess.match.events;

import com.example.jarchess.match.result.ChessMatchResult;

public class MatchEndingEvent {
    final ChessMatchResult result;

    public MatchEndingEvent(ChessMatchResult matchChessMatchResult) {
        this.result = matchChessMatchResult;
    }

    public ChessMatchResult getResult() {
        return result;
    }
}
