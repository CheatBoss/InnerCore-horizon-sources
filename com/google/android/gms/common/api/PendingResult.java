package com.google.android.gms.common.api;

public abstract class PendingResult<R extends Result>
{
    public interface StatusListener
    {
        void onComplete(final Status p0);
    }
}
