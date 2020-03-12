package com.example.jarchess.match.result;

/**
 * A Stalemate draw results from a player being unable to make a legal move while not being in check.
 */
public class StalemateDrawResult extends DrawResult {
    @Override
    protected String getDrawTypeString() {
        return "stalemate";
    }
}
