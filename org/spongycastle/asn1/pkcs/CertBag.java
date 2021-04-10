package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.*;

public class CertBag extends ASN1Object
{
    private ASN1ObjectIdentifier certId;
    private ASN1Encodable certValue;
    
    public CertBag(final ASN1ObjectIdentifier certId, final ASN1Encodable certValue) {
        this.certId = certId;
        this.certValue = certValue;
    }
    
    private CertBag(final ASN1Sequence asn1Sequence) {
        this.certId = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.certValue = ((DERTaggedObject)asn1Sequence.getObjectAt(1)).getObject();
    }
    
    public static CertBag getInstance(final Object o) {
        if (o instanceof CertBag) {
            return (CertBag)o;
        }
        if (o != null) {
            return new CertBag(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getCertId() {
        return this.certId;
    }
    
    public ASN1Encodable getCertValue() {
        return this.certValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certId);
        asn1EncodableVector.add(new DERTaggedObject(0, this.certValue));
        return new DERSequence(asn1EncodableVector);
    }
}
