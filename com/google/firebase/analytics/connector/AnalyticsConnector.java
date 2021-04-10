package com.google.firebase.analytics.connector;

import android.os.*;

public interface AnalyticsConnector
{
    void logEvent(final String p0, final String p1, final Bundle p2);
    
    void setUserProperty(final String p0, final String p1, final Object p2);
}
