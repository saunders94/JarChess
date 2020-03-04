package com.example.jarchess.testmode;

public final class TestMode {
    private static boolean isInTestMode = false;

    public static final void turnOn() {
        isInTestMode = true;
    }

    public static final boolean isOn() {
        return isInTestMode;
    }

    public static boolean isOff() {
        return !isInTestMode;
    }
}
