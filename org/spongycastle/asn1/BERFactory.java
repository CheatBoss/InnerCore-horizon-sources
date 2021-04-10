package org.spongycastle.asn1;

class BERFactory
{
    static final BERSequence EMPTY_SEQUENCE;
    static final BERSet EMPTY_SET;
    
    static {
        EMPTY_SEQUENCE = new BERSequence();
        EMPTY_SET = new BERSet();
    }
    
    static BERSequence createSequence(final ASN1EncodableVector asn1EncodableVector) {
        if (asn1EncodableVector.size() < 1) {
            return BERFactory.EMPTY_SEQUENCE;
        }
        return new BERSequence(asn1EncodableVector);
    }
    
    static BERSet createSet(final ASN1EncodableVector asn1EncodableVector) {
        if (asn1EncodableVector.size() < 1) {
            return BERFactory.EMPTY_SET;
        }
        return new BERSet(asn1EncodableVector);
    }
}
