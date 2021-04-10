package org.spongycastle.asn1.x500;

import org.spongycastle.asn1.*;

public class RDN extends ASN1Object
{
    private ASN1Set values;
    
    public RDN(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(asn1ObjectIdentifier);
        asn1EncodableVector.add(asn1Encodable);
        this.values = new DERSet(new DERSequence(asn1EncodableVector));
    }
    
    private RDN(final ASN1Set values) {
        this.values = values;
    }
    
    public RDN(final AttributeTypeAndValue attributeTypeAndValue) {
        this.values = new DERSet(attributeTypeAndValue);
    }
    
    public RDN(final AttributeTypeAndValue[] array) {
        this.values = new DERSet(array);
    }
    
    public static RDN getInstance(final Object o) {
        if (o instanceof RDN) {
            return (RDN)o;
        }
        if (o != null) {
            return new RDN(ASN1Set.getInstance(o));
        }
        return null;
    }
    
    public AttributeTypeAndValue getFirst() {
        if (this.values.size() == 0) {
            return null;
        }
        return AttributeTypeAndValue.getInstance(this.values.getObjectAt(0));
    }
    
    public AttributeTypeAndValue[] getTypesAndValues() {
        final int size = this.values.size();
        final AttributeTypeAndValue[] array = new AttributeTypeAndValue[size];
        for (int i = 0; i != size; ++i) {
            array[i] = AttributeTypeAndValue.getInstance(this.values.getObjectAt(i));
        }
        return array;
    }
    
    public boolean isMultiValued() {
        return this.values.size() > 1;
    }
    
    public int size() {
        return this.values.size();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.values;
    }
}
