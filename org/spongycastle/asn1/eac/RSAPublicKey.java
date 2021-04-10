package org.spongycastle.asn1.eac;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class RSAPublicKey extends PublicKeyDataObject
{
    private static int exponentValid = 2;
    private static int modulusValid = 1;
    private BigInteger exponent;
    private BigInteger modulus;
    private ASN1ObjectIdentifier usage;
    private int valid;
    
    public RSAPublicKey(final ASN1ObjectIdentifier usage, final BigInteger modulus, final BigInteger exponent) {
        this.valid = 0;
        this.usage = usage;
        this.modulus = modulus;
        this.exponent = exponent;
    }
    
    RSAPublicKey(final ASN1Sequence asn1Sequence) {
        this.valid = 0;
        final Enumeration objects = asn1Sequence.getObjects();
        this.usage = ASN1ObjectIdentifier.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final UnsignedInteger instance = UnsignedInteger.getInstance(objects.nextElement());
            final int tagNo = instance.getTagNo();
            if (tagNo != 1) {
                if (tagNo != 2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown DERTaggedObject :");
                    sb.append(instance.getTagNo());
                    sb.append("-> not an Iso7816RSAPublicKeyStructure");
                    throw new IllegalArgumentException(sb.toString());
                }
                this.setExponent(instance);
            }
            else {
                this.setModulus(instance);
            }
        }
        if (this.valid == 3) {
            return;
        }
        throw new IllegalArgumentException("missing argument -> not an Iso7816RSAPublicKeyStructure");
    }
    
    private void setExponent(final UnsignedInteger unsignedInteger) {
        final int valid = this.valid;
        final int exponentValid = RSAPublicKey.exponentValid;
        if ((valid & exponentValid) == 0x0) {
            this.valid = (valid | exponentValid);
            this.exponent = unsignedInteger.getValue();
            return;
        }
        throw new IllegalArgumentException("Exponent already set");
    }
    
    private void setModulus(final UnsignedInteger unsignedInteger) {
        final int valid = this.valid;
        final int modulusValid = RSAPublicKey.modulusValid;
        if ((valid & modulusValid) == 0x0) {
            this.valid = (valid | modulusValid);
            this.modulus = unsignedInteger.getValue();
            return;
        }
        throw new IllegalArgumentException("Modulus already set");
    }
    
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    public BigInteger getPublicExponent() {
        return this.exponent;
    }
    
    @Override
    public ASN1ObjectIdentifier getUsage() {
        return this.usage;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.usage);
        asn1EncodableVector.add(new UnsignedInteger(1, this.getModulus()));
        asn1EncodableVector.add(new UnsignedInteger(2, this.getPublicExponent()));
        return new DERSequence(asn1EncodableVector);
    }
}
