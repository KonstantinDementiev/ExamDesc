package com.shared;

public class FieldVerifier {

    private final static int MIN_NUMBER = 1;
    private final static int MAX_NUMBER = 100;

    public static int getNumberCount(String number) {
        if (number == null) {
            throw new IllegalArgumentException("Input is null.");
        }

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isNumberInvalid(int number) {
        return number < MIN_NUMBER || number > MAX_NUMBER;
    }


}
