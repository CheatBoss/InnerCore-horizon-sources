package com.microsoft.aad.adal;

class AuthenticationRequestState
{
    private final APIEvent mAPIEvent;
    private boolean mCancelled;
    private AuthenticationCallback<AuthenticationResult> mDelegate;
    private AuthenticationRequest mRequest;
    private int mRequestId;
    
    public AuthenticationRequestState(final int mRequestId, final AuthenticationRequest mRequest, final AuthenticationCallback<AuthenticationResult> mDelegate, final APIEvent mapiEvent) {
        this.mRequestId = 0;
        this.mDelegate = null;
        this.mCancelled = false;
        this.mRequest = null;
        this.mRequestId = mRequestId;
        this.mDelegate = mDelegate;
        this.mRequest = mRequest;
        this.mAPIEvent = mapiEvent;
    }
    
    APIEvent getAPIEvent() {
        return this.mAPIEvent;
    }
    
    public AuthenticationCallback<AuthenticationResult> getDelegate() {
        return this.mDelegate;
    }
    
    public AuthenticationRequest getRequest() {
        return this.mRequest;
    }
    
    public int getRequestId() {
        return this.mRequestId;
    }
    
    public boolean isCancelled() {
        return this.mCancelled;
    }
    
    public void setCancelled(final boolean mCancelled) {
        this.mCancelled = mCancelled;
    }
    
    public void setDelegate(final AuthenticationCallback<AuthenticationResult> mDelegate) {
        this.mDelegate = mDelegate;
    }
    
    public void setRequest(final AuthenticationRequest mRequest) {
        this.mRequest = mRequest;
    }
    
    public void setRequestId(final int mRequestId) {
        this.mRequestId = mRequestId;
    }
}
