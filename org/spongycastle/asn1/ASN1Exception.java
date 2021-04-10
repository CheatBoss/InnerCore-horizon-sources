package org.spongycastle.asn1;

import java.io.*;

public class ASN1Exception extends IOException
{
    private Throwable cause;
    
    ASN1Exception(final String s) {
        super(s);
    }
    
    ASN1Exception(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
