package com.shared;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class FieldVerifierTest {

    private final static int MIN_COUNT = 1;
    private final static int MAX_COUNT = 100;
    private final static int DEFAULT_TIME_DELAY = 500;

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void when_count_input_null_then_throw_exception() {
        FieldVerifier.getNumberCount(null);
    }

    @Test
    public void when_count_input_is_not_numeric_then_throw_exception() {
        assertEquals(FieldVerifier.getNumberCount("something"), -1);
    }

    @Test
    public void when_count_input_is_float_then_throw_exception() {
        assertEquals(FieldVerifier.getNumberCount("15.6"), -1);
    }

    @Test
    public void isNumberValid_test() {
        assertTrue(FieldVerifier.isCountInvalid(-1));
        assertTrue(FieldVerifier.isCountInvalid(MIN_COUNT - 1));
        assertTrue(FieldVerifier.isCountInvalid(MAX_COUNT + 1));
        assertFalse(FieldVerifier.isCountInvalid(MIN_COUNT));
        assertFalse(FieldVerifier.isCountInvalid(MAX_COUNT));
    }

    @Test
    public void when_thread_delay_input_null_then_return_default() {
        assertEquals(FieldVerifier.getThreadDelay(null), DEFAULT_TIME_DELAY);
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void when_thread_delay_input_is_not_numeric_then_throw_exception() {
        FieldVerifier.getThreadDelay("something");
    }

}
