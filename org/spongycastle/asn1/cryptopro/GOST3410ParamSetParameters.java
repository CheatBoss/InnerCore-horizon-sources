package org.spongycastle.asn1.cryptopro;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class GOST3410ParamSetParameters extends ASN1Object
{
    ASN1Integer a;
    int keySize;
    ASN1Integer p;
    ASN1Integer q;
    
    public GOST3410ParamSetParameters(final int keySize, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        this.keySize = keySize;
        this.p = new ASN1Integer(bigInteger);
        this.q = new ASN1Integer(bigInteger2);
        this.a = new ASN1Integer(bigInteger3);
    }
    
    public GOST3410ParamSetParameters(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.keySize = objects.nextElement().getValue().intValue();
        this.p = objects.nextElement();
        this.q = objects.nextElement();
        this.a = objects.nextElement();
    }
    
    public static GOST3410ParamSetParameters getInstance(final Object o) {
        if (o == null || o instanceof GOST3410ParamSetParameters) {
            return (GOST3410ParamSetParameters)o;
        }
        if (o instanceof ASN1Sequence) {
            return new GOST3410ParamSetParameters((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid GOST3410Parameter: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static GOST3410ParamSetParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getA() {
        return this.a.getPositiveValue();
    }
    
    public int getKeySize() {
        return this.keySize;
    }
    
    public int getLKeySize() {
        return this.keySize;
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
        asn1EncodableVector.add(new ASN1Integer(this.keySize));
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.q);
        asn1EncodableVector.add(this.a);
        return new DERSequence(asn1EncodableVector);
    }
}
