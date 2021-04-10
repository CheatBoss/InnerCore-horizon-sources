package org.spongycastle.asn1.pkcs;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class DHParameter extends ASN1Object
{
    ASN1Integer g;
    ASN1Integer l;
    ASN1Integer p;
    
    public DHParameter(final BigInteger bigInteger, final BigInteger bigInteger2, final int n) {
        this.p = new ASN1Integer(bigInteger);
        this.g = new ASN1Integer(bigInteger2);
        ASN1Integer l;
        if (n != 0) {
            l = new ASN1Integer(n);
        }
        else {
            l = null;
        }
        this.l = l;
    }
    
    private DHParameter(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.p = ASN1Integer.getInstance(objects.nextElement());
        this.g = ASN1Integer.getInstance(objects.nextElement());
        ASN1Integer l;
        if (objects.hasMoreElements()) {
            l = objects.nextElement();
        }
        else {
            l = null;
        }
        this.l = l;
    }
    
    public static DHParameter getInstance(final Object o) {
        if (o instanceof DHParameter) {
            return (DHParameter)o;
        }
        if (o != null) {
            return new DHParameter(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BigInteger getG() {
        return this.g.getPositiveValue();
    }
    
    public BigInteger getL() {
        final ASN1Integer l = this.l;
        if (l == null) {
            return null;
        }
        return l.getPositiveValue();
    }
    
    public BigInteger getP() {
        return this.p.getPositiveValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.g);
        if (this.getL() != null) {
            asn1EncodableVector.add(this.l);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
