package com.appsflyer;

public interface AppsFlyerTrackingRequestListener
{
    void onTrackingRequestFailure(final String p0);
    
    void onTrackingRequestSuccess();
}
