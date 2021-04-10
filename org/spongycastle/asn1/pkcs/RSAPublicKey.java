package org.spongycastle.asn1.pkcs;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class RSAPublicKey extends ASN1Object
{
    private BigInteger modulus;
    private BigInteger publicExponent;
    
    public RSAPublicKey(final BigInteger modulus, final BigInteger publicExponent) {
        this.modulus = modulus;
        this.publicExponent = publicExponent;
    }
    
    private RSAPublicKey(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            final Enumeration objects = asn1Sequence.getObjects();
            this.modulus = ASN1Integer.getInstance(objects.nextElement()).getPositiveValue();
            this.publicExponent = ASN1Integer.getInstance(objects.nextElement()).getPositiveValue();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static RSAPublicKey getInstance(final Object o) {
        if (o instanceof RSAPublicKey) {
            return (RSAPublicKey)o;
        }
        if (o != null) {
            return new RSAPublicKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static RSAPublicKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(this.getModulus()));
        asn1EncodableVector.add(new ASN1Integer(this.getPublicExponent()));
        return new DERSequence(asn1EncodableVector);
    }
}
