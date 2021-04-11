package com.shared;

import java.util.Random;

public class NumberGenerator {

    public static int[] generateNumbers(int numberCount) {
        int minValue = 1;
        int minLimit = 30;
        int maxValue = 1000;
        int[] result = new int[numberCount];
        boolean isAllValuesOverThanLimit = true;
        Random random = new Random();
        do {
            for (int i = 0; i < numberCount; i++) {
                result[i] = random.nextInt(maxValue - minValue) + minValue;
                isAllValuesOverThanLimit = isAllValuesOverThanLimit && result[i] > minLimit;
            }
        }
        while (isAllValuesOverThanLimit);
        return result;
    }

}
