package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class OriginatorInfo extends ASN1Object
{
    private ASN1Set certs;
    private ASN1Set crls;
    
    private OriginatorInfo(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        if (size != 0) {
            ASN1TaggedObject asn1TaggedObject;
            if (size != 1) {
                if (size != 2) {
                    throw new IllegalArgumentException("OriginatorInfo too big");
                }
                this.certs = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(0), false);
                asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(1);
            }
            else {
                asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(0);
                final int tagNo = asn1TaggedObject.getTagNo();
                if (tagNo == 0) {
                    this.certs = ASN1Set.getInstance(asn1TaggedObject, false);
                    return;
                }
                if (tagNo != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Bad tag in OriginatorInfo: ");
                    sb.append(asn1TaggedObject.getTagNo());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            this.crls = ASN1Set.getInstance(asn1TaggedObject, false);
        }
    }
    
    public OriginatorInfo(final ASN1Set certs, final ASN1Set crls) {
        this.certs = certs;
        this.crls = crls;
    }
    
    public static OriginatorInfo getInstance(final Object o) {
        if (o instanceof OriginatorInfo) {
            return (OriginatorInfo)o;
        }
        if (o != null) {
            return new OriginatorInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static OriginatorInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1Set getCRLs() {
        return this.crls;
    }
    
    public ASN1Set getCertificates() {
        return this.certs;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.certs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.certs));
        }
        if (this.crls != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
