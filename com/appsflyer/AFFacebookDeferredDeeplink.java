package com.appsflyer;

final class AFFacebookDeferredDeeplink
{
    public interface AppLinkFetchEvents
    {
        void onAppLinkFetchFailed(final String p0);
        
        void onAppLinkFetchFinished(final String p0, final String p1, final String p2);
    }
}
