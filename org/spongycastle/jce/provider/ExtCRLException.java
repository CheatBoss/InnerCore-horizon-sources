package org.spongycastle.jce.provider;

import java.security.cert.*;

class ExtCRLException extends CRLException
{
    Throwable cause;
    
    ExtCRLException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
