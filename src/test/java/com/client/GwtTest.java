package com.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.gwtJUnit";
    }

    public void testGwtService() {
        GwtServiceAsync gwtService = GWT.create(GwtService.class);
        ServiceDefTarget target = (ServiceDefTarget) gwtService;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwt/index");
        delayTestFinish(10000);

        gwtService.gwtServer(50, new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
                fail("Request failure: " + caught.getMessage());
            }

            public void onSuccess(String result) {
                assertTrue(result.startsWith("Number is correct!"));
                finishTest();
            }
        });
    }

}
