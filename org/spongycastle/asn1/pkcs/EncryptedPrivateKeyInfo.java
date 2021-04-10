package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class EncryptedPrivateKeyInfo extends ASN1Object
{
    private AlgorithmIdentifier algId;
    private ASN1OctetString data;
    
    private EncryptedPrivateKeyInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.algId = AlgorithmIdentifier.getInstance(objects.nextElement());
        this.data = ASN1OctetString.getInstance(objects.nextElement());
    }
    
    public EncryptedPrivateKeyInfo(final AlgorithmIdentifier algId, final byte[] array) {
        this.algId = algId;
        this.data = new DEROctetString(array);
    }
    
    public static EncryptedPrivateKeyInfo getInstance(final Object o) {
        if (o instanceof EncryptedPrivateKeyInfo) {
            return (EncryptedPrivateKeyInfo)o;
        }
        if (o != null) {
            return new EncryptedPrivateKeyInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getEncryptedData() {
        return this.data.getOctets();
    }
    
    public AlgorithmIdentifier getEncryptionAlgorithm() {
        return this.algId;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.algId);
        asn1EncodableVector.add(this.data);
        return new DERSequence(asn1EncodableVector);
    }
}
