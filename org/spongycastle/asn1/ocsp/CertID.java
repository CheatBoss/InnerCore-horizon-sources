package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class CertID extends ASN1Object
{
    AlgorithmIdentifier hashAlgorithm;
    ASN1OctetString issuerKeyHash;
    ASN1OctetString issuerNameHash;
    ASN1Integer serialNumber;
    
    private CertID(final ASN1Sequence asn1Sequence) {
        this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.issuerNameHash = (ASN1OctetString)asn1Sequence.getObjectAt(1);
        this.issuerKeyHash = (ASN1OctetString)asn1Sequence.getObjectAt(2);
        this.serialNumber = (ASN1Integer)asn1Sequence.getObjectAt(3);
    }
    
    public CertID(final AlgorithmIdentifier hashAlgorithm, final ASN1OctetString issuerNameHash, final ASN1OctetString issuerKeyHash, final ASN1Integer serialNumber) {
        this.hashAlgorithm = hashAlgorithm;
        this.issuerNameHash = issuerNameHash;
        this.issuerKeyHash = issuerKeyHash;
        this.serialNumber = serialNumber;
    }
    
    public static CertID getInstance(final Object o) {
        if (o instanceof CertID) {
            return (CertID)o;
        }
        if (o != null) {
            return new CertID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static CertID getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public ASN1OctetString getIssuerKeyHash() {
        return this.issuerKeyHash;
    }
    
    public ASN1OctetString getIssuerNameHash() {
        return this.issuerNameHash;
    }
    
    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.hashAlgorithm);
        asn1EncodableVector.add(this.issuerNameHash);
        asn1EncodableVector.add(this.issuerKeyHash);
        asn1EncodableVector.add(this.serialNumber);
        return new DERSequence(asn1EncodableVector);
    }
}
