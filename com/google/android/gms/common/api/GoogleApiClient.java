package com.google.android.gms.common.api;

import java.util.*;
import android.os.*;
import com.google.android.gms.common.*;

public abstract class GoogleApiClient
{
    private static final Set<GoogleApiClient> zzcu;
    
    static {
        zzcu = Collections.newSetFromMap(new WeakHashMap<GoogleApiClient, Boolean>());
    }
    
    public interface ConnectionCallbacks
    {
        void onConnected(final Bundle p0);
        
        void onConnectionSuspended(final int p0);
    }
    
    public interface OnConnectionFailedListener
    {
        void onConnectionFailed(final ConnectionResult p0);
    }
}
