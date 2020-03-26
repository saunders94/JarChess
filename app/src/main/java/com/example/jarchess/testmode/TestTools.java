package com.example.jarchess.testmode;

import java.math.BigInteger;

public class TestTools {
    public static void printMagnitudeBinary(BigInteger bigInteger) {
        for (byte b : bigInteger.toByteArray()) {
            for (int i = 7; i >= 0; i--) {
                System.out.print(1 & (b >> i));
            }
            System.out.print(" ");
        }
        System.out.println();
    }

    public static void printlnMagnitudeBinary(BigInteger bigInteger) {
        printMagnitudeBinary(bigInteger);
        System.out.println();
    }

    public static void toMagnitudeBinaryString(BigInteger bigInteger) {
        StringBuilder s = new StringBuilder();
        for (byte b : bigInteger.toByteArray()) {
            for (int i = 7; i >= 0; i--) {
                s.append(1 & (b >> i));
            }
            s.append(" ");
        }
    }
}
