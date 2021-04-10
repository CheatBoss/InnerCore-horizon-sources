package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.*;

public class CommitmentTypeIndication extends ASN1Object
{
    private ASN1ObjectIdentifier commitmentTypeId;
    private ASN1Sequence commitmentTypeQualifier;
    
    public CommitmentTypeIndication(final ASN1ObjectIdentifier commitmentTypeId) {
        this.commitmentTypeId = commitmentTypeId;
    }
    
    public CommitmentTypeIndication(final ASN1ObjectIdentifier commitmentTypeId, final ASN1Sequence commitmentTypeQualifier) {
        this.commitmentTypeId = commitmentTypeId;
        this.commitmentTypeQualifier = commitmentTypeQualifier;
    }
    
    private CommitmentTypeIndication(final ASN1Sequence asn1Sequence) {
        this.commitmentTypeId = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
        if (asn1Sequence.size() > 1) {
            this.commitmentTypeQualifier = (ASN1Sequence)asn1Sequence.getObjectAt(1);
        }
    }
    
    public static CommitmentTypeIndication getInstance(final Object o) {
        if (o != null && !(o instanceof CommitmentTypeIndication)) {
            return new CommitmentTypeIndication(ASN1Sequence.getInstance(o));
        }
        return (CommitmentTypeIndication)o;
    }
    
    public ASN1ObjectIdentifier getCommitmentTypeId() {
        return this.commitmentTypeId;
    }
    
    public ASN1Sequence getCommitmentTypeQualifier() {
        return this.commitmentTypeQualifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.commitmentTypeId);
        final ASN1Sequence commitmentTypeQualifier = this.commitmentTypeQualifier;
        if (commitmentTypeQualifier != null) {
            asn1EncodableVector.add(commitmentTypeQualifier);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
