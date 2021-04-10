package org.spongycastle.asn1.bc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class EncryptedSecretKeyData extends ASN1Object
{
    private final ASN1OctetString encryptedKeyData;
    private final AlgorithmIdentifier keyEncryptionAlgorithm;
    
    private EncryptedSecretKeyData(final ASN1Sequence asn1Sequence) {
        this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.encryptedKeyData = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public EncryptedSecretKeyData(final AlgorithmIdentifier keyEncryptionAlgorithm, final byte[] array) {
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        this.encryptedKeyData = new DEROctetString(Arrays.clone(array));
    }
    
    public static EncryptedSecretKeyData getInstance(final Object o) {
        if (o instanceof EncryptedSecretKeyData) {
            return (EncryptedSecretKeyData)o;
        }
        if (o != null) {
            return new EncryptedSecretKeyData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getEncryptedKeyData() {
        return Arrays.clone(this.encryptedKeyData.getOctets());
    }
    
    public AlgorithmIdentifier getKeyEncryptionAlgorithm() {
        return this.keyEncryptionAlgorithm;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.keyEncryptionAlgorithm);
        asn1EncodableVector.add(this.encryptedKeyData);
        return new DERSequence(asn1EncodableVector);
    }
}
