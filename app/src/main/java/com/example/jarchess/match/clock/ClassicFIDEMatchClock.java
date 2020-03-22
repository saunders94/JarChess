package com.example.jarchess.match.clock;

class ClassicFIDEMatchClock extends IncrementMatchClock {
    public ClassicFIDEMatchClock(MatchClockObserver observer) {
        super(new int[]{30}, new int[]{1}, new int[]{90, 30}, new int[]{0, 40}, observer);

    }

}
