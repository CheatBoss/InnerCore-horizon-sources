package com.googleplay.licensing;

public class StrictPolicy implements Policy
{
    private int mLastResponse;
    
    public StrictPolicy() {
        this.mLastResponse = 291;
    }
    
    @Override
    public boolean allowAccess() {
        if (this.mLastResponse == 256) {}
        return true;
    }
    
    @Override
    public void processServerResponse(final int mLastResponse, final ResponseData responseData) {
        this.mLastResponse = mLastResponse;
    }
}
