package com.bumptech.glide.manager;

import android.content.*;

public class ConnectivityMonitorFactory
{
    public ConnectivityMonitor build(final Context context, final ConnectivityMonitor.ConnectivityListener connectivityListener) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
            return new DefaultConnectivityMonitor(context, connectivityListener);
        }
        return new NullConnectivityMonitor();
    }
}
