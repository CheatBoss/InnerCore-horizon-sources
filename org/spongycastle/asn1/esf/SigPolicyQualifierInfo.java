package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.*;

public class SigPolicyQualifierInfo extends ASN1Object
{
    private ASN1ObjectIdentifier sigPolicyQualifierId;
    private ASN1Encodable sigQualifier;
    
    public SigPolicyQualifierInfo(final ASN1ObjectIdentifier sigPolicyQualifierId, final ASN1Encodable sigQualifier) {
        this.sigPolicyQualifierId = sigPolicyQualifierId;
        this.sigQualifier = sigQualifier;
    }
    
    private SigPolicyQualifierInfo(final ASN1Sequence asn1Sequence) {
        this.sigPolicyQualifierId = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.sigQualifier = asn1Sequence.getObjectAt(1);
    }
    
    public static SigPolicyQualifierInfo getInstance(final Object o) {
        if (o instanceof SigPolicyQualifierInfo) {
            return (SigPolicyQualifierInfo)o;
        }
        if (o != null) {
            return new SigPolicyQualifierInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getSigPolicyQualifierId() {
        return new ASN1ObjectIdentifier(this.sigPolicyQualifierId.getId());
    }
    
    public ASN1Encodable getSigQualifier() {
        return this.sigQualifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.sigPolicyQualifierId);
        asn1EncodableVector.add(this.sigQualifier);
        return new DERSequence(asn1EncodableVector);
    }
}
