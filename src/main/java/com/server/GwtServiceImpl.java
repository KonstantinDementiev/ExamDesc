package com.server;

import com.client.GwtService;
import com.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GwtServiceImpl extends RemoteServiceServlet implements GwtService {

    public String gwtServer(String input) throws IllegalArgumentException {

        if (FieldVerifier.isNumberInvalid(FieldVerifier.getNumberCount(input))) {
            throw new IllegalArgumentException("Number must be from 1 to 1000");
        }
        return "Number is valid.";
    }

}
