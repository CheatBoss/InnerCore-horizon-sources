package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.*;
import android.os.*;

final class zzf implements BaseConnectionCallbacks
{
    private final /* synthetic */ GoogleApiClient.ConnectionCallbacks zztd;
    
    zzf(final GoogleApiClient.ConnectionCallbacks zztd) {
        this.zztd = zztd;
    }
    
    @Override
    public final void onConnected(final Bundle bundle) {
        this.zztd.onConnected(bundle);
    }
    
    @Override
    public final void onConnectionSuspended(final int n) {
        this.zztd.onConnectionSuspended(n);
    }
}
