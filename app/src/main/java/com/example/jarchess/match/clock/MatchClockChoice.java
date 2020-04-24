package com.example.jarchess.match.clock;

public enum MatchClockChoice {
    CASUAL_MATCH_CLOCK(0),
    HIDDEN_CASUAL_MATCH_CLOCK(1),
    CLASSIC_FIDE_MATCH_CLOCK(2);
    private static int constructorIndex = 0;
    private final int intValue;

    MatchClockChoice(int i) {
        intValue = i;
    }

    public static MatchClockChoice getFromIntValue(int i) {
        return values()[i];
    }

    public int getIntValue() {
        return intValue;
    }

    public MatchClock makeMatchClock() {
        switch (this) {

            case CASUAL_MATCH_CLOCK:
                return new CasualMatchClock();

            case HIDDEN_CASUAL_MATCH_CLOCK:
                return new HiddenCasualMatchClock();

            case CLASSIC_FIDE_MATCH_CLOCK:
                return new ClassicFIDEMatchClock();

            default:
                throw new IllegalStateException("Unexpected MatchClockChoice value: " + this);
        }
    }

    @Override
    public String toString() {
        return super.toString().replaceAll("_", " ");
    }
}
