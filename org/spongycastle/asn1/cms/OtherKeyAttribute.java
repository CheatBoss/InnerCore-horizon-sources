package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class OtherKeyAttribute extends ASN1Object
{
    private ASN1Encodable keyAttr;
    private ASN1ObjectIdentifier keyAttrId;
    
    public OtherKeyAttribute(final ASN1ObjectIdentifier keyAttrId, final ASN1Encodable keyAttr) {
        this.keyAttrId = keyAttrId;
        this.keyAttr = keyAttr;
    }
    
    public OtherKeyAttribute(final ASN1Sequence asn1Sequence) {
        this.keyAttrId = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.keyAttr = asn1Sequence.getObjectAt(1);
    }
    
    public static OtherKeyAttribute getInstance(final Object o) {
        if (o instanceof OtherKeyAttribute) {
            return (OtherKeyAttribute)o;
        }
        if (o != null) {
            return new OtherKeyAttribute(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Encodable getKeyAttr() {
        return this.keyAttr;
    }
    
    public ASN1ObjectIdentifier getKeyAttrId() {
        return this.keyAttrId;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.keyAttrId);
        asn1EncodableVector.add(this.keyAttr);
        return new DERSequence(asn1EncodableVector);
    }
}
