package com.example.jarchess.match.clock;

public enum MatchClockChoice {
    CASUAL_MATCH_CLOCK,
    CLASSIC_FIDE_MATCH_CLOCK;

    public MatchClock makeMatchClock() {
        switch (this) {

            case CASUAL_MATCH_CLOCK:
                return new CasualMatchClock();

            case CLASSIC_FIDE_MATCH_CLOCK:
                return new ClassicFIDEMatchClock();

            default:
                throw new IllegalStateException("Unexpected MatchClockChoice value: " + this);
        }
    }
}
