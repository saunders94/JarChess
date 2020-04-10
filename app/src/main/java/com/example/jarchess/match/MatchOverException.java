package com.example.jarchess.match;

import com.example.jarchess.match.result.ChessMatchResult;

public class MatchOverException extends Exception {

    private final ChessMatchResult matchChessMatchResult;

    public MatchOverException(ChessMatchResult matchChessMatchResult) {
        super("MatchOverException with result: " + matchChessMatchResult);
        this.matchChessMatchResult = matchChessMatchResult;
    }

    public ChessMatchResult getMatchChessMatchResult() {
        return matchChessMatchResult;
    }
}
