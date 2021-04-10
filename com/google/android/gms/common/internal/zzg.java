package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.*;
import com.google.android.gms.common.*;

final class zzg implements BaseOnConnectionFailedListener
{
    private final /* synthetic */ GoogleApiClient.OnConnectionFailedListener zzte;
    
    zzg(final GoogleApiClient.OnConnectionFailedListener zzte) {
        this.zzte = zzte;
    }
    
    @Override
    public final void onConnectionFailed(final ConnectionResult connectionResult) {
        this.zzte.onConnectionFailed(connectionResult);
    }
}
