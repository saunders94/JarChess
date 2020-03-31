package com.example.jarchess.match.events;

import com.example.jarchess.match.result.ChessMatchResult;

public class MatchEndingEvent {
    final ChessMatchResult matchChessMatchResult;

    public MatchEndingEvent(ChessMatchResult matchChessMatchResult) {
        this.matchChessMatchResult = matchChessMatchResult;
    }

    public ChessMatchResult getMatchChessMatchResult() {
        return matchChessMatchResult;
    }
}
