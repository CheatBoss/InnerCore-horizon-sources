package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.cms.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class EncryptedPOP extends ASN1Object
{
    private final ContentInfo cms;
    private final TaggedRequest request;
    private final AlgorithmIdentifier thePOPAlgID;
    private final byte[] witness;
    private final AlgorithmIdentifier witnessAlgID;
    
    private EncryptedPOP(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 5) {
            this.request = TaggedRequest.getInstance(asn1Sequence.getObjectAt(0));
            this.cms = ContentInfo.getInstance(asn1Sequence.getObjectAt(1));
            this.thePOPAlgID = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(2));
            this.witnessAlgID = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(3));
            this.witness = Arrays.clone(ASN1OctetString.getInstance(asn1Sequence.getObjectAt(4)).getOctets());
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public EncryptedPOP(final TaggedRequest request, final ContentInfo cms, final AlgorithmIdentifier thePOPAlgID, final AlgorithmIdentifier witnessAlgID, final byte[] array) {
        this.request = request;
        this.cms = cms;
        this.thePOPAlgID = thePOPAlgID;
        this.witnessAlgID = witnessAlgID;
        this.witness = Arrays.clone(array);
    }
    
    public static EncryptedPOP getInstance(final Object o) {
        if (o instanceof EncryptedPOP) {
            return (EncryptedPOP)o;
        }
        if (o != null) {
            return new EncryptedPOP(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ContentInfo getCms() {
        return this.cms;
    }
    
    public TaggedRequest getRequest() {
        return this.request;
    }
    
    public AlgorithmIdentifier getThePOPAlgID() {
        return this.thePOPAlgID;
    }
    
    public byte[] getWitness() {
        return Arrays.clone(this.witness);
    }
    
    public AlgorithmIdentifier getWitnessAlgID() {
        return this.witnessAlgID;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.request);
        asn1EncodableVector.add(this.cms);
        asn1EncodableVector.add(this.thePOPAlgID);
        asn1EncodableVector.add(this.witnessAlgID);
        asn1EncodableVector.add(new DEROctetString(this.witness));
        return new DERSequence(asn1EncodableVector);
    }
}
