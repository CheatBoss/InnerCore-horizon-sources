package org.spongycastle.asn1.esf;

import java.io.*;
import org.spongycastle.asn1.*;

public class OtherRevVals extends ASN1Object
{
    private ASN1ObjectIdentifier otherRevValType;
    private ASN1Encodable otherRevVals;
    
    public OtherRevVals(final ASN1ObjectIdentifier otherRevValType, final ASN1Encodable otherRevVals) {
        this.otherRevValType = otherRevValType;
        this.otherRevVals = otherRevVals;
    }
    
    private OtherRevVals(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.otherRevValType = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
            try {
                this.otherRevVals = ASN1Primitive.fromByteArray(asn1Sequence.getObjectAt(1).toASN1Primitive().getEncoded("DER"));
                return;
            }
            catch (IOException ex) {
                throw new IllegalStateException();
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static OtherRevVals getInstance(final Object o) {
        if (o instanceof OtherRevVals) {
            return (OtherRevVals)o;
        }
        if (o != null) {
            return new OtherRevVals(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getOtherRevValType() {
        return this.otherRevValType;
    }
    
    public ASN1Encodable getOtherRevVals() {
        return this.otherRevVals;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.otherRevValType);
        asn1EncodableVector.add(this.otherRevVals);
        return new DERSequence(asn1EncodableVector);
    }
}
