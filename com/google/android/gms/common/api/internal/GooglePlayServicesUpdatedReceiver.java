package com.google.android.gms.common.api.internal;

import android.content.*;
import android.net.*;

public final class GooglePlayServicesUpdatedReceiver extends BroadcastReceiver
{
    private Context mContext;
    private final Callback zzkt;
    
    public GooglePlayServicesUpdatedReceiver(final Callback zzkt) {
        this.zzkt = zzkt;
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        final Uri data = intent.getData();
        String schemeSpecificPart;
        if (data != null) {
            schemeSpecificPart = data.getSchemeSpecificPart();
        }
        else {
            schemeSpecificPart = null;
        }
        if ("com.google.android.gms".equals(schemeSpecificPart)) {
            this.zzkt.zzv();
            this.unregister();
        }
    }
    
    public final void unregister() {
        synchronized (this) {
            if (this.mContext != null) {
                this.mContext.unregisterReceiver((BroadcastReceiver)this);
            }
            this.mContext = null;
        }
    }
    
    public final void zzc(final Context mContext) {
        this.mContext = mContext;
    }
    
    public abstract static class Callback
    {
        public abstract void zzv();
    }
}
