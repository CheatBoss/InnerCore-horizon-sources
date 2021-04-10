package com.appsflyer.internal.model.event;

import com.appsflyer.*;
import android.content.*;

public class AdRevenue extends AFEvent
{
    public AdRevenue() {
        super(null, Boolean.FALSE, null);
    }
    
    @Override
    public AFEvent urlString(final String s) {
        return super.urlString(this.addChannel(s));
    }
}
