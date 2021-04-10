package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.*;

public class CertStatus extends ASN1Object implements ASN1Choice
{
    private int tagNo;
    private ASN1Encodable value;
    
    public CertStatus() {
        this.tagNo = 0;
        this.value = DERNull.INSTANCE;
    }
    
    public CertStatus(final int tagNo, final ASN1Encodable value) {
        this.tagNo = tagNo;
        this.value = value;
    }
    
    private CertStatus(final ASN1TaggedObject asn1TaggedObject) {
        this.tagNo = asn1TaggedObject.getTagNo();
        final int tagNo = asn1TaggedObject.getTagNo();
        ASN1Object value = null;
        Label_0035: {
            if (tagNo != 0) {
                if (tagNo == 1) {
                    value = RevokedInfo.getInstance(asn1TaggedObject, false);
                    break Label_0035;
                }
                if (tagNo != 2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag encountered: ");
                    sb.append(asn1TaggedObject.getTagNo());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            value = DERNull.INSTANCE;
        }
        this.value = value;
    }
    
    public CertStatus(final RevokedInfo value) {
        this.tagNo = 1;
        this.value = value;
    }
    
    public static CertStatus getInstance(final Object o) {
        if (o == null || o instanceof CertStatus) {
            return (CertStatus)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new CertStatus((ASN1TaggedObject)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static CertStatus getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public ASN1Encodable getStatus() {
        return this.value;
    }
    
    public int getTagNo() {
        return this.tagNo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, this.value);
    }
}
