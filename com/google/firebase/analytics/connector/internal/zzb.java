package com.google.firebase.analytics.connector.internal;

import com.google.firebase.components.*;
import com.google.firebase.*;
import android.content.*;
import com.google.firebase.events.*;
import com.google.firebase.analytics.connector.*;

final class zzb implements ComponentFactory
{
    static final ComponentFactory zzbsl;
    
    static {
        zzbsl = new zzb();
    }
    
    private zzb() {
    }
    
    @Override
    public final Object create(final ComponentContainer componentContainer) {
        return AnalyticsConnectorImpl.getInstance(componentContainer.get(FirebaseApp.class), componentContainer.get(Context.class), componentContainer.get(Subscriber.class));
    }
}
