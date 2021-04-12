package com.shared;

public class FieldVerifier {

    private final static int MIN_COUNT = 1;
    private final static int MAX_COUNT = 100;
    private final static int DEFAULT_THREAD_SLEEP = 500;

    public static int getNumberCount(String number) {
        if (number == null) {
            throw new IllegalArgumentException("Input is null.");
        }
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean isCountInvalid(int count) {
        return count < MIN_COUNT || count > MAX_COUNT;
    }

    public static int getThreadDelay(String number) {
        if (number == null || number.isEmpty()) {
            return DEFAULT_THREAD_SLEEP;
        }
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Input is incorrect.");
        }
    }

}
