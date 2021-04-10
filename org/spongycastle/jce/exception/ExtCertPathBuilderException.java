package org.spongycastle.jce.exception;

import java.security.cert.*;

public class ExtCertPathBuilderException extends CertPathBuilderException implements ExtException
{
    private Throwable cause;
    
    public ExtCertPathBuilderException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    public ExtCertPathBuilderException(final String s, final Throwable cause, final CertPath certPath, final int n) {
        super(s, cause);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
