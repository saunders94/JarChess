package com.example.jarchess.match.clock;

import com.example.jarchess.testmode.TestMode;
import com.example.jarchess.testmode.TestModeException;

public enum MatchClockChoice {
    CASUAL_MATCH_CLOCK,
    HIDDEN_CASUAL_MATCH_CLOCK,
    CLASSIC_FIDE_MATCH_CLOCK,
    TEST_MATCH_CLOCK;

    public MatchClock makeMatchClock() {
        switch (this) {

            case CASUAL_MATCH_CLOCK:
                return new CasualMatchClock();

            case HIDDEN_CASUAL_MATCH_CLOCK:
                return new HiddenCasualMatchClock();

            case CLASSIC_FIDE_MATCH_CLOCK:
                return new ClassicFIDEMatchClock();

            case TEST_MATCH_CLOCK:
                if (TestMode.isOff()) {
                    throw new TestModeException();
                }
                return new TestMatchClock();

            default:
                throw new IllegalStateException("Unexpected MatchClockChoice value: " + this);
        }
    }
}
