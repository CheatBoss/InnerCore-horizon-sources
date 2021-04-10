package com.google.firebase.analytics.connector;

import com.google.firebase.events.*;

final class zzb implements EventHandler
{
    static final EventHandler zzbsj;
    
    static {
        zzbsj = new zzb();
    }
    
    private zzb() {
    }
    
    @Override
    public final void handle(final Event event) {
        AnalyticsConnectorImpl.zza(event);
    }
}
