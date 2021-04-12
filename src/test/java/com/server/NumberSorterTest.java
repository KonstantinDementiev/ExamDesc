package com.server;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

public class NumberSorterTest {

    private NumberSorter numberSorter;
    private int[] originalArray;
    private int[] expectedArrayUp;
    private int[] expectedArrayDown;

    @BeforeMethod
    public void setUp() {
        originalArray = new int[]{5, 3, 1, 4, 2};
        expectedArrayUp = new int[]{1, 2, 3, 4, 5};
        expectedArrayDown = new int[]{5, 4, 3, 2, 1};
    }

    @Test
    public void sorting_in_increasing_order_is_success() throws InterruptedException {
        int[] actualArray = Arrays.copyOf(originalArray, originalArray.length);
        numberSorter = new NumberSorter(true, actualArray);
        numberSorter.start();
        Thread.sleep(5);
        int[][] responseArray = numberSorter.getCurrentSortedArray(1);
        numberSorter.join();
        assertEquals(responseArray[0], expectedArrayUp);
    }

    @Test
    public void sorting_in_decreasing_order_is_success() throws InterruptedException {
        int[] actualArray = Arrays.copyOf(originalArray, originalArray.length);
        numberSorter = new NumberSorter(false, actualArray);
        numberSorter.start();
        Thread.sleep(5);
        int[][] responseArray = numberSorter.getCurrentSortedArray(1);
        numberSorter.join();
        assertEquals(responseArray[0], expectedArrayDown);
    }

}
