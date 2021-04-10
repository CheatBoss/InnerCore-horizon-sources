package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;
import java.util.*;

public class TBSCertList extends ASN1Object
{
    Extensions crlExtensions;
    X500Name issuer;
    Time nextUpdate;
    ASN1Sequence revokedCertificates;
    AlgorithmIdentifier signature;
    Time thisUpdate;
    ASN1Integer version;
    
    public TBSCertList(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 3 && asn1Sequence.size() <= 7) {
            int n = 0;
            if (asn1Sequence.getObjectAt(0) instanceof ASN1Integer) {
                this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
                n = 1;
            }
            else {
                this.version = null;
            }
            final int n2 = n + 1;
            this.signature = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n));
            final int n3 = n2 + 1;
            this.issuer = X500Name.getInstance(asn1Sequence.getObjectAt(n2));
            final int n4 = n3 + 1;
            this.thisUpdate = Time.getInstance(asn1Sequence.getObjectAt(n3));
            int n5 = n4;
            Label_0165: {
                if (n4 < asn1Sequence.size()) {
                    if (!(asn1Sequence.getObjectAt(n4) instanceof ASN1UTCTime) && !(asn1Sequence.getObjectAt(n4) instanceof ASN1GeneralizedTime)) {
                        n5 = n4;
                        if (!(asn1Sequence.getObjectAt(n4) instanceof Time)) {
                            break Label_0165;
                        }
                    }
                    this.nextUpdate = Time.getInstance(asn1Sequence.getObjectAt(n4));
                    n5 = n4 + 1;
                }
            }
            int n6;
            if ((n6 = n5) < asn1Sequence.size()) {
                n6 = n5;
                if (!(asn1Sequence.getObjectAt(n5) instanceof ASN1TaggedObject)) {
                    this.revokedCertificates = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(n5));
                    n6 = n5 + 1;
                }
            }
            if (n6 < asn1Sequence.size() && asn1Sequence.getObjectAt(n6) instanceof ASN1TaggedObject) {
                this.crlExtensions = Extensions.getInstance(ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n6), true));
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static TBSCertList getInstance(final Object o) {
        if (o instanceof TBSCertList) {
            return (TBSCertList)o;
        }
        if (o != null) {
            return new TBSCertList(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static TBSCertList getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public Extensions getExtensions() {
        return this.crlExtensions;
    }
    
    public X500Name getIssuer() {
        return this.issuer;
    }
    
    public Time getNextUpdate() {
        return this.nextUpdate;
    }
    
    public Enumeration getRevokedCertificateEnumeration() {
        if (this.revokedCertificates == null) {
            return new EmptyEnumeration();
        }
        return new RevokedCertificatesEnumeration(this.revokedCertificates.getObjects());
    }
    
    public CRLEntry[] getRevokedCertificates() {
        final ASN1Sequence revokedCertificates = this.revokedCertificates;
        int i = 0;
        if (revokedCertificates == null) {
            return new CRLEntry[0];
        }
        final int size = revokedCertificates.size();
        final CRLEntry[] array = new CRLEntry[size];
        while (i < size) {
            array[i] = CRLEntry.getInstance(this.revokedCertificates.getObjectAt(i));
            ++i;
        }
        return array;
    }
    
    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }
    
    public Time getThisUpdate() {
        return this.thisUpdate;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    public int getVersionNumber() {
        final ASN1Integer version = this.version;
        if (version == null) {
            return 1;
        }
        return version.getValue().intValue() + 1;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1Integer version = this.version;
        if (version != null) {
            asn1EncodableVector.add(version);
        }
        asn1EncodableVector.add(this.signature);
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.thisUpdate);
        final Time nextUpdate = this.nextUpdate;
        if (nextUpdate != null) {
            asn1EncodableVector.add(nextUpdate);
        }
        final ASN1Sequence revokedCertificates = this.revokedCertificates;
        if (revokedCertificates != null) {
            asn1EncodableVector.add(revokedCertificates);
        }
        if (this.crlExtensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.crlExtensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    public static class CRLEntry extends ASN1Object
    {
        Extensions crlEntryExtensions;
        ASN1Sequence seq;
        
        private CRLEntry(final ASN1Sequence seq) {
            if (seq.size() >= 2 && seq.size() <= 3) {
                this.seq = seq;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(seq.size());
            throw new IllegalArgumentException(sb.toString());
        }
        
        public static CRLEntry getInstance(final Object o) {
            if (o instanceof CRLEntry) {
                return (CRLEntry)o;
            }
            if (o != null) {
                return new CRLEntry(ASN1Sequence.getInstance(o));
            }
            return null;
        }
        
        public Extensions getExtensions() {
            if (this.crlEntryExtensions == null && this.seq.size() == 3) {
                this.crlEntryExtensions = Extensions.getInstance(this.seq.getObjectAt(2));
            }
            return this.crlEntryExtensions;
        }
        
        public Time getRevocationDate() {
            return Time.getInstance(this.seq.getObjectAt(1));
        }
        
        public ASN1Integer getUserCertificate() {
            return ASN1Integer.getInstance(this.seq.getObjectAt(0));
        }
        
        public boolean hasExtensions() {
            return this.seq.size() == 3;
        }
        
        @Override
        public ASN1Primitive toASN1Primitive() {
            return this.seq;
        }
    }
    
    private class EmptyEnumeration implements Enumeration
    {
        @Override
        public boolean hasMoreElements() {
            return false;
        }
        
        @Override
        public Object nextElement() {
            throw new NoSuchElementException("Empty Enumeration");
        }
    }
    
    private class RevokedCertificatesEnumeration implements Enumeration
    {
        private final Enumeration en;
        
        RevokedCertificatesEnumeration(final Enumeration en) {
            this.en = en;
        }
        
        @Override
        public boolean hasMoreElements() {
            return this.en.hasMoreElements();
        }
        
        @Override
        public Object nextElement() {
            return CRLEntry.getInstance(this.en.nextElement());
        }
    }
}
