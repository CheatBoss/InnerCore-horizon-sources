package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class DecryptedPOP extends ASN1Object
{
    private final BodyPartID bodyPartID;
    private final byte[] thePOP;
    private final AlgorithmIdentifier thePOPAlgID;
    
    private DecryptedPOP(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.bodyPartID = BodyPartID.getInstance(asn1Sequence.getObjectAt(0));
            this.thePOPAlgID = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.thePOP = Arrays.clone(ASN1OctetString.getInstance(asn1Sequence.getObjectAt(2)).getOctets());
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public DecryptedPOP(final BodyPartID bodyPartID, final AlgorithmIdentifier thePOPAlgID, final byte[] array) {
        this.bodyPartID = bodyPartID;
        this.thePOPAlgID = thePOPAlgID;
        this.thePOP = Arrays.clone(array);
    }
    
    public static DecryptedPOP getInstance(final Object o) {
        if (o instanceof DecryptedPOP) {
            return (DecryptedPOP)o;
        }
        if (o != null) {
            return new DecryptedPOP(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BodyPartID getBodyPartID() {
        return this.bodyPartID;
    }
    
    public byte[] getThePOP() {
        return Arrays.clone(this.thePOP);
    }
    
    public AlgorithmIdentifier getThePOPAlgID() {
        return this.thePOPAlgID;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.bodyPartID);
        asn1EncodableVector.add(this.thePOPAlgID);
        asn1EncodableVector.add(new DEROctetString(this.thePOP));
        return new DERSequence(asn1EncodableVector);
    }
}
