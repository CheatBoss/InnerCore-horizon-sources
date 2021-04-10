package org.spongycastle.asn1.x509;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class RSAPublicKeyStructure extends ASN1Object
{
    private BigInteger modulus;
    private BigInteger publicExponent;
    
    public RSAPublicKeyStructure(final BigInteger modulus, final BigInteger publicExponent) {
        this.modulus = modulus;
        this.publicExponent = publicExponent;
    }
    
    public RSAPublicKeyStructure(final ASN1Sequence asn1Sequence) {
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
    
    public static RSAPublicKeyStructure getInstance(final Object o) {
        if (o == null || o instanceof RSAPublicKeyStructure) {
            return (RSAPublicKeyStructure)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RSAPublicKeyStructure((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid RSAPublicKeyStructure: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static RSAPublicKeyStructure getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
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
