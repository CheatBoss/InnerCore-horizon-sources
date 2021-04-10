package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class PKIMessages extends ASN1Object
{
    private ASN1Sequence content;
    
    private PKIMessages(final ASN1Sequence content) {
        this.content = content;
    }
    
    public PKIMessages(final PKIMessage pkiMessage) {
        this.content = new DERSequence(pkiMessage);
    }
    
    public PKIMessages(final PKIMessage[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.content = new DERSequence(asn1EncodableVector);
    }
    
    public static PKIMessages getInstance(final Object o) {
        if (o instanceof PKIMessages) {
            return (PKIMessages)o;
        }
        if (o != null) {
            return new PKIMessages(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
    
    public PKIMessage[] toPKIMessageArray() {
        final int size = this.content.size();
        final PKIMessage[] array = new PKIMessage[size];
        for (int i = 0; i != size; ++i) {
            array[i] = PKIMessage.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
}
