package com.example.jarchess.match.clock;

public enum MatchClockChoice {
    CASUAL_MATCH_CLOCK,
    CLASSIC_FIDE_MATCH_CLOCK;

    public MatchClock makeMatchClock(MatchClockObserver observer) {
        switch (this) {

            case CASUAL_MATCH_CLOCK:
                return new CasualMatchClock(observer);

            case CLASSIC_FIDE_MATCH_CLOCK:
                return new ClassicFIDEMatchClock(observer);

            default:
                throw new IllegalStateException("Unexpected MatchClockChoice value: " + this);
        }
    }
}
