package org.spongycastle.crypto.params;

import org.spongycastle.asn1.*;
import org.spongycastle.math.ec.*;
import java.math.*;

public class ECNamedDomainParameters extends ECDomainParameters
{
    private ASN1ObjectIdentifier name;
    
    public ECNamedDomainParameters(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger) {
        this(asn1ObjectIdentifier, ecCurve, ecPoint, bigInteger, null, null);
    }
    
    public ECNamedDomainParameters(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2) {
        this(asn1ObjectIdentifier, ecCurve, ecPoint, bigInteger, bigInteger2, null);
    }
    
    public ECNamedDomainParameters(final ASN1ObjectIdentifier name, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2, final byte[] array) {
        super(ecCurve, ecPoint, bigInteger, bigInteger2, array);
        this.name = name;
    }
    
    public ASN1ObjectIdentifier getName() {
        return this.name;
    }
}
