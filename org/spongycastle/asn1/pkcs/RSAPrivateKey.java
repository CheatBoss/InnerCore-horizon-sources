package org.spongycastle.asn1.pkcs;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class RSAPrivateKey extends ASN1Object
{
    private BigInteger coefficient;
    private BigInteger exponent1;
    private BigInteger exponent2;
    private BigInteger modulus;
    private ASN1Sequence otherPrimeInfos;
    private BigInteger prime1;
    private BigInteger prime2;
    private BigInteger privateExponent;
    private BigInteger publicExponent;
    private BigInteger version;
    
    public RSAPrivateKey(final BigInteger modulus, final BigInteger publicExponent, final BigInteger privateExponent, final BigInteger prime1, final BigInteger prime2, final BigInteger exponent1, final BigInteger exponent2, final BigInteger coefficient) {
        this.otherPrimeInfos = null;
        this.version = BigInteger.valueOf(0L);
        this.modulus = modulus;
        this.publicExponent = publicExponent;
        this.privateExponent = privateExponent;
        this.prime1 = prime1;
        this.prime2 = prime2;
        this.exponent1 = exponent1;
        this.exponent2 = exponent2;
        this.coefficient = coefficient;
    }
    
    private RSAPrivateKey(final ASN1Sequence asn1Sequence) {
        this.otherPrimeInfos = null;
        final Enumeration objects = asn1Sequence.getObjects();
        final BigInteger value = objects.nextElement().getValue();
        if (value.intValue() != 0 && value.intValue() != 1) {
            throw new IllegalArgumentException("wrong version for RSA private key");
        }
        this.version = value;
        this.modulus = objects.nextElement().getValue();
        this.publicExponent = objects.nextElement().getValue();
        this.privateExponent = objects.nextElement().getValue();
        this.prime1 = objects.nextElement().getValue();
        this.prime2 = objects.nextElement().getValue();
        this.exponent1 = objects.nextElement().getValue();
        this.exponent2 = objects.nextElement().getValue();
        this.coefficient = objects.nextElement().getValue();
        if (objects.hasMoreElements()) {
            this.otherPrimeInfos = (ASN1Sequence)objects.nextElement();
        }
    }
    
    public static RSAPrivateKey getInstance(final Object o) {
        if (o instanceof RSAPrivateKey) {
            return (RSAPrivateKey)o;
        }
        if (o != null) {
            return new RSAPrivateKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static RSAPrivateKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getCoefficient() {
        return this.coefficient;
    }
    
    public BigInteger getExponent1() {
        return this.exponent1;
    }
    
    public BigInteger getExponent2() {
        return this.exponent2;
    }
    
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    public BigInteger getPrime1() {
        return this.prime1;
    }
    
    public BigInteger getPrime2() {
        return this.prime2;
    }
    
    public BigInteger getPrivateExponent() {
        return this.privateExponent;
    }
    
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    public BigInteger getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(this.version));
        asn1EncodableVector.add(new ASN1Integer(this.getModulus()));
        asn1EncodableVector.add(new ASN1Integer(this.getPublicExponent()));
        asn1EncodableVector.add(new ASN1Integer(this.getPrivateExponent()));
        asn1EncodableVector.add(new ASN1Integer(this.getPrime1()));
        asn1EncodableVector.add(new ASN1Integer(this.getPrime2()));
        asn1EncodableVector.add(new ASN1Integer(this.getExponent1()));
        asn1EncodableVector.add(new ASN1Integer(this.getExponent2()));
        asn1EncodableVector.add(new ASN1Integer(this.getCoefficient()));
        final ASN1Sequence otherPrimeInfos = this.otherPrimeInfos;
        if (otherPrimeInfos != null) {
            asn1EncodableVector.add(otherPrimeInfos);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
