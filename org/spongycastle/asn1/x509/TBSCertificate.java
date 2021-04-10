package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class TBSCertificate extends ASN1Object
{
    Time endDate;
    Extensions extensions;
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
    
    private TBSCertificate(final ASN1Sequence seq) {
        this.seq = seq;
        int n;
        if (seq.getObjectAt(0) instanceof ASN1TaggedObject) {
            this.version = ASN1Integer.getInstance((ASN1TaggedObject)seq.getObjectAt(0), true);
            n = 0;
        }
        else {
            this.version = new ASN1Integer(0L);
            n = -1;
        }
        boolean b = false;
        boolean b2 = false;
        Label_0126: {
            if (this.version.getValue().equals(BigInteger.valueOf(0L))) {
                b = true;
            }
            else {
                if (this.version.getValue().equals(BigInteger.valueOf(1L))) {
                    b = false;
                    b2 = true;
                    break Label_0126;
                }
                if (!this.version.getValue().equals(BigInteger.valueOf(2L))) {
                    throw new IllegalArgumentException("version number not recognised");
                }
                b = false;
            }
            b2 = false;
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
        int i;
        final int n3 = i = seq.size() - n2 - 1;
        if (n3 != 0) {
            if (b) {
                throw new IllegalArgumentException("version 1 certificate contains extra data");
            }
            i = n3;
        }
        while (i > 0) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)seq.getObjectAt(n2 + i);
            final int tagNo = asn1TaggedObject.getTagNo();
            if (tagNo != 1) {
                if (tagNo != 2) {
                    if (tagNo == 3) {
                        if (b2) {
                            throw new IllegalArgumentException("version 2 certificate cannot contain extensions");
                        }
                        this.extensions = Extensions.getInstance(ASN1Sequence.getInstance(asn1TaggedObject, true));
                    }
                }
                else {
                    this.subjectUniqueId = DERBitString.getInstance(asn1TaggedObject, false);
                }
            }
            else {
                this.issuerUniqueId = DERBitString.getInstance(asn1TaggedObject, false);
            }
            --i;
        }
    }
    
    public static TBSCertificate getInstance(final Object o) {
        if (o instanceof TBSCertificate) {
            return (TBSCertificate)o;
        }
        if (o != null) {
            return new TBSCertificate(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static TBSCertificate getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public Time getEndDate() {
        return this.endDate;
    }
    
    public Extensions getExtensions() {
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
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    public int getVersionNumber() {
        return this.version.getValue().intValue() + 1;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
