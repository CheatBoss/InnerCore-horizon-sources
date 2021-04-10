package org.spongycastle.asn1.x9;

import org.spongycastle.asn1.*;

public class DHValidationParms extends ASN1Object
{
    private ASN1Integer pgenCounter;
    private DERBitString seed;
    
    private DHValidationParms(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.seed = DERBitString.getInstance(asn1Sequence.getObjectAt(0));
            this.pgenCounter = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public DHValidationParms(final DERBitString seed, final ASN1Integer pgenCounter) {
        if (seed == null) {
            throw new IllegalArgumentException("'seed' cannot be null");
        }
        if (pgenCounter != null) {
            this.seed = seed;
            this.pgenCounter = pgenCounter;
            return;
        }
        throw new IllegalArgumentException("'pgenCounter' cannot be null");
    }
    
    public static DHValidationParms getInstance(final Object o) {
        if (o instanceof DHValidationParms) {
            return (DHValidationParms)o;
        }
        if (o != null) {
            return new DHValidationParms(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static DHValidationParms getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1Integer getPgenCounter() {
        return this.pgenCounter;
    }
    
    public DERBitString getSeed() {
        return this.seed;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.seed);
        asn1EncodableVector.add(this.pgenCounter);
        return new DERSequence(asn1EncodableVector);
    }
}
