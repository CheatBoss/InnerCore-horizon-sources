package org.spongycastle.asn1.x9;

import java.math.*;
import org.spongycastle.asn1.*;

public class DHPublicKey extends ASN1Object
{
    private ASN1Integer y;
    
    public DHPublicKey(final BigInteger bigInteger) {
        if (bigInteger != null) {
            this.y = new ASN1Integer(bigInteger);
            return;
        }
        throw new IllegalArgumentException("'y' cannot be null");
    }
    
    private DHPublicKey(final ASN1Integer y) {
        if (y != null) {
            this.y = y;
            return;
        }
        throw new IllegalArgumentException("'y' cannot be null");
    }
    
    public static DHPublicKey getInstance(final Object o) {
        if (o == null || o instanceof DHPublicKey) {
            return (DHPublicKey)o;
        }
        if (o instanceof ASN1Integer) {
            return new DHPublicKey((ASN1Integer)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid DHPublicKey: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static DHPublicKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Integer.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getY() {
        return this.y.getPositiveValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.y;
    }
}
