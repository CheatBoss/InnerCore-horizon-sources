package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class X509CertificateStructure extends ASN1Object implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    ASN1Sequence seq;
    DERBitString sig;
    AlgorithmIdentifier sigAlgId;
    TBSCertificateStructure tbsCert;
    
    public X509CertificateStructure(final ASN1Sequence seq) {
        this.seq = seq;
        if (seq.size() == 3) {
            this.tbsCert = TBSCertificateStructure.getInstance(seq.getObjectAt(0));
            this.sigAlgId = AlgorithmIdentifier.getInstance(seq.getObjectAt(1));
            this.sig = DERBitString.getInstance(seq.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("sequence wrong size for a certificate");
    }
    
    public static X509CertificateStructure getInstance(final Object o) {
        if (o instanceof X509CertificateStructure) {
            return (X509CertificateStructure)o;
        }
        if (o != null) {
            return new X509CertificateStructure(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static X509CertificateStructure getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
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
    
    public TBSCertificateStructure getTBSCertificate() {
        return this.tbsCert;
    }
    
    public int getVersion() {
        return this.tbsCert.getVersion();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
