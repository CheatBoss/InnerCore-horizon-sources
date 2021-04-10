package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.ocsp.*;
import org.spongycastle.asn1.*;

public class OcspIdentifier extends ASN1Object
{
    private ResponderID ocspResponderID;
    private ASN1GeneralizedTime producedAt;
    
    private OcspIdentifier(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.ocspResponderID = ResponderID.getInstance(asn1Sequence.getObjectAt(0));
            this.producedAt = (ASN1GeneralizedTime)asn1Sequence.getObjectAt(1);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public OcspIdentifier(final ResponderID ocspResponderID, final ASN1GeneralizedTime producedAt) {
        this.ocspResponderID = ocspResponderID;
        this.producedAt = producedAt;
    }
    
    public static OcspIdentifier getInstance(final Object o) {
        if (o instanceof OcspIdentifier) {
            return (OcspIdentifier)o;
        }
        if (o != null) {
            return new OcspIdentifier(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ResponderID getOcspResponderID() {
        return this.ocspResponderID;
    }
    
    public ASN1GeneralizedTime getProducedAt() {
        return this.producedAt;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.ocspResponderID);
        asn1EncodableVector.add(this.producedAt);
        return new DERSequence(asn1EncodableVector);
    }
}
