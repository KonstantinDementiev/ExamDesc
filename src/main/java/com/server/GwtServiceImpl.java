package com.server;

import com.client.GwtService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtServiceImpl extends RemoteServiceServlet implements GwtService {

    private final static int MAX_TIMEOUT = 30000;
    private NumberSorter numberSorter;

    @Override
    public String gwtServer(int number) throws IllegalArgumentException {
        return "Number is correct!";
    }

    @Override
    public String sendOriginalArray(boolean isIncreasingOrder, int[] originalNumbers) {
        numberSorter = new NumberSorter(isIncreasingOrder, originalNumbers);
        numberSorter.setPriority(1);
        numberSorter.start();
        return "Sorting started!";
    }
    @Override
    public int[][] getCurrentArray() {
        return numberSorter.getCurrentSortedArray(MAX_TIMEOUT);
    }


}
