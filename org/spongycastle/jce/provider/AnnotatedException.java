package org.spongycastle.jce.provider;

import org.spongycastle.jce.exception.*;

public class AnnotatedException extends Exception implements ExtException
{
    private Throwable _underlyingException;
    
    public AnnotatedException(final String s) {
        this(s, null);
    }
    
    public AnnotatedException(final String s, final Throwable underlyingException) {
        super(s);
        this._underlyingException = underlyingException;
    }
    
    @Override
    public Throwable getCause() {
        return this._underlyingException;
    }
    
    Throwable getUnderlyingException() {
        return this._underlyingException;
    }
}
