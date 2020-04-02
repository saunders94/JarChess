package com.example.jarchess.match.clock;

public class HiddenCasualMatchClock extends CasualMatchClock {
    @Override
    protected synchronized void tick() {
        // do nothing
    }
}
