package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.cms.*;
import org.spongycastle.asn1.*;

public class POPOPrivKey extends ASN1Object implements ASN1Choice
{
    public static final int agreeMAC = 3;
    public static final int dhMAC = 2;
    public static final int encryptedKey = 4;
    public static final int subsequentMessage = 1;
    public static final int thisMessage = 0;
    private ASN1Encodable obj;
    private int tagNo;
    
    private POPOPrivKey(final ASN1TaggedObject asn1TaggedObject) {
        final int tagNo = asn1TaggedObject.getTagNo();
        this.tagNo = tagNo;
        while (true) {
            Label_0069: {
                if (tagNo == 0) {
                    break Label_0069;
                }
                ASN1Object obj;
                if (tagNo != 1) {
                    if (tagNo == 2) {
                        break Label_0069;
                    }
                    if (tagNo != 3) {
                        if (tagNo != 4) {
                            throw new IllegalArgumentException("unknown tag in POPOPrivKey");
                        }
                        obj = EnvelopedData.getInstance(asn1TaggedObject, false);
                    }
                    else {
                        obj = PKMACValue.getInstance(asn1TaggedObject, false);
                    }
                }
                else {
                    obj = SubsequentMessage.valueOf(ASN1Integer.getInstance(asn1TaggedObject, false).getValue().intValue());
                }
                this.obj = obj;
                return;
            }
            ASN1Object obj = DERBitString.getInstance(asn1TaggedObject, false);
            continue;
        }
    }
    
    public POPOPrivKey(final SubsequentMessage obj) {
        this.tagNo = 1;
        this.obj = obj;
    }
    
    public static POPOPrivKey getInstance(final Object o) {
        if (o instanceof POPOPrivKey) {
            return (POPOPrivKey)o;
        }
        if (o != null) {
            return new POPOPrivKey(ASN1TaggedObject.getInstance(o));
        }
        return null;
    }
    
    public static POPOPrivKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1TaggedObject.getInstance(asn1TaggedObject, b));
    }
    
    public int getType() {
        return this.tagNo;
    }
    
    public ASN1Encodable getValue() {
        return this.obj;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, this.obj);
    }
}
