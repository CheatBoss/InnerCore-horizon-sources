package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class RecipientEncryptedKey extends ASN1Object
{
    private ASN1OctetString encryptedKey;
    private KeyAgreeRecipientIdentifier identifier;
    
    private RecipientEncryptedKey(final ASN1Sequence asn1Sequence) {
        this.identifier = KeyAgreeRecipientIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.encryptedKey = (ASN1OctetString)asn1Sequence.getObjectAt(1);
    }
    
    public RecipientEncryptedKey(final KeyAgreeRecipientIdentifier identifier, final ASN1OctetString encryptedKey) {
        this.identifier = identifier;
        this.encryptedKey = encryptedKey;
    }
    
    public static RecipientEncryptedKey getInstance(final Object o) {
        if (o instanceof RecipientEncryptedKey) {
            return (RecipientEncryptedKey)o;
        }
        if (o != null) {
            return new RecipientEncryptedKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static RecipientEncryptedKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1OctetString getEncryptedKey() {
        return this.encryptedKey;
    }
    
    public KeyAgreeRecipientIdentifier getIdentifier() {
        return this.identifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.identifier);
        asn1EncodableVector.add(this.encryptedKey);
        return new DERSequence(asn1EncodableVector);
    }
}
