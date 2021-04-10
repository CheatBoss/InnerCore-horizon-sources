package org.spongycastle.jcajce.provider.util;

import javax.crypto.*;

public class BadBlockException extends BadPaddingException
{
    private final Throwable cause;
    
    public BadBlockException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
