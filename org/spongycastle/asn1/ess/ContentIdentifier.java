package org.spongycastle.asn1.ess;

import org.spongycastle.asn1.*;

public class ContentIdentifier extends ASN1Object
{
    ASN1OctetString value;
    
    private ContentIdentifier(final ASN1OctetString value) {
        this.value = value;
    }
    
    public ContentIdentifier(final byte[] array) {
        this(new DEROctetString(array));
    }
    
    public static ContentIdentifier getInstance(final Object o) {
        if (o instanceof ContentIdentifier) {
            return (ContentIdentifier)o;
        }
        if (o != null) {
            return new ContentIdentifier(ASN1OctetString.getInstance(o));
        }
        return null;
    }
    
    public ASN1OctetString getValue() {
        return this.value;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }
}
