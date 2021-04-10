package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class OtherHashAlgAndValue extends ASN1Object
{
    private AlgorithmIdentifier hashAlgorithm;
    private ASN1OctetString hashValue;
    
    private OtherHashAlgAndValue(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.hashValue = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public OtherHashAlgAndValue(final AlgorithmIdentifier hashAlgorithm, final ASN1OctetString hashValue) {
        this.hashAlgorithm = hashAlgorithm;
        this.hashValue = hashValue;
    }
    
    public static OtherHashAlgAndValue getInstance(final Object o) {
        if (o instanceof OtherHashAlgAndValue) {
            return (OtherHashAlgAndValue)o;
        }
        if (o != null) {
            return new OtherHashAlgAndValue(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public ASN1OctetString getHashValue() {
        return this.hashValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.hashAlgorithm);
        asn1EncodableVector.add(this.hashValue);
        return new DERSequence(asn1EncodableVector);
    }
}
