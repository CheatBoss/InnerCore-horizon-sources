package com.android.installreferrer.api;

public interface InstallReferrerStateListener
{
    void onInstallReferrerServiceDisconnected();
    
    void onInstallReferrerSetupFinished(final int p0);
}
