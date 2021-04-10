package org.spongycastle.asn1.esf;

import java.io.*;
import org.spongycastle.asn1.*;

public class OtherRevRefs extends ASN1Object
{
    private ASN1ObjectIdentifier otherRevRefType;
    private ASN1Encodable otherRevRefs;
    
    public OtherRevRefs(final ASN1ObjectIdentifier otherRevRefType, final ASN1Encodable otherRevRefs) {
        this.otherRevRefType = otherRevRefType;
        this.otherRevRefs = otherRevRefs;
    }
    
    private OtherRevRefs(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.otherRevRefType = new ASN1ObjectIdentifier(((ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0)).getId());
            try {
                this.otherRevRefs = ASN1Primitive.fromByteArray(asn1Sequence.getObjectAt(1).toASN1Primitive().getEncoded("DER"));
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
    
    public static OtherRevRefs getInstance(final Object o) {
        if (o instanceof OtherRevRefs) {
            return (OtherRevRefs)o;
        }
        if (o != null) {
            return new OtherRevRefs(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getOtherRevRefType() {
        return this.otherRevRefType;
    }
    
    public ASN1Encodable getOtherRevRefs() {
        return this.otherRevRefs;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.otherRevRefType);
        asn1EncodableVector.add(this.otherRevRefs);
        return new DERSequence(asn1EncodableVector);
    }
}
