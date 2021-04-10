package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class IdentityProofV2 extends ASN1Object
{
    private final AlgorithmIdentifier macAlgId;
    private final AlgorithmIdentifier proofAlgID;
    private final byte[] witness;
    
    private IdentityProofV2(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.proofAlgID = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.macAlgId = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.witness = Arrays.clone(ASN1OctetString.getInstance(asn1Sequence.getObjectAt(2)).getOctets());
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public IdentityProofV2(final AlgorithmIdentifier proofAlgID, final AlgorithmIdentifier macAlgId, final byte[] array) {
        this.proofAlgID = proofAlgID;
        this.macAlgId = macAlgId;
        this.witness = Arrays.clone(array);
    }
    
    public static IdentityProofV2 getInstance(final Object o) {
        if (o instanceof IdentityProofV2) {
            return (IdentityProofV2)o;
        }
        if (o != null) {
            return new IdentityProofV2(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getMacAlgId() {
        return this.macAlgId;
    }
    
    public AlgorithmIdentifier getProofAlgID() {
        return this.proofAlgID;
    }
    
    public byte[] getWitness() {
        return Arrays.clone(this.witness);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.proofAlgID);
        asn1EncodableVector.add(this.macAlgId);
        asn1EncodableVector.add(new DEROctetString(this.getWitness()));
        return new DERSequence(asn1EncodableVector);
    }
}
