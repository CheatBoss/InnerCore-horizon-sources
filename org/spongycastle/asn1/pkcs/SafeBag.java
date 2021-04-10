package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.*;

public class SafeBag extends ASN1Object
{
    private ASN1Set bagAttributes;
    private ASN1ObjectIdentifier bagId;
    private ASN1Encodable bagValue;
    
    public SafeBag(final ASN1ObjectIdentifier bagId, final ASN1Encodable bagValue) {
        this.bagId = bagId;
        this.bagValue = bagValue;
        this.bagAttributes = null;
    }
    
    public SafeBag(final ASN1ObjectIdentifier bagId, final ASN1Encodable bagValue, final ASN1Set bagAttributes) {
        this.bagId = bagId;
        this.bagValue = bagValue;
        this.bagAttributes = bagAttributes;
    }
    
    private SafeBag(final ASN1Sequence asn1Sequence) {
        this.bagId = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.bagValue = ((ASN1TaggedObject)asn1Sequence.getObjectAt(1)).getObject();
        if (asn1Sequence.size() == 3) {
            this.bagAttributes = (ASN1Set)asn1Sequence.getObjectAt(2);
        }
    }
    
    public static SafeBag getInstance(final Object o) {
        if (o instanceof SafeBag) {
            return (SafeBag)o;
        }
        if (o != null) {
            return new SafeBag(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Set getBagAttributes() {
        return this.bagAttributes;
    }
    
    public ASN1ObjectIdentifier getBagId() {
        return this.bagId;
    }
    
    public ASN1Encodable getBagValue() {
        return this.bagValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.bagId);
        asn1EncodableVector.add(new DLTaggedObject(true, 0, this.bagValue));
        final ASN1Set bagAttributes = this.bagAttributes;
        if (bagAttributes != null) {
            asn1EncodableVector.add(bagAttributes);
        }
        return new DLSequence(asn1EncodableVector);
    }
}
