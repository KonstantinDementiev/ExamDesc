package com.server;

public class NumberSorter extends Thread {

    private final boolean isIncreasingOrder;
    private final int[] sortingArray;
    private int[] iterationIndexes;
    private int[][] responseArray;

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

    public synchronized int[][] getCurrentSortedArray() {
        notify();
        responseArray[0] = sortingArray;
        responseArray[1] = iterationIndexes;
        return responseArray;
    }

    private void quickSort(int begin, int end) {
        if (begin < end) {
            int partitionIndex = 0;
            try {
                partitionIndex = partition(begin, end);
                iterationIndexes[3] = partitionIndex;
                iterationIndexes[4] = begin;
                iterationIndexes[5] = end;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            quickSort(begin, partitionIndex - 1);
            quickSort(partitionIndex + 1, end);
        }
    }

    private synchronized int partition(int begin, int end) throws InterruptedException {
        int pivot = sortingArray[end];
        int i = (begin - 1);
        iterationIndexes[1] = i;
        for (int j = begin; j < end; j++) {
            if (getSortOrder(isIncreasingOrder, sortingArray[j], pivot)) {
                i++;
                iterationIndexes[1] = i;
                rotateElements(i, j);
            }
            iterationIndexes[2] = j;
            wait();
        }
        rotateElements(i + 1, end);
        return i + 1;
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
