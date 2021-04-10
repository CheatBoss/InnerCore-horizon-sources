package com.appsflyer.internal.model.event;

import android.content.*;
import com.appsflyer.*;

public class Validate extends Purchase
{
    public Validate(final Context context) {
        super("af_purchase", Boolean.TRUE, context);
    }
    
    @Override
    public AFEvent urlString(final String s) {
        return super.urlString(this.addChannel(s));
    }
}
