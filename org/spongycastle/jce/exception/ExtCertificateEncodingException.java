package org.spongycastle.jce.exception;

import java.security.cert.*;

public class ExtCertificateEncodingException extends CertificateEncodingException implements ExtException
{
    private Throwable cause;
    
    public ExtCertificateEncodingException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
