package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class LraPopWitness extends ASN1Object
{
    private final ASN1Sequence bodyIds;
    private final BodyPartID pkiDataBodyid;
    
    private LraPopWitness(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.pkiDataBodyid = BodyPartID.getInstance(asn1Sequence.getObjectAt(0));
            this.bodyIds = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public LraPopWitness(final BodyPartID pkiDataBodyid, final ASN1Sequence bodyIds) {
        this.pkiDataBodyid = pkiDataBodyid;
        this.bodyIds = bodyIds;
    }
    
    public static LraPopWitness getInstance(final Object o) {
        if (o instanceof LraPopWitness) {
            return (LraPopWitness)o;
        }
        if (o != null) {
            return new LraPopWitness(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BodyPartID[] getBodyIds() {
        final BodyPartID[] array = new BodyPartID[this.bodyIds.size()];
        for (int i = 0; i != this.bodyIds.size(); ++i) {
            array[i] = BodyPartID.getInstance(this.bodyIds.getObjectAt(i));
        }
        return array;
    }
    
    public BodyPartID getPkiDataBodyid() {
        return this.pkiDataBodyid;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pkiDataBodyid);
        asn1EncodableVector.add(this.bodyIds);
        return new DERSequence(asn1EncodableVector);
    }
}
