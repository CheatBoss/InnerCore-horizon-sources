package com.microsoft.aad.adal;

import android.content.*;
import android.net.*;
import android.os.*;

class DefaultConnectionService implements IConnectionService
{
    private static final String TAG = "DefaultConnectionService";
    private final Context mConnectionContext;
    
    DefaultConnectionService(final Context mConnectionContext) {
        this.mConnectionContext = mConnectionContext;
    }
    
    @Override
    public boolean isConnectionAvailable() {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)this.mConnectionContext.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting() && !this.isNetworkDisabledFromOptimizations();
    }
    
    public boolean isNetworkDisabledFromOptimizations() {
        if (Build$VERSION.SDK_INT >= 23) {
            String s;
            if (UsageStatsManagerWrapper.getInstance().isAppInactive(this.mConnectionContext)) {
                s = "Client app is inactive. Network is disabled.";
            }
            else {
                final PowerManagerWrapper instance = PowerManagerWrapper.getInstance();
                if (!instance.isDeviceIdleMode(this.mConnectionContext) || instance.isIgnoringBatteryOptimizations(this.mConnectionContext)) {
                    return false;
                }
                s = "Device is dozing. Network is disabled.";
            }
            Logger.w("DefaultConnectionService", s, "", ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION);
            return true;
        }
        return false;
    }
}
