package com.appsflyer.internal.model.event;

import android.content.*;

public class ProxyEvent extends BackgroundEvent
{
    private String \u027e;
    
    public ProxyEvent() {
        super(null, null, Boolean.FALSE, null, null);
    }
    
    public BackgroundEvent body(final String \u027e) {
        this.\u027e = \u027e;
        return this;
    }
    
    @Override
    public String body() {
        return this.\u027e;
    }
}
