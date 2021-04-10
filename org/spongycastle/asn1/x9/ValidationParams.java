package org.spongycastle.asn1.x9;

import java.math.*;
import org.spongycastle.asn1.*;

public class ValidationParams extends ASN1Object
{
    private ASN1Integer pgenCounter;
    private DERBitString seed;
    
    private ValidationParams(final ASN1Sequence asn1Sequence) {
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
    
    public ValidationParams(final DERBitString seed, final ASN1Integer pgenCounter) {
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
    
    public ValidationParams(final byte[] array, final int n) {
        if (array != null) {
            this.seed = new DERBitString(array);
            this.pgenCounter = new ASN1Integer(n);
            return;
        }
        throw new IllegalArgumentException("'seed' cannot be null");
    }
    
    public static ValidationParams getInstance(final Object o) {
        if (o instanceof ValidationParams) {
            return (ValidationParams)o;
        }
        if (o != null) {
            return new ValidationParams(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static ValidationParams getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public BigInteger getPgenCounter() {
        return this.pgenCounter.getPositiveValue();
    }
    
    public byte[] getSeed() {
        return this.seed.getBytes();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.seed);
        asn1EncodableVector.add(this.pgenCounter);
        return new DERSequence(asn1EncodableVector);
    }
}
