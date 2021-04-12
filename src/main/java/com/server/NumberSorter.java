package com.server;

public class NumberSorter extends Thread {

    private final boolean isIncreasingOrder;
    private final int[] sortingArray;
    private int[] iterationIndexes;
    private int[][] responseArray;
    private int timeout;

    public NumberSorter(boolean isIncreasingOrder, int[] sortingArray) {
        this.isIncreasingOrder = isIncreasingOrder;
        this.sortingArray = sortingArray;
    }

    @Override
    public void run() {
        iterationIndexes = new int[6];
        responseArray = new int[2][];
        quickSort(0, sortingArray.length - 1);
        iterationIndexes[0] = 1;
    }

    public synchronized int[][] getCurrentSortedArray(int delay) {
        timeout = delay;
        notify();
        responseArray[0] = sortingArray;
        responseArray[1] = iterationIndexes;
        return responseArray;
    }

    private void quickSort(int begin, int end) {
        if (begin < end) {
            try {
                iterationIndexes[3] = partition(begin, end);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iterationIndexes[4] = begin;
            iterationIndexes[5] = end;
            quickSort(begin, iterationIndexes[3] - 1);
            quickSort(iterationIndexes[3] + 1, end);
        }
    }

    private synchronized int partition(int begin, int end) throws InterruptedException {
        int pivot = sortingArray[end];
        iterationIndexes[1] = (begin - 1);
        for (int j = begin; j < end; j++) {
            if (getSortOrder(isIncreasingOrder, sortingArray[j], pivot)) {
                iterationIndexes[1]++;
                rotateElements(iterationIndexes[1], j);
            }
            iterationIndexes[2] = j;
            wait(timeout);
        }
        rotateElements(iterationIndexes[1] + 1, end);
        return iterationIndexes[1] + 1;
    }

    private boolean getSortOrder(boolean isIncreasingOrder, int a, int b) {
        return isIncreasingOrder ? a <= b : a >= b;
    }

    private void rotateElements(int i, int j) {
        int swapTemp = sortingArray[i];
        sortingArray[i] = sortingArray[j];
        sortingArray[j] = swapTemp;
    }


}
