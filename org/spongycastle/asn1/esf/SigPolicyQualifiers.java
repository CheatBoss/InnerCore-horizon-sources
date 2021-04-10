package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.*;

public class SigPolicyQualifiers extends ASN1Object
{
    ASN1Sequence qualifiers;
    
    private SigPolicyQualifiers(final ASN1Sequence qualifiers) {
        this.qualifiers = qualifiers;
    }
    
    public SigPolicyQualifiers(final SigPolicyQualifierInfo[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.qualifiers = new DERSequence(asn1EncodableVector);
    }
    
    public static SigPolicyQualifiers getInstance(final Object o) {
        if (o instanceof SigPolicyQualifiers) {
            return (SigPolicyQualifiers)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SigPolicyQualifiers(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public SigPolicyQualifierInfo getInfoAt(final int n) {
        return SigPolicyQualifierInfo.getInstance(this.qualifiers.getObjectAt(n));
    }
    
    public int size() {
        return this.qualifiers.size();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.qualifiers;
    }
}
