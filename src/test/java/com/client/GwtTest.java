package com.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.shared.FieldVerifier;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class GwtTest {

    @Test
    public void testFieldVerifier() {
        assertTrue(FieldVerifier.isNumberInvalid(FieldVerifier.getNumberCount("")));
        assertTrue(FieldVerifier.isNumberInvalid(FieldVerifier.getNumberCount("-1")));
        assertTrue(FieldVerifier.isNumberInvalid(FieldVerifier.getNumberCount("0")));
        assertTrue(FieldVerifier.isNumberInvalid(FieldVerifier.getNumberCount("1001")));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFieldVerifierForNull() {
        FieldVerifier.getNumberCount(null);
    }



}
