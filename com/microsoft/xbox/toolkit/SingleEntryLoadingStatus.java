package com.microsoft.xbox.toolkit;

public class SingleEntryLoadingStatus
{
    private boolean isLoading;
    private XLEException lastError;
    private Object syncObj;
    
    public SingleEntryLoadingStatus() {
        this.isLoading = false;
        this.lastError = null;
        this.syncObj = new Object();
    }
    
    private void setDone(final XLEException lastError) {
        synchronized (this.syncObj) {
            this.isLoading = false;
            this.lastError = lastError;
            this.syncObj.notifyAll();
        }
    }
    
    public boolean getIsLoading() {
        return this.isLoading;
    }
    
    public XLEException getLastError() {
        return this.lastError;
    }
    
    public void reset() {
        synchronized (this.syncObj) {
            this.isLoading = false;
            this.lastError = null;
            this.syncObj.notifyAll();
        }
    }
    
    public void setFailed(final XLEException done) {
        this.setDone(done);
    }
    
    public void setSuccess() {
        this.setDone(null);
    }
    
    public WaitResult waitForNotLoading() {
        synchronized (this.syncObj) {
            if (this.isLoading) {
                try {
                    this.syncObj.wait();
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                return new WaitResult(true, this.lastError);
            }
            this.isLoading = true;
            return new WaitResult(false, null);
        }
    }
    
    public class WaitResult
    {
        public XLEException error;
        public boolean waited;
        
        public WaitResult(final boolean waited, final XLEException error) {
            this.waited = waited;
            this.error = error;
        }
    }
}
