package org.spongycastle.jce.exception;

import java.security.cert.*;

public class ExtCertPathValidatorException extends CertPathValidatorException implements ExtException
{
    private Throwable cause;
    
    public ExtCertPathValidatorException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    public ExtCertPathValidatorException(final String s, final Throwable cause, final CertPath certPath, final int n) {
        super(s, cause, certPath, n);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
