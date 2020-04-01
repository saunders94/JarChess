package com.example.jarchess.match.result;

public class AgreedUponDrawResult extends DrawResult {
    @Override
    protected String getDrawTypeString() {
        return "agreed upon";
    }
}
