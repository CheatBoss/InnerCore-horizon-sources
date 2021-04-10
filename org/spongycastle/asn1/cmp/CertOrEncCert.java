package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.crmf.*;
import org.spongycastle.asn1.*;

public class CertOrEncCert extends ASN1Object implements ASN1Choice
{
    private CMPCertificate certificate;
    private EncryptedValue encryptedCert;
    
    private CertOrEncCert(final ASN1TaggedObject asn1TaggedObject) {
        if (asn1TaggedObject.getTagNo() == 0) {
            this.certificate = CMPCertificate.getInstance(asn1TaggedObject.getObject());
            return;
        }
        if (asn1TaggedObject.getTagNo() == 1) {
            this.encryptedCert = EncryptedValue.getInstance(asn1TaggedObject.getObject());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown tag: ");
        sb.append(asn1TaggedObject.getTagNo());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public CertOrEncCert(final CMPCertificate certificate) {
        if (certificate != null) {
            this.certificate = certificate;
            return;
        }
        throw new IllegalArgumentException("'certificate' cannot be null");
    }
    
    public CertOrEncCert(final EncryptedValue encryptedCert) {
        if (encryptedCert != null) {
            this.encryptedCert = encryptedCert;
            return;
        }
        throw new IllegalArgumentException("'encryptedCert' cannot be null");
    }
    
    public static CertOrEncCert getInstance(final Object o) {
        if (o instanceof CertOrEncCert) {
            return (CertOrEncCert)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new CertOrEncCert((ASN1TaggedObject)o);
        }
        return null;
    }
    
    public CMPCertificate getCertificate() {
        return this.certificate;
    }
    
    public EncryptedValue getEncryptedCert() {
        return this.encryptedCert;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.certificate != null) {
            return new DERTaggedObject(true, 0, this.certificate);
        }
        return new DERTaggedObject(true, 1, this.encryptedCert);
    }
}
