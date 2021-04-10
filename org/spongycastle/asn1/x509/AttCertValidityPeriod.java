package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class AttCertValidityPeriod extends ASN1Object
{
    ASN1GeneralizedTime notAfterTime;
    ASN1GeneralizedTime notBeforeTime;
    
    public AttCertValidityPeriod(final ASN1GeneralizedTime notBeforeTime, final ASN1GeneralizedTime notAfterTime) {
        this.notBeforeTime = notBeforeTime;
        this.notAfterTime = notAfterTime;
    }
    
    private AttCertValidityPeriod(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.notBeforeTime = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(0));
            this.notAfterTime = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static AttCertValidityPeriod getInstance(final Object o) {
        if (o instanceof AttCertValidityPeriod) {
            return (AttCertValidityPeriod)o;
        }
        if (o != null) {
            return new AttCertValidityPeriod(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1GeneralizedTime getNotAfterTime() {
        return this.notAfterTime;
    }
    
    public ASN1GeneralizedTime getNotBeforeTime() {
        return this.notBeforeTime;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.notBeforeTime);
        asn1EncodableVector.add(this.notAfterTime);
        return new DERSequence(asn1EncodableVector);
    }
}
