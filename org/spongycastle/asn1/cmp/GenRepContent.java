package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class GenRepContent extends ASN1Object
{
    private ASN1Sequence content;
    
    private GenRepContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public GenRepContent(final InfoTypeAndValue infoTypeAndValue) {
        this.content = new DERSequence(infoTypeAndValue);
    }
    
    public GenRepContent(final InfoTypeAndValue[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.content = new DERSequence(asn1EncodableVector);
    }
    
    public static GenRepContent getInstance(final Object o) {
        if (o instanceof GenRepContent) {
            return (GenRepContent)o;
        }
        if (o != null) {
            return new GenRepContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
    
    public InfoTypeAndValue[] toInfoTypeAndValueArray() {
        final int size = this.content.size();
        final InfoTypeAndValue[] array = new InfoTypeAndValue[size];
        for (int i = 0; i != size; ++i) {
            array[i] = InfoTypeAndValue.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
}
