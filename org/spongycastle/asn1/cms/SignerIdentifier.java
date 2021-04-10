package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class SignerIdentifier extends ASN1Object implements ASN1Choice
{
    private ASN1Encodable id;
    
    public SignerIdentifier(final ASN1OctetString asn1OctetString) {
        this.id = new DERTaggedObject(false, 0, asn1OctetString);
    }
    
    public SignerIdentifier(final ASN1Primitive id) {
        this.id = id;
    }
    
    public SignerIdentifier(final IssuerAndSerialNumber id) {
        this.id = id;
    }
    
    public static SignerIdentifier getInstance(final Object o) {
        if (o == null || o instanceof SignerIdentifier) {
            return (SignerIdentifier)o;
        }
        if (o instanceof IssuerAndSerialNumber) {
            return new SignerIdentifier((IssuerAndSerialNumber)o);
        }
        if (o instanceof ASN1OctetString) {
            return new SignerIdentifier((ASN1OctetString)o);
        }
        if (o instanceof ASN1Primitive) {
            return new SignerIdentifier((ASN1Primitive)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal object in SignerIdentifier: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ASN1Encodable getId() {
        ASN1Encodable asn1Encodable2;
        final ASN1Encodable asn1Encodable = asn1Encodable2 = this.id;
        if (asn1Encodable instanceof ASN1TaggedObject) {
            asn1Encodable2 = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Encodable, false);
        }
        return asn1Encodable2;
    }
    
    public boolean isTagged() {
        return this.id instanceof ASN1TaggedObject;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.id.toASN1Primitive();
    }
}
