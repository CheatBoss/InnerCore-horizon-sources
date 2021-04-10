package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class InfoTypeAndValue extends ASN1Object
{
    private ASN1ObjectIdentifier infoType;
    private ASN1Encodable infoValue;
    
    public InfoTypeAndValue(final ASN1ObjectIdentifier infoType) {
        this.infoType = infoType;
        this.infoValue = null;
    }
    
    public InfoTypeAndValue(final ASN1ObjectIdentifier infoType, final ASN1Encodable infoValue) {
        this.infoType = infoType;
        this.infoValue = infoValue;
    }
    
    private InfoTypeAndValue(final ASN1Sequence asn1Sequence) {
        this.infoType = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.infoValue = asn1Sequence.getObjectAt(1);
        }
    }
    
    public static InfoTypeAndValue getInstance(final Object o) {
        if (o instanceof InfoTypeAndValue) {
            return (InfoTypeAndValue)o;
        }
        if (o != null) {
            return new InfoTypeAndValue(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getInfoType() {
        return this.infoType;
    }
    
    public ASN1Encodable getInfoValue() {
        return this.infoValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.infoType);
        final ASN1Encodable infoValue = this.infoValue;
        if (infoValue != null) {
            asn1EncodableVector.add(infoValue);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
