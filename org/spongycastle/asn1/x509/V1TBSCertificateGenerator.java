package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class V1TBSCertificateGenerator
{
    Time endDate;
    X500Name issuer;
    ASN1Integer serialNumber;
    AlgorithmIdentifier signature;
    Time startDate;
    X500Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;
    DERTaggedObject version;
    
    public V1TBSCertificateGenerator() {
        this.version = new DERTaggedObject(true, 0, new ASN1Integer(0L));
    }
    
    public TBSCertificate generateTBSCertificate() {
        if (this.serialNumber != null && this.signature != null && this.issuer != null && this.startDate != null && this.endDate != null && this.subject != null && this.subjectPublicKeyInfo != null) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            asn1EncodableVector.add(this.serialNumber);
            asn1EncodableVector.add(this.signature);
            asn1EncodableVector.add(this.issuer);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(this.startDate);
            asn1EncodableVector2.add(this.endDate);
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
            asn1EncodableVector.add(this.subject);
            asn1EncodableVector.add(this.subjectPublicKeyInfo);
            return TBSCertificate.getInstance(new DERSequence(asn1EncodableVector));
        }
        throw new IllegalStateException("not all mandatory fields set in V1 TBScertificate generator");
    }
    
    public void setEndDate(final ASN1UTCTime asn1UTCTime) {
        this.endDate = new Time(asn1UTCTime);
    }
    
    public void setEndDate(final Time endDate) {
        this.endDate = endDate;
    }
    
    public void setIssuer(final X500Name issuer) {
        this.issuer = issuer;
    }
    
    public void setIssuer(final X509Name x509Name) {
        this.issuer = X500Name.getInstance(x509Name.toASN1Primitive());
    }
    
    public void setSerialNumber(final ASN1Integer serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public void setSignature(final AlgorithmIdentifier signature) {
        this.signature = signature;
    }
    
    public void setStartDate(final ASN1UTCTime asn1UTCTime) {
        this.startDate = new Time(asn1UTCTime);
    }
    
    public void setStartDate(final Time startDate) {
        this.startDate = startDate;
    }
    
    public void setSubject(final X500Name subject) {
        this.subject = subject;
    }
    
    public void setSubject(final X509Name x509Name) {
        this.subject = X500Name.getInstance(x509Name.toASN1Primitive());
    }
    
    public void setSubjectPublicKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.subjectPublicKeyInfo = subjectPublicKeyInfo;
    }
}
