package org.spongycastle.util;

public class StreamParsingException extends Exception
{
    Throwable _e;
    
    public StreamParsingException(final String s, final Throwable e) {
        super(s);
        this._e = e;
    }
    
    @Override
    public Throwable getCause() {
        return this._e;
    }
}
