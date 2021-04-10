package org.spongycastle.asn1;

import java.math.*;

public class DEREnumerated extends ASN1Enumerated
{
    public DEREnumerated(final int n) {
        super(n);
    }
    
    public DEREnumerated(final BigInteger bigInteger) {
        super(bigInteger);
    }
    
    DEREnumerated(final byte[] array) {
        super(array);
    }
}
