package org.spongycastle.asn1.oiw;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class ElGamalParameter extends ASN1Object
{
    ASN1Integer g;
    ASN1Integer p;
    
    public ElGamalParameter(final BigInteger bigInteger, final BigInteger bigInteger2) {
        this.p = new ASN1Integer(bigInteger);
        this.g = new ASN1Integer(bigInteger2);
    }
    
    private ElGamalParameter(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.p = objects.nextElement();
        this.g = objects.nextElement();
    }
    
    public static ElGamalParameter getInstance(final Object o) {
        if (o instanceof ElGamalParameter) {
            return (ElGamalParameter)o;
        }
        if (o != null) {
            return new ElGamalParameter(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BigInteger getG() {
        return this.g.getPositiveValue();
    }
    
    public BigInteger getP() {
        return this.p.getPositiveValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.g);
        return new DERSequence(asn1EncodableVector);
    }
}
