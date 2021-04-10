package org.spongycastle.asn1.cms.ecc;

import org.spongycastle.asn1.cms.*;
import org.spongycastle.asn1.*;

public class MQVuserKeyingMaterial extends ASN1Object
{
    private ASN1OctetString addedukm;
    private OriginatorPublicKey ephemeralPublicKey;
    
    private MQVuserKeyingMaterial(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 1 && asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Sequence has incorrect number of elements");
        }
        this.ephemeralPublicKey = OriginatorPublicKey.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.addedukm = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true);
        }
    }
    
    public MQVuserKeyingMaterial(final OriginatorPublicKey ephemeralPublicKey, final ASN1OctetString addedukm) {
        if (ephemeralPublicKey != null) {
            this.ephemeralPublicKey = ephemeralPublicKey;
            this.addedukm = addedukm;
            return;
        }
        throw new IllegalArgumentException("Ephemeral public key cannot be null");
    }
    
    public static MQVuserKeyingMaterial getInstance(final Object o) {
        if (o instanceof MQVuserKeyingMaterial) {
            return (MQVuserKeyingMaterial)o;
        }
        if (o != null) {
            return new MQVuserKeyingMaterial(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static MQVuserKeyingMaterial getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1OctetString getAddedukm() {
        return this.addedukm;
    }
    
    public OriginatorPublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.ephemeralPublicKey);
        if (this.addedukm != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.addedukm));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
