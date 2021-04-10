package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class BasicOCSPResponse extends ASN1Object
{
    private ASN1Sequence certs;
    private DERBitString signature;
    private AlgorithmIdentifier signatureAlgorithm;
    private ResponseData tbsResponseData;
    
    private BasicOCSPResponse(final ASN1Sequence asn1Sequence) {
        this.tbsResponseData = ResponseData.getInstance(asn1Sequence.getObjectAt(0));
        this.signatureAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.signature = (DERBitString)asn1Sequence.getObjectAt(2);
        if (asn1Sequence.size() > 3) {
            this.certs = ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(3), true);
        }
    }
    
    public BasicOCSPResponse(final ResponseData tbsResponseData, final AlgorithmIdentifier signatureAlgorithm, final DERBitString signature, final ASN1Sequence certs) {
        this.tbsResponseData = tbsResponseData;
        this.signatureAlgorithm = signatureAlgorithm;
        this.signature = signature;
        this.certs = certs;
    }
    
    public static BasicOCSPResponse getInstance(final Object o) {
        if (o instanceof BasicOCSPResponse) {
            return (BasicOCSPResponse)o;
        }
        if (o != null) {
            return new BasicOCSPResponse(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static BasicOCSPResponse getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
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
    
    public ResponseData getTbsResponseData() {
        return this.tbsResponseData;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.tbsResponseData);
        asn1EncodableVector.add(this.signatureAlgorithm);
        asn1EncodableVector.add(this.signature);
        if (this.certs != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.certs));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
