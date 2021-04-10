package com.appsflyer.internal.model.event;

import android.content.*;
import com.appsflyer.*;
import java.util.*;

public abstract class BackgroundEvent extends AFEvent
{
    private final boolean \u026a;
    private final boolean \u027e;
    private boolean \u04cf;
    
    BackgroundEvent() {
        this(null, null, null, null, null);
    }
    
    BackgroundEvent(final String s, final Boolean b, final Boolean b2, final Boolean b3, final Context context) {
        super(s, b3 != null && b3, context);
        final boolean b4 = true;
        this.\u026a = (b == null || b);
        boolean booleanValue = b4;
        if (b2 != null) {
            booleanValue = b2;
        }
        this.\u027e = booleanValue;
    }
    
    public String body() {
        return AFHelper.convertToJsonObject(this.params()).toString();
    }
    
    public boolean proxyMode() {
        return this.\u027e;
    }
    
    public boolean readResponse() {
        return this.\u026a;
    }
    
    public BackgroundEvent trackingStopped(final boolean \u04cf) {
        this.\u04cf = \u04cf;
        return this;
    }
    
    public boolean trackingStopped() {
        return this.\u04cf;
    }
}
