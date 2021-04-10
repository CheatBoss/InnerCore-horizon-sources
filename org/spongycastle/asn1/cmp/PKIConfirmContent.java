package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class PKIConfirmContent extends ASN1Object
{
    private ASN1Null val;
    
    public PKIConfirmContent() {
        this.val = DERNull.INSTANCE;
    }
    
    private PKIConfirmContent(final ASN1Null val) {
        this.val = val;
    }
    
    public static PKIConfirmContent getInstance(final Object o) {
        if (o == null || o instanceof PKIConfirmContent) {
            return (PKIConfirmContent)o;
        }
        if (o instanceof ASN1Null) {
            return new PKIConfirmContent((ASN1Null)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid object: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.val;
    }
}
