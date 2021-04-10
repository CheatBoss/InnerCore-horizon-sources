package org.spongycastle.asn1.cryptopro;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class ECGOST3410ParamSetParameters extends ASN1Object
{
    ASN1Integer a;
    ASN1Integer b;
    ASN1Integer p;
    ASN1Integer q;
    ASN1Integer x;
    ASN1Integer y;
    
    public ECGOST3410ParamSetParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4, final int n, final BigInteger bigInteger5) {
        this.a = new ASN1Integer(bigInteger);
        this.b = new ASN1Integer(bigInteger2);
        this.p = new ASN1Integer(bigInteger3);
        this.q = new ASN1Integer(bigInteger4);
        this.x = new ASN1Integer(n);
        this.y = new ASN1Integer(bigInteger5);
    }
    
    public ECGOST3410ParamSetParameters(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.a = objects.nextElement();
        this.b = objects.nextElement();
        this.p = objects.nextElement();
        this.q = objects.nextElement();
        this.x = objects.nextElement();
        this.y = objects.nextElement();
    }
    
    public static ECGOST3410ParamSetParameters getInstance(final Object o) {
        if (o == null || o instanceof ECGOST3410ParamSetParameters) {
            return (ECGOST3410ParamSetParameters)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ECGOST3410ParamSetParameters((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid GOST3410Parameter: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static ECGOST3410ParamSetParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getA() {
        return this.a.getPositiveValue();
    }
    
    public BigInteger getP() {
        return this.p.getPositiveValue();
    }
    
    public BigInteger getQ() {
        return this.q.getPositiveValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.a);
        asn1EncodableVector.add(this.b);
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.q);
        asn1EncodableVector.add(this.x);
        asn1EncodableVector.add(this.y);
        return new DERSequence(asn1EncodableVector);
    }
}
