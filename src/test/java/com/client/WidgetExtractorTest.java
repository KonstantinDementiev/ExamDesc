package com.client;

import org.testng.annotations.Test;

public class WidgetExtractorTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void when_input_null_then_throw_exception() {
        WidgetExtractor.getWidget(null);
    }

}
