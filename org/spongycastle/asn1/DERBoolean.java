package org.spongycastle.asn1;

public class DERBoolean extends ASN1Boolean
{
    public DERBoolean(final boolean b) {
        super(b);
    }
    
    DERBoolean(final byte[] array) {
        super(array);
    }
}
