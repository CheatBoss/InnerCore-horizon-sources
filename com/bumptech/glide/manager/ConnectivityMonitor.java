package com.bumptech.glide.manager;

public interface ConnectivityMonitor extends LifecycleListener
{
    public interface ConnectivityListener
    {
        void onConnectivityChanged(final boolean p0);
    }
}
