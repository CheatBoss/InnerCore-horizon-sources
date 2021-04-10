package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.*;

public class OcspResponsesID extends ASN1Object
{
    private OcspIdentifier ocspIdentifier;
    private OtherHash ocspRepHash;
    
    private OcspResponsesID(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1 && asn1Sequence.size() <= 2) {
            this.ocspIdentifier = OcspIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            if (asn1Sequence.size() > 1) {
                this.ocspRepHash = OtherHash.getInstance(asn1Sequence.getObjectAt(1));
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public OcspResponsesID(final OcspIdentifier ocspIdentifier) {
        this(ocspIdentifier, null);
    }
    
    public OcspResponsesID(final OcspIdentifier ocspIdentifier, final OtherHash ocspRepHash) {
        this.ocspIdentifier = ocspIdentifier;
        this.ocspRepHash = ocspRepHash;
    }
    
    public static OcspResponsesID getInstance(final Object o) {
        if (o instanceof OcspResponsesID) {
            return (OcspResponsesID)o;
        }
        if (o != null) {
            return new OcspResponsesID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public OcspIdentifier getOcspIdentifier() {
        return this.ocspIdentifier;
    }
    
    public OtherHash getOcspRepHash() {
        return this.ocspRepHash;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.ocspIdentifier);
        final OtherHash ocspRepHash = this.ocspRepHash;
        if (ocspRepHash != null) {
            asn1EncodableVector.add(ocspRepHash);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
