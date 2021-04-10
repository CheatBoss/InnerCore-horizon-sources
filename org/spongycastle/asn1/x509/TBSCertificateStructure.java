package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;

public class TBSCertificateStructure extends ASN1Object implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    Time endDate;
    X509Extensions extensions;
    X500Name issuer;
    DERBitString issuerUniqueId;
    ASN1Sequence seq;
    ASN1Integer serialNumber;
    AlgorithmIdentifier signature;
    Time startDate;
    X500Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;
    DERBitString subjectUniqueId;
    ASN1Integer version;
    
    public TBSCertificateStructure(final ASN1Sequence seq) {
        this.seq = seq;
        int n;
        if (seq.getObjectAt(0) instanceof DERTaggedObject) {
            this.version = ASN1Integer.getInstance((ASN1TaggedObject)seq.getObjectAt(0), true);
            n = 0;
        }
        else {
            this.version = new ASN1Integer(0L);
            n = -1;
        }
        this.serialNumber = ASN1Integer.getInstance(seq.getObjectAt(n + 1));
        this.signature = AlgorithmIdentifier.getInstance(seq.getObjectAt(n + 2));
        this.issuer = X500Name.getInstance(seq.getObjectAt(n + 3));
        final ASN1Sequence asn1Sequence = (ASN1Sequence)seq.getObjectAt(n + 4);
        this.startDate = Time.getInstance(asn1Sequence.getObjectAt(0));
        this.endDate = Time.getInstance(asn1Sequence.getObjectAt(1));
        this.subject = X500Name.getInstance(seq.getObjectAt(n + 5));
        final int n2 = n + 6;
        this.subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(seq.getObjectAt(n2));
        for (int i = seq.size() - n2 - 1; i > 0; --i) {
            final DERTaggedObject derTaggedObject = (DERTaggedObject)seq.getObjectAt(n2 + i);
            final int tagNo = derTaggedObject.getTagNo();
            if (tagNo != 1) {
                if (tagNo != 2) {
                    if (tagNo == 3) {
                        this.extensions = X509Extensions.getInstance(derTaggedObject);
                    }
                }
                else {
                    this.subjectUniqueId = DERBitString.getInstance(derTaggedObject, false);
                }
            }
            else {
                this.issuerUniqueId = DERBitString.getInstance(derTaggedObject, false);
            }
        }
    }
    
    public static TBSCertificateStructure getInstance(final Object o) {
        if (o instanceof TBSCertificateStructure) {
            return (TBSCertificateStructure)o;
        }
        if (o != null) {
            return new TBSCertificateStructure(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static TBSCertificateStructure getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public Time getEndDate() {
        return this.endDate;
    }
    
    public X509Extensions getExtensions() {
        return this.extensions;
    }
    
    public X500Name getIssuer() {
        return this.issuer;
    }
    
    public DERBitString getIssuerUniqueId() {
        return this.issuerUniqueId;
    }
    
    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }
    
    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }
    
    public Time getStartDate() {
        return this.startDate;
    }
    
    public X500Name getSubject() {
        return this.subject;
    }
    
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.subjectPublicKeyInfo;
    }
    
    public DERBitString getSubjectUniqueId() {
        return this.subjectUniqueId;
    }
    
    public int getVersion() {
        return this.version.getValue().intValue() + 1;
    }
    
    public ASN1Integer getVersionNumber() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
