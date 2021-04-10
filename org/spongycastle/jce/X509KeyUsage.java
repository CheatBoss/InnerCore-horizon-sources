package org.spongycastle.jce;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;

public class X509KeyUsage extends ASN1Object
{
    public static final int cRLSign = 2;
    public static final int dataEncipherment = 16;
    public static final int decipherOnly = 32768;
    public static final int digitalSignature = 128;
    public static final int encipherOnly = 1;
    public static final int keyAgreement = 8;
    public static final int keyCertSign = 4;
    public static final int keyEncipherment = 32;
    public static final int nonRepudiation = 64;
    private int usage;
    
    public X509KeyUsage(final int usage) {
        this.usage = 0;
        this.usage = usage;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new KeyUsage(this.usage).toASN1Primitive();
    }
}
