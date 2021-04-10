package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class PolicyQualifierInfo extends ASN1Object
{
    private ASN1ObjectIdentifier policyQualifierId;
    private ASN1Encodable qualifier;
    
    public PolicyQualifierInfo(final String s) {
        this.policyQualifierId = PolicyQualifierId.id_qt_cps;
        this.qualifier = new DERIA5String(s);
    }
    
    public PolicyQualifierInfo(final ASN1ObjectIdentifier policyQualifierId, final ASN1Encodable qualifier) {
        this.policyQualifierId = policyQualifierId;
        this.qualifier = qualifier;
    }
    
    public PolicyQualifierInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.policyQualifierId = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.qualifier = asn1Sequence.getObjectAt(1);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static PolicyQualifierInfo getInstance(final Object o) {
        if (o instanceof PolicyQualifierInfo) {
            return (PolicyQualifierInfo)o;
        }
        if (o != null) {
            return new PolicyQualifierInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getPolicyQualifierId() {
        return this.policyQualifierId;
    }
    
    public ASN1Encodable getQualifier() {
        return this.qualifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.policyQualifierId);
        asn1EncodableVector.add(this.qualifier);
        return new DERSequence(asn1EncodableVector);
    }
}
