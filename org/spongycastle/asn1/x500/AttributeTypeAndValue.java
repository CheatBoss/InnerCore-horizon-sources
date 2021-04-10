package org.spongycastle.asn1.x500;

import org.spongycastle.asn1.*;

public class AttributeTypeAndValue extends ASN1Object
{
    private ASN1ObjectIdentifier type;
    private ASN1Encodable value;
    
    public AttributeTypeAndValue(final ASN1ObjectIdentifier type, final ASN1Encodable value) {
        this.type = type;
        this.value = value;
    }
    
    private AttributeTypeAndValue(final ASN1Sequence asn1Sequence) {
        this.type = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.value = asn1Sequence.getObjectAt(1);
    }
    
    public static AttributeTypeAndValue getInstance(final Object o) {
        if (o instanceof AttributeTypeAndValue) {
            return (AttributeTypeAndValue)o;
        }
        if (o != null) {
            return new AttributeTypeAndValue(ASN1Sequence.getInstance(o));
        }
        throw new IllegalArgumentException("null value in getInstance()");
    }
    
    public ASN1ObjectIdentifier getType() {
        return this.type;
    }
    
    public ASN1Encodable getValue() {
        return this.value;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.type);
        asn1EncodableVector.add(this.value);
        return new DERSequence(asn1EncodableVector);
    }
}
