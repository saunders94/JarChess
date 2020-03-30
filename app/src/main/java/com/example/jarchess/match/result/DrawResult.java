package com.example.jarchess.match.result;

public abstract class DrawResult extends ChessMatchResult {
    public DrawResult() {
        super(null);
    }

    protected abstract String getDrawTypeString();

    @Override
    protected String getMessage() {
        return "The match resulted in a " + getDrawTypeString() + " draw";
    }
}
