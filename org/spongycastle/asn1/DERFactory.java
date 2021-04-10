package org.spongycastle.asn1;

class DERFactory
{
    static final ASN1Sequence EMPTY_SEQUENCE;
    static final ASN1Set EMPTY_SET;
    
    static {
        EMPTY_SEQUENCE = new DERSequence();
        EMPTY_SET = new DERSet();
    }
    
    static ASN1Sequence createSequence(final ASN1EncodableVector asn1EncodableVector) {
        if (asn1EncodableVector.size() < 1) {
            return DERFactory.EMPTY_SEQUENCE;
        }
        return new DLSequence(asn1EncodableVector);
    }
    
    static ASN1Set createSet(final ASN1EncodableVector asn1EncodableVector) {
        if (asn1EncodableVector.size() < 1) {
            return DERFactory.EMPTY_SET;
        }
        return new DLSet(asn1EncodableVector);
    }
}
