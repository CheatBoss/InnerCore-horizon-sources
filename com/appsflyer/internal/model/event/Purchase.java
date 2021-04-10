package com.appsflyer.internal.model.event;

import android.content.*;

public abstract class Purchase extends BackgroundEvent
{
    Purchase() {
        this(null, null, null);
    }
    
    Purchase(final String s, final Boolean b, final Context context) {
        super(s, Boolean.FALSE, null, b, context);
    }
}
