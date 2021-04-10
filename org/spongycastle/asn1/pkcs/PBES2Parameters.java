package org.spongycastle.asn1.pkcs;

import java.util.*;
import org.spongycastle.asn1.*;

public class PBES2Parameters extends ASN1Object implements PKCSObjectIdentifiers
{
    private KeyDerivationFunc func;
    private EncryptionScheme scheme;
    
    private PBES2Parameters(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        final ASN1Sequence instance = ASN1Sequence.getInstance(objects.nextElement().toASN1Primitive());
        if (instance.getObjectAt(0).equals(PBES2Parameters.id_PBKDF2)) {
            this.func = new KeyDerivationFunc(PBES2Parameters.id_PBKDF2, PBKDF2Params.getInstance(instance.getObjectAt(1)));
        }
        else {
            this.func = KeyDerivationFunc.getInstance(instance);
        }
        this.scheme = EncryptionScheme.getInstance(objects.nextElement());
    }
    
    public PBES2Parameters(final KeyDerivationFunc func, final EncryptionScheme scheme) {
        this.func = func;
        this.scheme = scheme;
    }
    
    public static PBES2Parameters getInstance(final Object o) {
        if (o instanceof PBES2Parameters) {
            return (PBES2Parameters)o;
        }
        if (o != null) {
            return new PBES2Parameters(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public EncryptionScheme getEncryptionScheme() {
        return this.scheme;
    }
    
    public KeyDerivationFunc getKeyDerivationFunc() {
        return this.func;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.func);
        asn1EncodableVector.add(this.scheme);
        return new DERSequence(asn1EncodableVector);
    }
}
