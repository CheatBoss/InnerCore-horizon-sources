package org.spongycastle.asn1.x509;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class SubjectKeyIdentifier extends ASN1Object
{
    private byte[] keyidentifier;
    
    protected SubjectKeyIdentifier(final ASN1OctetString asn1OctetString) {
        this(asn1OctetString.getOctets());
    }
    
    public SubjectKeyIdentifier(final byte[] array) {
        this.keyidentifier = Arrays.clone(array);
    }
    
    public static SubjectKeyIdentifier fromExtensions(final Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.subjectKeyIdentifier));
    }
    
    public static SubjectKeyIdentifier getInstance(final Object o) {
        if (o instanceof SubjectKeyIdentifier) {
            return (SubjectKeyIdentifier)o;
        }
        if (o != null) {
            return new SubjectKeyIdentifier(ASN1OctetString.getInstance(o));
        }
        return null;
    }
    
    public static SubjectKeyIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1OctetString.getInstance(asn1TaggedObject, b));
    }
    
    public byte[] getKeyIdentifier() {
        return Arrays.clone(this.keyidentifier);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DEROctetString(this.getKeyIdentifier());
    }
}
