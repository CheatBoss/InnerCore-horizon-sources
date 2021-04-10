package org.spongycastle.jce.exception;

import java.io.*;

public class ExtIOException extends IOException implements ExtException
{
    private Throwable cause;
    
    public ExtIOException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
