package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class KEKRecipientInfo extends ASN1Object
{
    private ASN1OctetString encryptedKey;
    private KEKIdentifier kekid;
    private AlgorithmIdentifier keyEncryptionAlgorithm;
    private ASN1Integer version;
    
    public KEKRecipientInfo(final ASN1Sequence asn1Sequence) {
        this.version = (ASN1Integer)asn1Sequence.getObjectAt(0);
        this.kekid = KEKIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(2));
        this.encryptedKey = (ASN1OctetString)asn1Sequence.getObjectAt(3);
    }
    
    public KEKRecipientInfo(final KEKIdentifier kekid, final AlgorithmIdentifier keyEncryptionAlgorithm, final ASN1OctetString encryptedKey) {
        this.version = new ASN1Integer(4L);
        this.kekid = kekid;
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        this.encryptedKey = encryptedKey;
    }
    
    public static KEKRecipientInfo getInstance(final Object o) {
        if (o instanceof KEKRecipientInfo) {
            return (KEKRecipientInfo)o;
        }
        if (o != null) {
            return new KEKRecipientInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static KEKRecipientInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1OctetString getEncryptedKey() {
        return this.encryptedKey;
    }
    
    public KEKIdentifier getKekid() {
        return this.kekid;
    }
    
    public AlgorithmIdentifier getKeyEncryptionAlgorithm() {
        return this.keyEncryptionAlgorithm;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.kekid);
        asn1EncodableVector.add(this.keyEncryptionAlgorithm);
        asn1EncodableVector.add(this.encryptedKey);
        return new DERSequence(asn1EncodableVector);
    }
}
