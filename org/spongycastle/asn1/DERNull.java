package org.spongycastle.asn1;

import java.io.*;

public class DERNull extends ASN1Null
{
    public static final DERNull INSTANCE;
    private static final byte[] zeroBytes;
    
    static {
        INSTANCE = new DERNull();
        zeroBytes = new byte[0];
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(5, DERNull.zeroBytes);
    }
    
    @Override
    int encodedLength() {
        return 2;
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
}
