package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.*;

public class SignaturePolicyId extends ASN1Object
{
    private OtherHashAlgAndValue sigPolicyHash;
    private ASN1ObjectIdentifier sigPolicyId;
    private SigPolicyQualifiers sigPolicyQualifiers;
    
    public SignaturePolicyId(final ASN1ObjectIdentifier asn1ObjectIdentifier, final OtherHashAlgAndValue otherHashAlgAndValue) {
        this(asn1ObjectIdentifier, otherHashAlgAndValue, null);
    }
    
    public SignaturePolicyId(final ASN1ObjectIdentifier sigPolicyId, final OtherHashAlgAndValue sigPolicyHash, final SigPolicyQualifiers sigPolicyQualifiers) {
        this.sigPolicyId = sigPolicyId;
        this.sigPolicyHash = sigPolicyHash;
        this.sigPolicyQualifiers = sigPolicyQualifiers;
    }
    
    private SignaturePolicyId(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2 && asn1Sequence.size() != 3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        this.sigPolicyId = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.sigPolicyHash = OtherHashAlgAndValue.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() == 3) {
            this.sigPolicyQualifiers = SigPolicyQualifiers.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public static SignaturePolicyId getInstance(final Object o) {
        if (o instanceof SignaturePolicyId) {
            return (SignaturePolicyId)o;
        }
        if (o != null) {
            return new SignaturePolicyId(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public OtherHashAlgAndValue getSigPolicyHash() {
        return this.sigPolicyHash;
    }
    
    public ASN1ObjectIdentifier getSigPolicyId() {
        return new ASN1ObjectIdentifier(this.sigPolicyId.getId());
    }
    
    public SigPolicyQualifiers getSigPolicyQualifiers() {
        return this.sigPolicyQualifiers;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.sigPolicyId);
        asn1EncodableVector.add(this.sigPolicyHash);
        final SigPolicyQualifiers sigPolicyQualifiers = this.sigPolicyQualifiers;
        if (sigPolicyQualifiers != null) {
            asn1EncodableVector.add(sigPolicyQualifiers);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
