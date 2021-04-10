package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.*;

public class CrlValidatedID extends ASN1Object
{
    private OtherHash crlHash;
    private CrlIdentifier crlIdentifier;
    
    private CrlValidatedID(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1 && asn1Sequence.size() <= 2) {
            this.crlHash = OtherHash.getInstance(asn1Sequence.getObjectAt(0));
            if (asn1Sequence.size() > 1) {
                this.crlIdentifier = CrlIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public CrlValidatedID(final OtherHash otherHash) {
        this(otherHash, null);
    }
    
    public CrlValidatedID(final OtherHash crlHash, final CrlIdentifier crlIdentifier) {
        this.crlHash = crlHash;
        this.crlIdentifier = crlIdentifier;
    }
    
    public static CrlValidatedID getInstance(final Object o) {
        if (o instanceof CrlValidatedID) {
            return (CrlValidatedID)o;
        }
        if (o != null) {
            return new CrlValidatedID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public OtherHash getCrlHash() {
        return this.crlHash;
    }
    
    public CrlIdentifier getCrlIdentifier() {
        return this.crlIdentifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.crlHash.toASN1Primitive());
        final CrlIdentifier crlIdentifier = this.crlIdentifier;
        if (crlIdentifier != null) {
            asn1EncodableVector.add(crlIdentifier.toASN1Primitive());
        }
        return new DERSequence(asn1EncodableVector);
    }
}
