package com.example.jarchess.match.result;

public abstract class DrawResult extends Result {
    public DrawResult() {
        super(null);
    }

    @Override
    protected String getMessage() {
        return "The match resulted in a " + getDrawTypeString() + " draw";
    }

    protected abstract String getDrawTypeString();
}
