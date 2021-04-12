package com.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("index")
public interface GwtService extends RemoteService {

    String gwtServer(int number) throws IllegalArgumentException;

    String sendOriginalArray(boolean isIncreasingOrder, int[] originalNumbers);

    int[][] getCurrentArray();
}
