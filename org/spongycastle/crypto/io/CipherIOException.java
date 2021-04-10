package org.spongycastle.crypto.io;

import java.io.*;

public class CipherIOException extends IOException
{
    private static final long serialVersionUID = 1L;
    private final Throwable cause;
    
    public CipherIOException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
