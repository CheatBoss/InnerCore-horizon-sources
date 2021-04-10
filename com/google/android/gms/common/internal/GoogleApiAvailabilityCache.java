package com.google.android.gms.common.internal;

import android.util.*;
import com.google.android.gms.common.*;
import android.content.*;
import com.google.android.gms.common.api.*;

public class GoogleApiAvailabilityCache
{
    private final SparseIntArray zzug;
    private GoogleApiAvailabilityLight zzuh;
    
    public GoogleApiAvailabilityCache() {
        this(GoogleApiAvailability.getInstance());
    }
    
    public GoogleApiAvailabilityCache(final GoogleApiAvailabilityLight zzuh) {
        this.zzug = new SparseIntArray();
        Preconditions.checkNotNull(zzuh);
        this.zzuh = zzuh;
    }
    
    public void flush() {
        this.zzug.clear();
    }
    
    public int getClientAvailability(final Context context, final Api.Client client) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(client);
        if (!client.requiresGooglePlayServices()) {
            return 0;
        }
        final int minApkVersion = client.getMinApkVersion();
        final int value = this.zzug.get(minApkVersion, -1);
        if (value != -1) {
            return value;
        }
        int n = 0;
        int n2;
        while (true) {
            n2 = value;
            if (n >= this.zzug.size()) {
                break;
            }
            final int key = this.zzug.keyAt(n);
            if (key > minApkVersion && this.zzug.get(key) == 0) {
                n2 = 0;
                break;
            }
            ++n;
        }
        int googlePlayServicesAvailable;
        if ((googlePlayServicesAvailable = n2) == -1) {
            googlePlayServicesAvailable = this.zzuh.isGooglePlayServicesAvailable(context, minApkVersion);
        }
        this.zzug.put(minApkVersion, googlePlayServicesAvailable);
        return googlePlayServicesAvailable;
    }
}
