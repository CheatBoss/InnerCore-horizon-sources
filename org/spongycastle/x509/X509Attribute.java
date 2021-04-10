package org.spongycastle.x509;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class X509Attribute extends ASN1Object
{
    Attribute attr;
    
    public X509Attribute(final String s, final ASN1Encodable asn1Encodable) {
        this.attr = new Attribute(new ASN1ObjectIdentifier(s), new DERSet(asn1Encodable));
    }
    
    public X509Attribute(final String s, final ASN1EncodableVector asn1EncodableVector) {
        this.attr = new Attribute(new ASN1ObjectIdentifier(s), new DERSet(asn1EncodableVector));
    }
    
    X509Attribute(final ASN1Encodable asn1Encodable) {
        this.attr = Attribute.getInstance(asn1Encodable);
    }
    
    public String getOID() {
        return this.attr.getAttrType().getId();
    }
    
    public ASN1Encodable[] getValues() {
        final ASN1Set attrValues = this.attr.getAttrValues();
        final ASN1Encodable[] array = new ASN1Encodable[attrValues.size()];
        for (int i = 0; i != attrValues.size(); ++i) {
            array[i] = attrValues.getObjectAt(i);
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.attr.toASN1Primitive();
    }
}
