package org.spongycastle.util;

public class StoreException extends RuntimeException
{
    private Throwable _e;
    
    public StoreException(final String s, final Throwable e) {
        super(s);
        this._e = e;
    }
    
    @Override
    public Throwable getCause() {
        return this._e;
    }
}
