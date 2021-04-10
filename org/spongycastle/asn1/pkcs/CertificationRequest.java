package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class CertificationRequest extends ASN1Object
{
    protected CertificationRequestInfo reqInfo;
    protected AlgorithmIdentifier sigAlgId;
    protected DERBitString sigBits;
    
    protected CertificationRequest() {
        this.reqInfo = null;
        this.sigAlgId = null;
        this.sigBits = null;
    }
    
    public CertificationRequest(final ASN1Sequence asn1Sequence) {
        this.reqInfo = null;
        this.sigAlgId = null;
        this.sigBits = null;
        this.reqInfo = CertificationRequestInfo.getInstance(asn1Sequence.getObjectAt(0));
        this.sigAlgId = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.sigBits = (DERBitString)asn1Sequence.getObjectAt(2);
    }
    
    public CertificationRequest(final CertificationRequestInfo reqInfo, final AlgorithmIdentifier sigAlgId, final DERBitString sigBits) {
        this.reqInfo = null;
        this.sigAlgId = null;
        this.sigBits = null;
        this.reqInfo = reqInfo;
        this.sigAlgId = sigAlgId;
        this.sigBits = sigBits;
    }
    
    public static CertificationRequest getInstance(final Object o) {
        if (o instanceof CertificationRequest) {
            return (CertificationRequest)o;
        }
        if (o != null) {
            return new CertificationRequest(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CertificationRequestInfo getCertificationRequestInfo() {
        return this.reqInfo;
    }
    
    public DERBitString getSignature() {
        return this.sigBits;
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.sigAlgId;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.reqInfo);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(this.sigBits);
        return new DERSequence(asn1EncodableVector);
    }
}
