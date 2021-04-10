package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class Challenge extends ASN1Object
{
    private ASN1OctetString challenge;
    private AlgorithmIdentifier owf;
    private ASN1OctetString witness;
    
    private Challenge(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        int n = 0;
        if (size == 3) {
            this.owf = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            n = 1;
        }
        this.witness = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n));
        this.challenge = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n + 1));
    }
    
    public Challenge(final AlgorithmIdentifier owf, final byte[] array, final byte[] array2) {
        this.owf = owf;
        this.witness = new DEROctetString(array);
        this.challenge = new DEROctetString(array2);
    }
    
    public Challenge(final byte[] array, final byte[] array2) {
        this(null, array, array2);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(asn1Encodable);
        }
    }
    
    public static Challenge getInstance(final Object o) {
        if (o instanceof Challenge) {
            return (Challenge)o;
        }
        if (o != null) {
            return new Challenge(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getChallenge() {
        return this.challenge.getOctets();
    }
    
    public AlgorithmIdentifier getOwf() {
        return this.owf;
    }
    
    public byte[] getWitness() {
        return this.witness.getOctets();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        this.addOptional(asn1EncodableVector, this.owf);
        asn1EncodableVector.add(this.witness);
        asn1EncodableVector.add(this.challenge);
        return new DERSequence(asn1EncodableVector);
    }
}
