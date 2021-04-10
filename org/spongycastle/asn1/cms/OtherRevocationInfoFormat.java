package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class OtherRevocationInfoFormat extends ASN1Object
{
    private ASN1Encodable otherRevInfo;
    private ASN1ObjectIdentifier otherRevInfoFormat;
    
    public OtherRevocationInfoFormat(final ASN1ObjectIdentifier otherRevInfoFormat, final ASN1Encodable otherRevInfo) {
        this.otherRevInfoFormat = otherRevInfoFormat;
        this.otherRevInfo = otherRevInfo;
    }
    
    private OtherRevocationInfoFormat(final ASN1Sequence asn1Sequence) {
        this.otherRevInfoFormat = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.otherRevInfo = asn1Sequence.getObjectAt(1);
    }
    
    public static OtherRevocationInfoFormat getInstance(final Object o) {
        if (o instanceof OtherRevocationInfoFormat) {
            return (OtherRevocationInfoFormat)o;
        }
        if (o != null) {
            return new OtherRevocationInfoFormat(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static OtherRevocationInfoFormat getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1Encodable getInfo() {
        return this.otherRevInfo;
    }
    
    public ASN1ObjectIdentifier getInfoFormat() {
        return this.otherRevInfoFormat;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.otherRevInfoFormat);
        asn1EncodableVector.add(this.otherRevInfo);
        return new DERSequence(asn1EncodableVector);
    }
}
