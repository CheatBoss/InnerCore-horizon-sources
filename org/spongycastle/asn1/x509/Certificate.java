package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class Certificate extends ASN1Object
{
    ASN1Sequence seq;
    DERBitString sig;
    AlgorithmIdentifier sigAlgId;
    TBSCertificate tbsCert;
    
    private Certificate(final ASN1Sequence seq) {
        this.seq = seq;
        if (seq.size() == 3) {
            this.tbsCert = TBSCertificate.getInstance(seq.getObjectAt(0));
            this.sigAlgId = AlgorithmIdentifier.getInstance(seq.getObjectAt(1));
            this.sig = DERBitString.getInstance(seq.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("sequence wrong size for a certificate");
    }
    
    public static Certificate getInstance(final Object o) {
        if (o instanceof Certificate) {
            return (Certificate)o;
        }
        if (o != null) {
            return new Certificate(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static Certificate getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public Time getEndDate() {
        return this.tbsCert.getEndDate();
    }
    
    public X500Name getIssuer() {
        return this.tbsCert.getIssuer();
    }
    
    public ASN1Integer getSerialNumber() {
        return this.tbsCert.getSerialNumber();
    }
    
    public DERBitString getSignature() {
        return this.sig;
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.sigAlgId;
    }
    
    public Time getStartDate() {
        return this.tbsCert.getStartDate();
    }
    
    public X500Name getSubject() {
        return this.tbsCert.getSubject();
    }
    
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.tbsCert.getSubjectPublicKeyInfo();
    }
    
    public TBSCertificate getTBSCertificate() {
        return this.tbsCert;
    }
    
    public ASN1Integer getVersion() {
        return this.tbsCert.getVersion();
    }
    
    public int getVersionNumber() {
        return this.tbsCert.getVersionNumber();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
