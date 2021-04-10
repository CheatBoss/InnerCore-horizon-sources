package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class PopLinkWitnessV2 extends ASN1Object
{
    private final AlgorithmIdentifier keyGenAlgorithm;
    private final AlgorithmIdentifier macAlgorithm;
    private final byte[] witness;
    
    private PopLinkWitnessV2(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.keyGenAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.macAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.witness = Arrays.clone(ASN1OctetString.getInstance(asn1Sequence.getObjectAt(2)).getOctets());
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public PopLinkWitnessV2(final AlgorithmIdentifier keyGenAlgorithm, final AlgorithmIdentifier macAlgorithm, final byte[] array) {
        this.keyGenAlgorithm = keyGenAlgorithm;
        this.macAlgorithm = macAlgorithm;
        this.witness = Arrays.clone(array);
    }
    
    public static PopLinkWitnessV2 getInstance(final Object o) {
        if (o instanceof PopLinkWitnessV2) {
            return (PopLinkWitnessV2)o;
        }
        if (o != null) {
            return new PopLinkWitnessV2(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getKeyGenAlgorithm() {
        return this.keyGenAlgorithm;
    }
    
    public AlgorithmIdentifier getMacAlgorithm() {
        return this.macAlgorithm;
    }
    
    public byte[] getWitness() {
        return Arrays.clone(this.witness);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.keyGenAlgorithm);
        asn1EncodableVector.add(this.macAlgorithm);
        asn1EncodableVector.add(new DEROctetString(this.getWitness()));
        return new DERSequence(asn1EncodableVector);
    }
}
