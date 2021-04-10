package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class KeyAgreeRecipientInfo extends ASN1Object
{
    private AlgorithmIdentifier keyEncryptionAlgorithm;
    private OriginatorIdentifierOrKey originator;
    private ASN1Sequence recipientEncryptedKeys;
    private ASN1OctetString ukm;
    private ASN1Integer version;
    
    public KeyAgreeRecipientInfo(final ASN1Sequence asn1Sequence) {
        this.version = (ASN1Integer)asn1Sequence.getObjectAt(0);
        this.originator = OriginatorIdentifierOrKey.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true);
        int n = 2;
        if (asn1Sequence.getObjectAt(2) instanceof ASN1TaggedObject) {
            this.ukm = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(2), true);
            n = 3;
        }
        this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n));
        this.recipientEncryptedKeys = (ASN1Sequence)asn1Sequence.getObjectAt(n + 1);
    }
    
    public KeyAgreeRecipientInfo(final OriginatorIdentifierOrKey originator, final ASN1OctetString ukm, final AlgorithmIdentifier keyEncryptionAlgorithm, final ASN1Sequence recipientEncryptedKeys) {
        this.version = new ASN1Integer(3L);
        this.originator = originator;
        this.ukm = ukm;
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        this.recipientEncryptedKeys = recipientEncryptedKeys;
    }
    
    public static KeyAgreeRecipientInfo getInstance(final Object o) {
        if (o instanceof KeyAgreeRecipientInfo) {
            return (KeyAgreeRecipientInfo)o;
        }
        if (o != null) {
            return new KeyAgreeRecipientInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static KeyAgreeRecipientInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public AlgorithmIdentifier getKeyEncryptionAlgorithm() {
        return this.keyEncryptionAlgorithm;
    }
    
    public OriginatorIdentifierOrKey getOriginator() {
        return this.originator;
    }
    
    public ASN1Sequence getRecipientEncryptedKeys() {
        return this.recipientEncryptedKeys;
    }
    
    public ASN1OctetString getUserKeyingMaterial() {
        return this.ukm;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(new DERTaggedObject(true, 0, this.originator));
        if (this.ukm != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.ukm));
        }
        asn1EncodableVector.add(this.keyEncryptionAlgorithm);
        asn1EncodableVector.add(this.recipientEncryptedKeys);
        return new DERSequence(asn1EncodableVector);
    }
}
