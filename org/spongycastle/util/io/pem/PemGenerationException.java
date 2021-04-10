package org.spongycastle.util.io.pem;

import java.io.*;

public class PemGenerationException extends IOException
{
    private Throwable cause;
    
    public PemGenerationException(final String s) {
        super(s);
    }
    
    public PemGenerationException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
