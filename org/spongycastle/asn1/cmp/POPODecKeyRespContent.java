package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class POPODecKeyRespContent extends ASN1Object
{
    private ASN1Sequence content;
    
    private POPODecKeyRespContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static POPODecKeyRespContent getInstance(final Object o) {
        if (o instanceof POPODecKeyRespContent) {
            return (POPODecKeyRespContent)o;
        }
        if (o != null) {
            return new POPODecKeyRespContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer[] toASN1IntegerArray() {
        final int size = this.content.size();
        final ASN1Integer[] array = new ASN1Integer[size];
        for (int i = 0; i != size; ++i) {
            array[i] = ASN1Integer.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
}
