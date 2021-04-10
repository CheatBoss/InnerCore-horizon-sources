package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class RevReqContent extends ASN1Object
{
    private ASN1Sequence content;
    
    private RevReqContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public RevReqContent(final RevDetails revDetails) {
        this.content = new DERSequence(revDetails);
    }
    
    public RevReqContent(final RevDetails[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.content = new DERSequence(asn1EncodableVector);
    }
    
    public static RevReqContent getInstance(final Object o) {
        if (o instanceof RevReqContent) {
            return (RevReqContent)o;
        }
        if (o != null) {
            return new RevReqContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
    
    public RevDetails[] toRevDetailsArray() {
        final int size = this.content.size();
        final RevDetails[] array = new RevDetails[size];
        for (int i = 0; i != size; ++i) {
            array[i] = RevDetails.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
}
