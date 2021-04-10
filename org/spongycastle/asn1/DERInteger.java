package org.spongycastle.asn1;

import java.math.*;

public class DERInteger extends ASN1Integer
{
    public DERInteger(final long n) {
        super(n);
    }
    
    public DERInteger(final BigInteger bigInteger) {
        super(bigInteger);
    }
    
    public DERInteger(final byte[] array) {
        super(array, true);
    }
}
