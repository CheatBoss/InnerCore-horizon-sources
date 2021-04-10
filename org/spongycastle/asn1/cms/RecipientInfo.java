package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class RecipientInfo extends ASN1Object implements ASN1Choice
{
    ASN1Encodable info;
    
    public RecipientInfo(final ASN1Primitive info) {
        this.info = info;
    }
    
    public RecipientInfo(final KEKRecipientInfo kekRecipientInfo) {
        this.info = new DERTaggedObject(false, 2, kekRecipientInfo);
    }
    
    public RecipientInfo(final KeyAgreeRecipientInfo keyAgreeRecipientInfo) {
        this.info = new DERTaggedObject(false, 1, keyAgreeRecipientInfo);
    }
    
    public RecipientInfo(final KeyTransRecipientInfo info) {
        this.info = info;
    }
    
    public RecipientInfo(final OtherRecipientInfo otherRecipientInfo) {
        this.info = new DERTaggedObject(false, 4, otherRecipientInfo);
    }
    
    public RecipientInfo(final PasswordRecipientInfo passwordRecipientInfo) {
        this.info = new DERTaggedObject(false, 3, passwordRecipientInfo);
    }
    
    public static RecipientInfo getInstance(final Object o) {
        if (o == null || o instanceof RecipientInfo) {
            return (RecipientInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RecipientInfo((ASN1Primitive)o);
        }
        if (o instanceof ASN1TaggedObject) {
            return new RecipientInfo((ASN1Primitive)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    private KEKRecipientInfo getKEKInfo(final ASN1TaggedObject asn1TaggedObject) {
        return KEKRecipientInfo.getInstance(asn1TaggedObject, asn1TaggedObject.isExplicit());
    }
    
    public ASN1Encodable getInfo() {
        final ASN1Encodable info = this.info;
        if (!(info instanceof ASN1TaggedObject)) {
            return KeyTransRecipientInfo.getInstance(info);
        }
        final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)info;
        final int tagNo = asn1TaggedObject.getTagNo();
        if (tagNo == 1) {
            return KeyAgreeRecipientInfo.getInstance(asn1TaggedObject, false);
        }
        if (tagNo == 2) {
            return this.getKEKInfo(asn1TaggedObject);
        }
        if (tagNo == 3) {
            return PasswordRecipientInfo.getInstance(asn1TaggedObject, false);
        }
        if (tagNo == 4) {
            return OtherRecipientInfo.getInstance(asn1TaggedObject, false);
        }
        throw new IllegalStateException("unknown tag");
    }
    
    public ASN1Integer getVersion() {
        final ASN1Encodable info = this.info;
        if (!(info instanceof ASN1TaggedObject)) {
            return KeyTransRecipientInfo.getInstance(info).getVersion();
        }
        final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)info;
        final int tagNo = asn1TaggedObject.getTagNo();
        if (tagNo == 1) {
            return KeyAgreeRecipientInfo.getInstance(asn1TaggedObject, false).getVersion();
        }
        if (tagNo == 2) {
            return this.getKEKInfo(asn1TaggedObject).getVersion();
        }
        if (tagNo == 3) {
            return PasswordRecipientInfo.getInstance(asn1TaggedObject, false).getVersion();
        }
        if (tagNo == 4) {
            return new ASN1Integer(0L);
        }
        throw new IllegalStateException("unknown tag");
    }
    
    public boolean isTagged() {
        return this.info instanceof ASN1TaggedObject;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.info.toASN1Primitive();
    }
}
