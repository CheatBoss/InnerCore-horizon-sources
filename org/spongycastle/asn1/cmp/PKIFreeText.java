package org.spongycastle.asn1.cmp;

import java.util.*;
import org.spongycastle.asn1.*;

public class PKIFreeText extends ASN1Object
{
    ASN1Sequence strings;
    
    public PKIFreeText(final String s) {
        this(new DERUTF8String(s));
    }
    
    private PKIFreeText(final ASN1Sequence strings) {
        final Enumeration objects = strings.getObjects();
        while (objects.hasMoreElements()) {
            if (objects.nextElement() instanceof DERUTF8String) {
                continue;
            }
            throw new IllegalArgumentException("attempt to insert non UTF8 STRING into PKIFreeText");
        }
        this.strings = strings;
    }
    
    public PKIFreeText(final DERUTF8String derutf8String) {
        this.strings = new DERSequence(derutf8String);
    }
    
    public PKIFreeText(final String[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(new DERUTF8String(array[i]));
        }
        this.strings = new DERSequence(asn1EncodableVector);
    }
    
    public PKIFreeText(final DERUTF8String[] array) {
        this.strings = new DERSequence(array);
    }
    
    public static PKIFreeText getInstance(final Object o) {
        if (o instanceof PKIFreeText) {
            return (PKIFreeText)o;
        }
        if (o != null) {
            return new PKIFreeText(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static PKIFreeText getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public DERUTF8String getStringAt(final int n) {
        return (DERUTF8String)this.strings.getObjectAt(n);
    }
    
    public int size() {
        return this.strings.size();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.strings;
    }
}
