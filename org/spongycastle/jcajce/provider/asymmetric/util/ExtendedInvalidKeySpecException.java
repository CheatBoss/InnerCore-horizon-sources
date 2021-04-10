package org.spongycastle.jcajce.provider.asymmetric.util;

import java.security.spec.*;

public class ExtendedInvalidKeySpecException extends InvalidKeySpecException
{
    private Throwable cause;
    
    public ExtendedInvalidKeySpecException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
