package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class EncryptedData extends ASN1Object
{
    private EncryptedContentInfo encryptedContentInfo;
    private ASN1Set unprotectedAttrs;
    private ASN1Integer version;
    
    private EncryptedData(final ASN1Sequence asn1Sequence) {
        this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        this.encryptedContentInfo = EncryptedContentInfo.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() == 3) {
            this.unprotectedAttrs = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(2), false);
        }
    }
    
    public EncryptedData(final EncryptedContentInfo encryptedContentInfo) {
        this(encryptedContentInfo, null);
    }
    
    public EncryptedData(final EncryptedContentInfo encryptedContentInfo, final ASN1Set unprotectedAttrs) {
        long n;
        if (unprotectedAttrs == null) {
            n = 0L;
        }
        else {
            n = 2L;
        }
        this.version = new ASN1Integer(n);
        this.encryptedContentInfo = encryptedContentInfo;
        this.unprotectedAttrs = unprotectedAttrs;
    }
    
    public static EncryptedData getInstance(final Object o) {
        if (o instanceof EncryptedData) {
            return (EncryptedData)o;
        }
        if (o != null) {
            return new EncryptedData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public EncryptedContentInfo getEncryptedContentInfo() {
        return this.encryptedContentInfo;
    }
    
    public ASN1Set getUnprotectedAttrs() {
        return this.unprotectedAttrs;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.encryptedContentInfo);
        if (this.unprotectedAttrs != null) {
            asn1EncodableVector.add(new BERTaggedObject(false, 1, this.unprotectedAttrs));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
