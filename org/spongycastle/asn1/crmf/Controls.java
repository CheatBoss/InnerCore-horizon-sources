package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.*;

public class Controls extends ASN1Object
{
    private ASN1Sequence content;
    
    private Controls(final ASN1Sequence content) {
        this.content = content;
    }
    
    public Controls(final AttributeTypeAndValue attributeTypeAndValue) {
        this.content = new DERSequence(attributeTypeAndValue);
    }
    
    public Controls(final AttributeTypeAndValue[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.content = new DERSequence(asn1EncodableVector);
    }
    
    public static Controls getInstance(final Object o) {
        if (o instanceof Controls) {
            return (Controls)o;
        }
        if (o != null) {
            return new Controls(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
    
    public AttributeTypeAndValue[] toAttributeTypeAndValueArray() {
        final int size = this.content.size();
        final AttributeTypeAndValue[] array = new AttributeTypeAndValue[size];
        for (int i = 0; i != size; ++i) {
            array[i] = AttributeTypeAndValue.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
}
