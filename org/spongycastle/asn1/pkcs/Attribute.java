package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.*;

public class Attribute extends ASN1Object
{
    private ASN1ObjectIdentifier attrType;
    private ASN1Set attrValues;
    
    public Attribute(final ASN1ObjectIdentifier attrType, final ASN1Set attrValues) {
        this.attrType = attrType;
        this.attrValues = attrValues;
    }
    
    public Attribute(final ASN1Sequence asn1Sequence) {
        this.attrType = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.attrValues = (ASN1Set)asn1Sequence.getObjectAt(1);
    }
    
    public static Attribute getInstance(final Object o) {
        if (o == null || o instanceof Attribute) {
            return (Attribute)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Attribute((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ASN1ObjectIdentifier getAttrType() {
        return this.attrType;
    }
    
    public ASN1Set getAttrValues() {
        return this.attrValues;
    }
    
    public ASN1Encodable[] getAttributeValues() {
        return this.attrValues.toArray();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.attrType);
        asn1EncodableVector.add(this.attrValues);
        return new DERSequence(asn1EncodableVector);
    }
}
