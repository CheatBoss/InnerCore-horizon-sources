package org.spongycastle.asn1.sec;

import java.math.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class ECPrivateKey extends ASN1Object
{
    private ASN1Sequence seq;
    
    public ECPrivateKey(final int n, final BigInteger bigInteger) {
        final byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray((n + 7) / 8, bigInteger);
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(1L));
        asn1EncodableVector.add(new DEROctetString(unsignedByteArray));
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    public ECPrivateKey(final int n, final BigInteger bigInteger, final ASN1Encodable asn1Encodable) {
        this(n, bigInteger, null, asn1Encodable);
    }
    
    public ECPrivateKey(final int n, final BigInteger bigInteger, final DERBitString derBitString, final ASN1Encodable asn1Encodable) {
        final byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray((n + 7) / 8, bigInteger);
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(1L));
        asn1EncodableVector.add(new DEROctetString(unsignedByteArray));
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, asn1Encodable));
        }
        if (derBitString != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, derBitString));
        }
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    public ECPrivateKey(final BigInteger bigInteger) {
        this(bigInteger.bitLength(), bigInteger);
    }
    
    public ECPrivateKey(final BigInteger bigInteger, final ASN1Encodable asn1Encodable) {
        this(bigInteger, null, asn1Encodable);
    }
    
    public ECPrivateKey(final BigInteger bigInteger, final DERBitString derBitString, final ASN1Encodable asn1Encodable) {
        this(bigInteger.bitLength(), bigInteger, derBitString, asn1Encodable);
    }
    
    private ECPrivateKey(final ASN1Sequence seq) {
        this.seq = seq;
    }
    
    public static ECPrivateKey getInstance(final Object o) {
        if (o instanceof ECPrivateKey) {
            return (ECPrivateKey)o;
        }
        if (o != null) {
            return new ECPrivateKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    private ASN1Primitive getObjectInTag(final int n) {
        final Enumeration objects = this.seq.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1Encodable asn1Encodable = objects.nextElement();
            if (asn1Encodable instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Encodable;
                if (asn1TaggedObject.getTagNo() == n) {
                    return asn1TaggedObject.getObject().toASN1Primitive();
                }
                continue;
            }
        }
        return null;
    }
    
    public BigInteger getKey() {
        return new BigInteger(1, ((ASN1OctetString)this.seq.getObjectAt(1)).getOctets());
    }
    
    public ASN1Primitive getParameters() {
        return this.getObjectInTag(0);
    }
    
    public DERBitString getPublicKey() {
        return (DERBitString)this.getObjectInTag(1);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
