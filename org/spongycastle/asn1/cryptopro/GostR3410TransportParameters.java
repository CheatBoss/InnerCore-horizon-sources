package org.spongycastle.asn1.cryptopro;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class GostR3410TransportParameters extends ASN1Object
{
    private final ASN1ObjectIdentifier encryptionParamSet;
    private final SubjectPublicKeyInfo ephemeralPublicKey;
    private final byte[] ukm;
    
    public GostR3410TransportParameters(final ASN1ObjectIdentifier encryptionParamSet, final SubjectPublicKeyInfo ephemeralPublicKey, final byte[] array) {
        this.encryptionParamSet = encryptionParamSet;
        this.ephemeralPublicKey = ephemeralPublicKey;
        this.ukm = Arrays.clone(array);
    }
    
    private GostR3410TransportParameters(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.encryptionParamSet = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.ukm = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1)).getOctets();
            this.ephemeralPublicKey = null;
            return;
        }
        if (asn1Sequence.size() == 3) {
            this.encryptionParamSet = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.ephemeralPublicKey = SubjectPublicKeyInfo.getInstance(ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1)), false);
            this.ukm = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(2)).getOctets();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown sequence length: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static GostR3410TransportParameters getInstance(final Object o) {
        if (o instanceof GostR3410TransportParameters) {
            return (GostR3410TransportParameters)o;
        }
        if (o != null) {
            return new GostR3410TransportParameters(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static GostR3410TransportParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return new GostR3410TransportParameters(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1ObjectIdentifier getEncryptionParamSet() {
        return this.encryptionParamSet;
    }
    
    public SubjectPublicKeyInfo getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
    
    public byte[] getUkm() {
        return Arrays.clone(this.ukm);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.encryptionParamSet);
        if (this.ephemeralPublicKey != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.ephemeralPublicKey));
        }
        asn1EncodableVector.add(new DEROctetString(this.ukm));
        return new DERSequence(asn1EncodableVector);
    }
}
