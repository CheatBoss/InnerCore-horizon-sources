package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class ResponderID extends ASN1Object implements ASN1Choice
{
    private ASN1Encodable value;
    
    public ResponderID(final ASN1OctetString value) {
        this.value = value;
    }
    
    public ResponderID(final X500Name value) {
        this.value = value;
    }
    
    public static ResponderID getInstance(final Object o) {
        if (o instanceof ResponderID) {
            return (ResponderID)o;
        }
        if (o instanceof DEROctetString) {
            return new ResponderID((ASN1OctetString)o);
        }
        if (!(o instanceof ASN1TaggedObject)) {
            return new ResponderID(X500Name.getInstance(o));
        }
        final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)o;
        if (asn1TaggedObject.getTagNo() == 1) {
            return new ResponderID(X500Name.getInstance(asn1TaggedObject, true));
        }
        return new ResponderID(ASN1OctetString.getInstance(asn1TaggedObject, true));
    }
    
    public static ResponderID getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public byte[] getKeyHash() {
        final ASN1Encodable value = this.value;
        if (value instanceof ASN1OctetString) {
            return ((ASN1OctetString)value).getOctets();
        }
        return null;
    }
    
    public X500Name getName() {
        final ASN1Encodable value = this.value;
        if (value instanceof ASN1OctetString) {
            return null;
        }
        return X500Name.getInstance(value);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.value instanceof ASN1OctetString) {
            return new DERTaggedObject(true, 2, this.value);
        }
        return new DERTaggedObject(true, 1, this.value);
    }
}
