package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class KeyAgreeRecipientIdentifier extends ASN1Object implements ASN1Choice
{
    private IssuerAndSerialNumber issuerSerial;
    private RecipientKeyIdentifier rKeyID;
    
    public KeyAgreeRecipientIdentifier(final IssuerAndSerialNumber issuerSerial) {
        this.issuerSerial = issuerSerial;
        this.rKeyID = null;
    }
    
    public KeyAgreeRecipientIdentifier(final RecipientKeyIdentifier rKeyID) {
        this.issuerSerial = null;
        this.rKeyID = rKeyID;
    }
    
    public static KeyAgreeRecipientIdentifier getInstance(final Object o) {
        if (o == null || o instanceof KeyAgreeRecipientIdentifier) {
            return (KeyAgreeRecipientIdentifier)o;
        }
        if (o instanceof ASN1Sequence) {
            return new KeyAgreeRecipientIdentifier(IssuerAndSerialNumber.getInstance(o));
        }
        if (o instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)o;
            if (asn1TaggedObject.getTagNo() == 0) {
                return new KeyAgreeRecipientIdentifier(RecipientKeyIdentifier.getInstance(asn1TaggedObject, false));
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid KeyAgreeRecipientIdentifier: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static KeyAgreeRecipientIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public IssuerAndSerialNumber getIssuerAndSerialNumber() {
        return this.issuerSerial;
    }
    
    public RecipientKeyIdentifier getRKeyID() {
        return this.rKeyID;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final IssuerAndSerialNumber issuerSerial = this.issuerSerial;
        if (issuerSerial != null) {
            return issuerSerial.toASN1Primitive();
        }
        return new DERTaggedObject(false, 0, this.rKeyID);
    }
}
