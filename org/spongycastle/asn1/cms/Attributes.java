package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class Attributes extends ASN1Object
{
    private ASN1Set attributes;
    
    public Attributes(final ASN1EncodableVector asn1EncodableVector) {
        this.attributes = new DLSet(asn1EncodableVector);
    }
    
    private Attributes(final ASN1Set attributes) {
        this.attributes = attributes;
    }
    
    public static Attributes getInstance(final Object o) {
        if (o instanceof Attributes) {
            return (Attributes)o;
        }
        if (o != null) {
            return new Attributes(ASN1Set.getInstance(o));
        }
        return null;
    }
    
    public Attribute[] getAttributes() {
        final int size = this.attributes.size();
        final Attribute[] array = new Attribute[size];
        for (int i = 0; i != size; ++i) {
            array[i] = Attribute.getInstance(this.attributes.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.attributes;
    }
}
