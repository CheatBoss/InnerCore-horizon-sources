package org.spongycastle.asn1.cryptopro;

import org.spongycastle.asn1.*;

public class GOST3410PublicKeyAlgParameters extends ASN1Object
{
    private ASN1ObjectIdentifier digestParamSet;
    private ASN1ObjectIdentifier encryptionParamSet;
    private ASN1ObjectIdentifier publicKeyParamSet;
    
    public GOST3410PublicKeyAlgParameters(final ASN1ObjectIdentifier publicKeyParamSet, final ASN1ObjectIdentifier digestParamSet) {
        this.publicKeyParamSet = publicKeyParamSet;
        this.digestParamSet = digestParamSet;
        this.encryptionParamSet = null;
    }
    
    public GOST3410PublicKeyAlgParameters(final ASN1ObjectIdentifier publicKeyParamSet, final ASN1ObjectIdentifier digestParamSet, final ASN1ObjectIdentifier encryptionParamSet) {
        this.publicKeyParamSet = publicKeyParamSet;
        this.digestParamSet = digestParamSet;
        this.encryptionParamSet = encryptionParamSet;
    }
    
    public GOST3410PublicKeyAlgParameters(final ASN1Sequence asn1Sequence) {
        this.publicKeyParamSet = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.digestParamSet = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(1);
        if (asn1Sequence.size() > 2) {
            this.encryptionParamSet = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(2);
        }
    }
    
    public static GOST3410PublicKeyAlgParameters getInstance(final Object o) {
        if (o instanceof GOST3410PublicKeyAlgParameters) {
            return (GOST3410PublicKeyAlgParameters)o;
        }
        if (o != null) {
            return new GOST3410PublicKeyAlgParameters(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static GOST3410PublicKeyAlgParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1ObjectIdentifier getDigestParamSet() {
        return this.digestParamSet;
    }
    
    public ASN1ObjectIdentifier getEncryptionParamSet() {
        return this.encryptionParamSet;
    }
    
    public ASN1ObjectIdentifier getPublicKeyParamSet() {
        return this.publicKeyParamSet;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.publicKeyParamSet);
        asn1EncodableVector.add(this.digestParamSet);
        final ASN1ObjectIdentifier encryptionParamSet = this.encryptionParamSet;
        if (encryptionParamSet != null) {
            asn1EncodableVector.add(encryptionParamSet);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
