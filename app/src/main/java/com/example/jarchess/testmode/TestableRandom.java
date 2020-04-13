package com.example.jarchess.testmode;

import java.util.Random;

public class TestableRandom {

    private static TestableRandom instance;
    private final Random random;
    private boolean nextBoolean;
    private boolean nextBooleanHasBeenSet = false;

    private TestableRandom() {
        random = new Random();
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static TestableRandom getInstance() {
        if (instance == null) {
            instance = new TestableRandom();
        }

        return instance;
    }

    public boolean getNextBoolean() {
        if (nextBooleanHasBeenSet) {
            return nextBoolean;
        } else {
            return random.nextBoolean();
        }
    }

    public void setNextBoolean(boolean nextBoolean) {
        if (TestMode.isOff()) {
            throw new TestModeException("Cannot set next boolean on testable random unless test mode is on.");
        } else {
            this.nextBoolean = nextBoolean;
            this.nextBooleanHasBeenSet = true;
        }
    }
}
