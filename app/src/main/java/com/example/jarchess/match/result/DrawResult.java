package com.example.jarchess.match.result;

public abstract class DrawResult extends ChessMatchResult {
    public DrawResult() {
        super(null);
    }

    protected abstract String getDrawTypeString();

    @Override
    protected String getMessage() {
        String a = "a ";
        CharSequence leadingChar = getDrawTypeString().toLowerCase().trim().subSequence(0, 1);
        if ("aeiou".contains(leadingChar)) {
            a = "an ";
        }
        return "The match resulted in " + a + getDrawTypeString() + " draw";
    }
}
