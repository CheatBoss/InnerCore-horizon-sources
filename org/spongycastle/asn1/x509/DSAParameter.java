package org.spongycastle.asn1.x509;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class DSAParameter extends ASN1Object
{
    ASN1Integer g;
    ASN1Integer p;
    ASN1Integer q;
    
    public DSAParameter(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        this.p = new ASN1Integer(bigInteger);
        this.q = new ASN1Integer(bigInteger2);
        this.g = new ASN1Integer(bigInteger3);
    }
    
    private DSAParameter(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            final Enumeration objects = asn1Sequence.getObjects();
            this.p = ASN1Integer.getInstance(objects.nextElement());
            this.q = ASN1Integer.getInstance(objects.nextElement());
            this.g = ASN1Integer.getInstance(objects.nextElement());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static DSAParameter getInstance(final Object o) {
        if (o instanceof DSAParameter) {
            return (DSAParameter)o;
        }
        if (o != null) {
            return new DSAParameter(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static DSAParameter getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getG() {
        return this.g.getPositiveValue();
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
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.q);
        asn1EncodableVector.add(this.g);
        return new DERSequence(asn1EncodableVector);
    }
}
