package org.spongycastle.asn1.ocsp;

import java.util.*;
import org.spongycastle.asn1.*;

public class CrlID extends ASN1Object
{
    private ASN1Integer crlNum;
    private ASN1GeneralizedTime crlTime;
    private DERIA5String crlUrl;
    
    private CrlID(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            final int tagNo = asn1TaggedObject.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unknown tag number: ");
                        sb.append(asn1TaggedObject.getTagNo());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    this.crlTime = ASN1GeneralizedTime.getInstance(asn1TaggedObject, true);
                }
                else {
                    this.crlNum = ASN1Integer.getInstance(asn1TaggedObject, true);
                }
            }
            else {
                this.crlUrl = DERIA5String.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public static CrlID getInstance(final Object o) {
        if (o instanceof CrlID) {
            return (CrlID)o;
        }
        if (o != null) {
            return new CrlID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer getCrlNum() {
        return this.crlNum;
    }
    
    public ASN1GeneralizedTime getCrlTime() {
        return this.crlTime;
    }
    
    public DERIA5String getCrlUrl() {
        return this.crlUrl;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.crlUrl != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.crlUrl));
        }
        if (this.crlNum != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.crlNum));
        }
        if (this.crlTime != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.crlTime));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
