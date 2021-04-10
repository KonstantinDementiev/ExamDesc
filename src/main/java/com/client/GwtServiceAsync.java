package com.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtServiceAsync {
  void gwtServer(String input, AsyncCallback<String> callback)
      throws IllegalArgumentException;
}
