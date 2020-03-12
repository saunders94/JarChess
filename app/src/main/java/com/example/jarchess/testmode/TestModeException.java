package com.example.jarchess.testmode;

//TODO javadocs
public class TestModeException extends RuntimeException {
    public TestModeException() {
        super("Test mode was activated and not expected. ");
    }

    public TestModeException(String msg) {
        super("Test mode was activated and not expected. " + msg);
    }
}
