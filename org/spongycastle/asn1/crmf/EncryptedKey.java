package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.cms.*;
import org.spongycastle.asn1.*;

public class EncryptedKey extends ASN1Object implements ASN1Choice
{
    private EncryptedValue encryptedValue;
    private EnvelopedData envelopedData;
    
    public EncryptedKey(final EnvelopedData envelopedData) {
        this.envelopedData = envelopedData;
    }
    
    public EncryptedKey(final EncryptedValue encryptedValue) {
        this.encryptedValue = encryptedValue;
    }
    
    public static EncryptedKey getInstance(final Object o) {
        if (o instanceof EncryptedKey) {
            return (EncryptedKey)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new EncryptedKey(EnvelopedData.getInstance((ASN1TaggedObject)o, false));
        }
        if (o instanceof EncryptedValue) {
            return new EncryptedKey((EncryptedValue)o);
        }
        return new EncryptedKey(EncryptedValue.getInstance(o));
    }
    
    public ASN1Encodable getValue() {
        final EncryptedValue encryptedValue = this.encryptedValue;
        if (encryptedValue != null) {
            return encryptedValue;
        }
        return this.envelopedData;
    }
    
    public boolean isEncryptedValue() {
        return this.encryptedValue != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final EncryptedValue encryptedValue = this.encryptedValue;
        if (encryptedValue != null) {
            return encryptedValue.toASN1Primitive();
        }
        return new DERTaggedObject(false, 0, this.envelopedData);
    }
}
