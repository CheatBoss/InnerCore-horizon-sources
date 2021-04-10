package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.crmf.*;
import org.spongycastle.asn1.*;

public class CertifiedKeyPair extends ASN1Object
{
    private CertOrEncCert certOrEncCert;
    private EncryptedValue privateKey;
    private PKIPublicationInfo publicationInfo;
    
    private CertifiedKeyPair(final ASN1Sequence asn1Sequence) {
        this.certOrEncCert = CertOrEncCert.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() >= 2) {
            if (asn1Sequence.size() == 2) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1));
                if (instance.getTagNo() == 0) {
                    this.privateKey = EncryptedValue.getInstance(instance.getObject());
                    return;
                }
                this.publicationInfo = PKIPublicationInfo.getInstance(instance.getObject());
            }
            else {
                this.privateKey = EncryptedValue.getInstance(ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1)));
                this.publicationInfo = PKIPublicationInfo.getInstance(ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(2)));
            }
        }
    }
    
    public CertifiedKeyPair(final CertOrEncCert certOrEncCert) {
        this(certOrEncCert, null, null);
    }
    
    public CertifiedKeyPair(final CertOrEncCert certOrEncCert, final EncryptedValue privateKey, final PKIPublicationInfo publicationInfo) {
        if (certOrEncCert != null) {
            this.certOrEncCert = certOrEncCert;
            this.privateKey = privateKey;
            this.publicationInfo = publicationInfo;
            return;
        }
        throw new IllegalArgumentException("'certOrEncCert' cannot be null");
    }
    
    public static CertifiedKeyPair getInstance(final Object o) {
        if (o instanceof CertifiedKeyPair) {
            return (CertifiedKeyPair)o;
        }
        if (o != null) {
            return new CertifiedKeyPair(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CertOrEncCert getCertOrEncCert() {
        return this.certOrEncCert;
    }
    
    public EncryptedValue getPrivateKey() {
        return this.privateKey;
    }
    
    public PKIPublicationInfo getPublicationInfo() {
        return this.publicationInfo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certOrEncCert);
        if (this.privateKey != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.privateKey));
        }
        if (this.publicationInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.publicationInfo));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
