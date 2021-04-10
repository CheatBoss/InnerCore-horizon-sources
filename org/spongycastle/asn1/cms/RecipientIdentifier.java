package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class RecipientIdentifier extends ASN1Object implements ASN1Choice
{
    private ASN1Encodable id;
    
    public RecipientIdentifier(final ASN1OctetString asn1OctetString) {
        this.id = new DERTaggedObject(false, 0, asn1OctetString);
    }
    
    public RecipientIdentifier(final ASN1Primitive id) {
        this.id = id;
    }
    
    public RecipientIdentifier(final IssuerAndSerialNumber id) {
        this.id = id;
    }
    
    public static RecipientIdentifier getInstance(final Object o) {
        if (o == null || o instanceof RecipientIdentifier) {
            return (RecipientIdentifier)o;
        }
        if (o instanceof IssuerAndSerialNumber) {
            return new RecipientIdentifier((IssuerAndSerialNumber)o);
        }
        if (o instanceof ASN1OctetString) {
            return new RecipientIdentifier((ASN1OctetString)o);
        }
        if (o instanceof ASN1Primitive) {
            return new RecipientIdentifier((ASN1Primitive)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Illegal object in RecipientIdentifier: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ASN1Encodable getId() {
        final ASN1Encodable id = this.id;
        if (id instanceof ASN1TaggedObject) {
            return ASN1OctetString.getInstance((ASN1TaggedObject)id, false);
        }
        return IssuerAndSerialNumber.getInstance(id);
    }
    
    public boolean isTagged() {
        return this.id instanceof ASN1TaggedObject;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.id.toASN1Primitive();
    }
}
