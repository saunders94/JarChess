package com.example.jarchess.testmode;


import java.util.LinkedList;
import java.util.Queue;

//TODO javadocs
public class TestableCurrentTime {
    private static Queue<Long> returnValues = new LinkedList<Long>();
    private static boolean hasBeenSet = false;

    /**
     * Sets the return value of currentTimeMillis
     *
     * @param rValue the value to return
     * @throws TestModeException if test mode is not enabled.
     */
    public static void setReturnValue(long rValue) throws TestModeException {
        if (TestMode.isOff()) {
            throw new TestModeException("Tried to set the return value of testable current time. ");
        } else {
            returnValues.add(rValue);
            hasBeenSet = true;
        }
    }

    /**
     * gets the current time as the milliseconds since midnight, January 1, 1970 UTC
     *
     * @return the current time as the milliseconds since midnight, January 1, 1970 UTC
     */
    public static long currentTimeMillis() {
        if (hasBeenSet) {
            return returnValues.remove();
        }

        return System.currentTimeMillis();
    }
}
