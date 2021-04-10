package com.microsoft.xbox.toolkit.network;

import com.microsoft.xbox.toolkit.*;

public abstract class IDataLoaderRunnable<T>
{
    protected int retryCountOnTokenError;
    
    public IDataLoaderRunnable() {
        this.retryCountOnTokenError = 1;
    }
    
    public abstract T buildData() throws XLEException;
    
    public abstract long getDefaultErrorCode();
    
    public int getShouldRetryCountOnTokenError() {
        return this.retryCountOnTokenError;
    }
    
    public Object getUserObject() {
        return null;
    }
    
    public abstract void onPostExcute(final AsyncResult<T> p0);
    
    public abstract void onPreExecute();
}
