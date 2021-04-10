package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class Signature extends ASN1Object
{
    ASN1Sequence certs;
    DERBitString signature;
    AlgorithmIdentifier signatureAlgorithm;
    
    private Signature(final ASN1Sequence asn1Sequence) {
        this.signatureAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.signature = (DERBitString)asn1Sequence.getObjectAt(1);
        if (asn1Sequence.size() == 3) {
            this.certs = ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(2), true);
        }
    }
    
    public Signature(final AlgorithmIdentifier signatureAlgorithm, final DERBitString signature) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.signature = signature;
    }
    
    public Signature(final AlgorithmIdentifier signatureAlgorithm, final DERBitString signature, final ASN1Sequence certs) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.signature = signature;
        this.certs = certs;
    }
    
    public static Signature getInstance(final Object o) {
        if (o instanceof Signature) {
            return (Signature)o;
        }
        if (o != null) {
            return new Signature(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static Signature getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1Sequence getCerts() {
        return this.certs;
    }
    
    public DERBitString getSignature() {
        return this.signature;
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.signatureAlgorithm);
        asn1EncodableVector.add(this.signature);
        if (this.certs != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.certs));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
