package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.*;

public class ProofOfPossession extends ASN1Object implements ASN1Choice
{
    public static final int TYPE_KEY_AGREEMENT = 3;
    public static final int TYPE_KEY_ENCIPHERMENT = 2;
    public static final int TYPE_RA_VERIFIED = 0;
    public static final int TYPE_SIGNING_KEY = 1;
    private ASN1Encodable obj;
    private int tagNo;
    
    public ProofOfPossession() {
        this.tagNo = 0;
        this.obj = DERNull.INSTANCE;
    }
    
    public ProofOfPossession(final int tagNo, final POPOPrivKey obj) {
        this.tagNo = tagNo;
        this.obj = obj;
    }
    
    private ProofOfPossession(final ASN1TaggedObject asn1TaggedObject) {
        final int tagNo = asn1TaggedObject.getTagNo();
        this.tagNo = tagNo;
        ASN1Object obj;
        if (tagNo != 0) {
            if (tagNo != 1) {
                if (tagNo != 2 && tagNo != 3) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unknown tag: ");
                    sb.append(this.tagNo);
                    throw new IllegalArgumentException(sb.toString());
                }
                obj = POPOPrivKey.getInstance(asn1TaggedObject, true);
            }
            else {
                obj = POPOSigningKey.getInstance(asn1TaggedObject, false);
            }
        }
        else {
            obj = DERNull.INSTANCE;
        }
        this.obj = obj;
    }
    
    public ProofOfPossession(final POPOSigningKey obj) {
        this.tagNo = 1;
        this.obj = obj;
    }
    
    public static ProofOfPossession getInstance(final Object o) {
        if (o == null || o instanceof ProofOfPossession) {
            return (ProofOfPossession)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new ProofOfPossession((ASN1TaggedObject)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid object: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ASN1Encodable getObject() {
        return this.obj;
    }
    
    public int getType() {
        return this.tagNo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, this.obj);
    }
}
