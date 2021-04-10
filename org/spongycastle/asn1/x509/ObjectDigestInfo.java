package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class ObjectDigestInfo extends ASN1Object
{
    public static final int otherObjectDigest = 2;
    public static final int publicKey = 0;
    public static final int publicKeyCert = 1;
    AlgorithmIdentifier digestAlgorithm;
    ASN1Enumerated digestedObjectType;
    DERBitString objectDigest;
    ASN1ObjectIdentifier otherObjectTypeID;
    
    public ObjectDigestInfo(final int n, final ASN1ObjectIdentifier otherObjectTypeID, final AlgorithmIdentifier digestAlgorithm, final byte[] array) {
        this.digestedObjectType = new ASN1Enumerated(n);
        if (n == 2) {
            this.otherObjectTypeID = otherObjectTypeID;
        }
        this.digestAlgorithm = digestAlgorithm;
        this.objectDigest = new DERBitString(array);
    }
    
    private ObjectDigestInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() <= 4 && asn1Sequence.size() >= 3) {
            int n = 0;
            this.digestedObjectType = ASN1Enumerated.getInstance(asn1Sequence.getObjectAt(0));
            if (asn1Sequence.size() == 4) {
                this.otherObjectTypeID = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(1));
                n = 1;
            }
            this.digestAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n + 1));
            this.objectDigest = DERBitString.getInstance(asn1Sequence.getObjectAt(n + 2));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static ObjectDigestInfo getInstance(final Object o) {
        if (o instanceof ObjectDigestInfo) {
            return (ObjectDigestInfo)o;
        }
        if (o != null) {
            return new ObjectDigestInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static ObjectDigestInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }
    
    public ASN1Enumerated getDigestedObjectType() {
        return this.digestedObjectType;
    }
    
    public DERBitString getObjectDigest() {
        return this.objectDigest;
    }
    
    public ASN1ObjectIdentifier getOtherObjectTypeID() {
        return this.otherObjectTypeID;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.digestedObjectType);
        final ASN1ObjectIdentifier otherObjectTypeID = this.otherObjectTypeID;
        if (otherObjectTypeID != null) {
            asn1EncodableVector.add(otherObjectTypeID);
        }
        asn1EncodableVector.add(this.digestAlgorithm);
        asn1EncodableVector.add(this.objectDigest);
        return new DERSequence(asn1EncodableVector);
    }
}
