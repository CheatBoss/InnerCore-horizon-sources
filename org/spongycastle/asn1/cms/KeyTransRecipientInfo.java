package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class KeyTransRecipientInfo extends ASN1Object
{
    private ASN1OctetString encryptedKey;
    private AlgorithmIdentifier keyEncryptionAlgorithm;
    private RecipientIdentifier rid;
    private ASN1Integer version;
    
    public KeyTransRecipientInfo(final ASN1Sequence asn1Sequence) {
        this.version = (ASN1Integer)asn1Sequence.getObjectAt(0);
        this.rid = RecipientIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(2));
        this.encryptedKey = (ASN1OctetString)asn1Sequence.getObjectAt(3);
    }
    
    public KeyTransRecipientInfo(final RecipientIdentifier rid, final AlgorithmIdentifier keyEncryptionAlgorithm, final ASN1OctetString encryptedKey) {
        ASN1Integer version;
        if (rid.toASN1Primitive() instanceof ASN1TaggedObject) {
            version = new ASN1Integer(2L);
        }
        else {
            version = new ASN1Integer(0L);
        }
        this.version = version;
        this.rid = rid;
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        this.encryptedKey = encryptedKey;
    }
    
    public static KeyTransRecipientInfo getInstance(final Object o) {
        if (o instanceof KeyTransRecipientInfo) {
            return (KeyTransRecipientInfo)o;
        }
        if (o != null) {
            return new KeyTransRecipientInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1OctetString getEncryptedKey() {
        return this.encryptedKey;
    }
    
    public AlgorithmIdentifier getKeyEncryptionAlgorithm() {
        return this.keyEncryptionAlgorithm;
    }
    
    public RecipientIdentifier getRecipientIdentifier() {
        return this.rid;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.rid);
        asn1EncodableVector.add(this.keyEncryptionAlgorithm);
        asn1EncodableVector.add(this.encryptedKey);
        return new DERSequence(asn1EncodableVector);
    }
}
