package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class V3TBSCertificateGenerator
{
    private boolean altNamePresentAndCritical;
    Time endDate;
    Extensions extensions;
    X500Name issuer;
    private DERBitString issuerUniqueID;
    ASN1Integer serialNumber;
    AlgorithmIdentifier signature;
    Time startDate;
    X500Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;
    private DERBitString subjectUniqueID;
    DERTaggedObject version;
    
    public V3TBSCertificateGenerator() {
        this.version = new DERTaggedObject(true, 0, new ASN1Integer(2L));
    }
    
    public TBSCertificate generateTBSCertificate() {
        if (this.serialNumber != null && this.signature != null && this.issuer != null && this.startDate != null && this.endDate != null && (this.subject != null || this.altNamePresentAndCritical) && this.subjectPublicKeyInfo != null) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            asn1EncodableVector.add(this.version);
            asn1EncodableVector.add(this.serialNumber);
            asn1EncodableVector.add(this.signature);
            asn1EncodableVector.add(this.issuer);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(this.startDate);
            asn1EncodableVector2.add(this.endDate);
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
            ASN1Object subject = this.subject;
            if (subject == null) {
                subject = new DERSequence();
            }
            asn1EncodableVector.add(subject);
            asn1EncodableVector.add(this.subjectPublicKeyInfo);
            if (this.issuerUniqueID != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 1, this.issuerUniqueID));
            }
            if (this.subjectUniqueID != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 2, this.subjectUniqueID));
            }
            if (this.extensions != null) {
                asn1EncodableVector.add(new DERTaggedObject(true, 3, this.extensions));
            }
            return TBSCertificate.getInstance(new DERSequence(asn1EncodableVector));
        }
        throw new IllegalStateException("not all mandatory fields set in V3 TBScertificate generator");
    }
    
    public void setEndDate(final ASN1UTCTime asn1UTCTime) {
        this.endDate = new Time(asn1UTCTime);
    }
    
    public void setEndDate(final Time endDate) {
        this.endDate = endDate;
    }
    
    public void setExtensions(final Extensions extensions) {
        this.extensions = extensions;
        if (extensions != null) {
            final Extension extension = extensions.getExtension(Extension.subjectAlternativeName);
            if (extension != null && extension.isCritical()) {
                this.altNamePresentAndCritical = true;
            }
        }
    }
    
    public void setExtensions(final X509Extensions x509Extensions) {
        this.setExtensions(Extensions.getInstance(x509Extensions));
    }
    
    public void setIssuer(final X500Name issuer) {
        this.issuer = issuer;
    }
    
    public void setIssuer(final X509Name x509Name) {
        this.issuer = X500Name.getInstance(x509Name);
    }
    
    public void setIssuerUniqueID(final DERBitString issuerUniqueID) {
        this.issuerUniqueID = issuerUniqueID;
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
    
    public void setSubjectUniqueID(final DERBitString subjectUniqueID) {
        this.subjectUniqueID = subjectUniqueID;
    }
}
