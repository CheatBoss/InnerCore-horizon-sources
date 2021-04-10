package org.spongycastle.x509;

import java.security.cert.*;

class ExtCertificateEncodingException extends CertificateEncodingException
{
    Throwable cause;
    
    ExtCertificateEncodingException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
