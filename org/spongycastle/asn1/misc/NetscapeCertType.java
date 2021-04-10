package org.spongycastle.asn1.misc;

import org.spongycastle.asn1.*;

public class NetscapeCertType extends DERBitString
{
    public static final int objectSigning = 16;
    public static final int objectSigningCA = 1;
    public static final int reserved = 8;
    public static final int smime = 32;
    public static final int smimeCA = 2;
    public static final int sslCA = 4;
    public static final int sslClient = 128;
    public static final int sslServer = 64;
    
    public NetscapeCertType(final int n) {
        super(ASN1BitString.getBytes(n), ASN1BitString.getPadBits(n));
    }
    
    public NetscapeCertType(final DERBitString derBitString) {
        super(derBitString.getBytes(), derBitString.getPadBits());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("NetscapeCertType: 0x");
        sb.append(Integer.toHexString(this.data[0] & 0xFF));
        return sb.toString();
    }
}
