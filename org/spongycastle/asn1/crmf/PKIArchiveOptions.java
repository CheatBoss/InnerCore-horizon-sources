package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.*;

public class PKIArchiveOptions extends ASN1Object implements ASN1Choice
{
    public static final int archiveRemGenPrivKey = 2;
    public static final int encryptedPrivKey = 0;
    public static final int keyGenParameters = 1;
    private ASN1Encodable value;
    
    public PKIArchiveOptions(final ASN1OctetString value) {
        this.value = value;
    }
    
    private PKIArchiveOptions(final ASN1TaggedObject asn1TaggedObject) {
        final int tagNo = asn1TaggedObject.getTagNo();
        ASN1Object value;
        if (tagNo != 0) {
            if (tagNo != 1) {
                if (tagNo != 2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unknown tag number: ");
                    sb.append(asn1TaggedObject.getTagNo());
                    throw new IllegalArgumentException(sb.toString());
                }
                value = ASN1Boolean.getInstance(asn1TaggedObject, false);
            }
            else {
                value = ASN1OctetString.getInstance(asn1TaggedObject, false);
            }
        }
        else {
            value = EncryptedKey.getInstance(asn1TaggedObject.getObject());
        }
        this.value = value;
    }
    
    public PKIArchiveOptions(final EncryptedKey value) {
        this.value = value;
    }
    
    public PKIArchiveOptions(final boolean b) {
        this.value = ASN1Boolean.getInstance(b);
    }
    
    public static PKIArchiveOptions getInstance(final Object o) {
        if (o == null || o instanceof PKIArchiveOptions) {
            return (PKIArchiveOptions)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new PKIArchiveOptions((ASN1TaggedObject)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object: ");
        sb.append(o);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public int getType() {
        final ASN1Encodable value = this.value;
        if (value instanceof EncryptedKey) {
            return 0;
        }
        if (value instanceof ASN1OctetString) {
            return 1;
        }
        return 2;
    }
    
    public ASN1Encodable getValue() {
        return this.value;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1Encodable value = this.value;
        if (value instanceof EncryptedKey) {
            return new DERTaggedObject(true, 0, this.value);
        }
        if (value instanceof ASN1OctetString) {
            return new DERTaggedObject(false, 1, this.value);
        }
        return new DERTaggedObject(false, 2, this.value);
    }
}
