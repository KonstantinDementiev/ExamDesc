package com.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtServiceAsync {

    void sendOriginalArray(boolean isIncreasingOrder, int[] originalNumbers, AsyncCallback<String> async);

    void getCurrentArray(AsyncCallback<int[][]> async);

    void gwtServer(int number, AsyncCallback<String> async);
}
