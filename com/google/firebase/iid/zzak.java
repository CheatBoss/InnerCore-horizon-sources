package com.google.firebase.iid;

public final class zzak extends Exception
{
    private final int errorCode;
    
    public zzak(final int errorCode, final String s) {
        super(s);
        this.errorCode = errorCode;
    }
    
    public final int getErrorCode() {
        return this.errorCode;
    }
}
