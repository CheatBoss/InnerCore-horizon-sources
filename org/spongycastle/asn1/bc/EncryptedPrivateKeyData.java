package org.spongycastle.asn1.bc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;

public class EncryptedPrivateKeyData extends ASN1Object
{
    private final Certificate[] certificateChain;
    private final EncryptedPrivateKeyInfo encryptedPrivateKeyInfo;
    
    private EncryptedPrivateKeyData(ASN1Sequence instance) {
        int n = 0;
        this.encryptedPrivateKeyInfo = EncryptedPrivateKeyInfo.getInstance(instance.getObjectAt(0));
        instance = ASN1Sequence.getInstance(instance.getObjectAt(1));
        this.certificateChain = new Certificate[instance.size()];
        while (true) {
            final Certificate[] certificateChain = this.certificateChain;
            if (n == certificateChain.length) {
                break;
            }
            certificateChain[n] = Certificate.getInstance(instance.getObjectAt(n));
            ++n;
        }
    }
    
    public EncryptedPrivateKeyData(final EncryptedPrivateKeyInfo encryptedPrivateKeyInfo, final Certificate[] array) {
        this.encryptedPrivateKeyInfo = encryptedPrivateKeyInfo;
        System.arraycopy(array, 0, this.certificateChain = new Certificate[array.length], 0, array.length);
    }
    
    public static EncryptedPrivateKeyData getInstance(final Object o) {
        if (o instanceof EncryptedPrivateKeyData) {
            return (EncryptedPrivateKeyData)o;
        }
        if (o != null) {
            return new EncryptedPrivateKeyData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Certificate[] getCertificateChain() {
        final Certificate[] certificateChain = this.certificateChain;
        final Certificate[] array = new Certificate[certificateChain.length];
        System.arraycopy(certificateChain, 0, array, 0, certificateChain.length);
        return array;
    }
    
    public EncryptedPrivateKeyInfo getEncryptedPrivateKeyInfo() {
        return this.encryptedPrivateKeyInfo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.encryptedPrivateKeyInfo);
        asn1EncodableVector.add(new DERSequence(this.certificateChain));
        return new DERSequence(asn1EncodableVector);
    }
}
