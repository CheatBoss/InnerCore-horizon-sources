package com.bumptech.glide.manager;

import android.net.*;
import android.content.*;

class DefaultConnectivityMonitor implements ConnectivityMonitor
{
    private final BroadcastReceiver connectivityReceiver;
    private final Context context;
    private boolean isConnected;
    private boolean isRegistered;
    private final ConnectivityListener listener;
    
    public DefaultConnectivityMonitor(final Context context, final ConnectivityListener listener) {
        this.connectivityReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final boolean access$000 = DefaultConnectivityMonitor.this.isConnected;
                DefaultConnectivityMonitor.this.isConnected = DefaultConnectivityMonitor.this.isConnected(context);
                if (access$000 != DefaultConnectivityMonitor.this.isConnected) {
                    DefaultConnectivityMonitor.this.listener.onConnectivityChanged(DefaultConnectivityMonitor.this.isConnected);
                }
            }
        };
        this.context = context.getApplicationContext();
        this.listener = listener;
    }
    
    private boolean isConnected(final Context context) {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    private void register() {
        if (this.isRegistered) {
            return;
        }
        this.isConnected = this.isConnected(this.context);
        this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.isRegistered = true;
    }
    
    private void unregister() {
        if (!this.isRegistered) {
            return;
        }
        this.context.unregisterReceiver(this.connectivityReceiver);
        this.isRegistered = false;
    }
    
    @Override
    public void onDestroy() {
    }
    
    @Override
    public void onStart() {
        this.register();
    }
    
    @Override
    public void onStop() {
        this.unregister();
    }
}
