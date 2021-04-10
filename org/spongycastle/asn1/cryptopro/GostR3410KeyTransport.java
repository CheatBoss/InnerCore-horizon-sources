package org.spongycastle.asn1.cryptopro;

import org.spongycastle.asn1.*;

public class GostR3410KeyTransport extends ASN1Object
{
    private final Gost2814789EncryptedKey sessionEncryptedKey;
    private final GostR3410TransportParameters transportParameters;
    
    private GostR3410KeyTransport(final ASN1Sequence asn1Sequence) {
        this.sessionEncryptedKey = Gost2814789EncryptedKey.getInstance(asn1Sequence.getObjectAt(0));
        this.transportParameters = GostR3410TransportParameters.getInstance(ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1)), false);
    }
    
    public static GostR3410KeyTransport getInstance(final Object o) {
        if (o instanceof GostR3410KeyTransport) {
            return (GostR3410KeyTransport)o;
        }
        if (o != null) {
            return new GostR3410KeyTransport(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Gost2814789EncryptedKey getSessionEncryptedKey() {
        return this.sessionEncryptedKey;
    }
    
    public GostR3410TransportParameters getTransportParameters() {
        return this.transportParameters;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.sessionEncryptedKey);
        if (this.transportParameters != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.transportParameters));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
