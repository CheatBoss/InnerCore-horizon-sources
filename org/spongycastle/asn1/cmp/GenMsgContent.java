package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class GenMsgContent extends ASN1Object
{
    private ASN1Sequence content;
    
    private GenMsgContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public GenMsgContent(final InfoTypeAndValue infoTypeAndValue) {
        this.content = new DERSequence(infoTypeAndValue);
    }
    
    public GenMsgContent(final InfoTypeAndValue[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.content = new DERSequence(asn1EncodableVector);
    }
    
    public static GenMsgContent getInstance(final Object o) {
        if (o instanceof GenMsgContent) {
            return (GenMsgContent)o;
        }
        if (o != null) {
            return new GenMsgContent(ASN1Sequence.getInstance(o));
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
