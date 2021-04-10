package org.spongycastle.asn1;

import java.io.*;

public abstract class ASN1Generator
{
    protected OutputStream _out;
    
    public ASN1Generator(final OutputStream out) {
        this._out = out;
    }
    
    public abstract OutputStream getRawOutputStream();
}
