package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class CertificateList extends ASN1Object
{
    int hashCodeValue;
    boolean isHashCodeSet;
    DERBitString sig;
    AlgorithmIdentifier sigAlgId;
    TBSCertList tbsCertList;
    
    public CertificateList(final ASN1Sequence asn1Sequence) {
        this.isHashCodeSet = false;
        if (asn1Sequence.size() == 3) {
            this.tbsCertList = TBSCertList.getInstance(asn1Sequence.getObjectAt(0));
            this.sigAlgId = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.sig = DERBitString.getInstance(asn1Sequence.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("sequence wrong size for CertificateList");
    }
    
    public static CertificateList getInstance(final Object o) {
        if (o instanceof CertificateList) {
            return (CertificateList)o;
        }
        if (o != null) {
            return new CertificateList(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static CertificateList getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public X500Name getIssuer() {
        return this.tbsCertList.getIssuer();
    }
    
    public Time getNextUpdate() {
        return this.tbsCertList.getNextUpdate();
    }
    
    public Enumeration getRevokedCertificateEnumeration() {
        return this.tbsCertList.getRevokedCertificateEnumeration();
    }
    
    public TBSCertList.CRLEntry[] getRevokedCertificates() {
        return this.tbsCertList.getRevokedCertificates();
    }
    
    public DERBitString getSignature() {
        return this.sig;
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.sigAlgId;
    }
    
    public TBSCertList getTBSCertList() {
        return this.tbsCertList;
    }
    
    public Time getThisUpdate() {
        return this.tbsCertList.getThisUpdate();
    }
    
    public int getVersionNumber() {
        return this.tbsCertList.getVersionNumber();
    }
    
    @Override
    public int hashCode() {
        if (!this.isHashCodeSet) {
            this.hashCodeValue = super.hashCode();
            this.isHashCodeSet = true;
        }
        return this.hashCodeValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.tbsCertList);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(this.sig);
        return new DERSequence(asn1EncodableVector);
    }
}
