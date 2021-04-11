package com.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class WidgetExtractor {

    public static IsWidget getWidget(com.google.gwt.dom.client.Element element) {
        EventListener listener = DOM.getEventListener(element);
        if (listener == null) {
            throw new IllegalArgumentException();
        }
        if (listener instanceof Widget) {
            return (Widget) listener;
        }
        throw new IllegalArgumentException();
    }

}
