package com.example.jarchess.match.clock;

class ClassicFIDEMatchClock extends IncrementMatchClock {

    public static final long TOLERANCE_MILLIS = IGNORE_TOLERANCE;//previously 3000L;

    public ClassicFIDEMatchClock() {
        super(new int[]{30}, new int[]{1}, new int[]{90, 30}, new int[]{0, 40}, TOLERANCE_MILLIS);

    }

}
