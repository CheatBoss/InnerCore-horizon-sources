package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class RevokedInfo extends ASN1Object
{
    private CRLReason revocationReason;
    private ASN1GeneralizedTime revocationTime;
    
    public RevokedInfo(final ASN1GeneralizedTime revocationTime, final CRLReason revocationReason) {
        this.revocationTime = revocationTime;
        this.revocationReason = revocationReason;
    }
    
    private RevokedInfo(final ASN1Sequence asn1Sequence) {
        this.revocationTime = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.revocationReason = CRLReason.getInstance(ASN1Enumerated.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true));
        }
    }
    
    public static RevokedInfo getInstance(final Object o) {
        if (o instanceof RevokedInfo) {
            return (RevokedInfo)o;
        }
        if (o != null) {
            return new RevokedInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static RevokedInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public CRLReason getRevocationReason() {
        return this.revocationReason;
    }
    
    public ASN1GeneralizedTime getRevocationTime() {
        return this.revocationTime;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.revocationTime);
        if (this.revocationReason != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.revocationReason));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
