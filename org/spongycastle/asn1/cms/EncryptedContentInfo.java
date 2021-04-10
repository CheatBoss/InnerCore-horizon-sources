package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class EncryptedContentInfo extends ASN1Object
{
    private AlgorithmIdentifier contentEncryptionAlgorithm;
    private ASN1ObjectIdentifier contentType;
    private ASN1OctetString encryptedContent;
    
    public EncryptedContentInfo(final ASN1ObjectIdentifier contentType, final AlgorithmIdentifier contentEncryptionAlgorithm, final ASN1OctetString encryptedContent) {
        this.contentType = contentType;
        this.contentEncryptionAlgorithm = contentEncryptionAlgorithm;
        this.encryptedContent = encryptedContent;
    }
    
    private EncryptedContentInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 2) {
            this.contentType = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
            this.contentEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            if (asn1Sequence.size() > 2) {
                this.encryptedContent = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(2), false);
            }
            return;
        }
        throw new IllegalArgumentException("Truncated Sequence Found");
    }
    
    public static EncryptedContentInfo getInstance(final Object o) {
        if (o instanceof EncryptedContentInfo) {
            return (EncryptedContentInfo)o;
        }
        if (o != null) {
            return new EncryptedContentInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getContentEncryptionAlgorithm() {
        return this.contentEncryptionAlgorithm;
    }
    
    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }
    
    public ASN1OctetString getEncryptedContent() {
        return this.encryptedContent;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.contentType);
        asn1EncodableVector.add(this.contentEncryptionAlgorithm);
        if (this.encryptedContent != null) {
            asn1EncodableVector.add(new BERTaggedObject(false, 0, this.encryptedContent));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
