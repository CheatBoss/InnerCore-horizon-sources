package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class CertificatePolicies extends ASN1Object
{
    private final PolicyInformation[] policyInformation;
    
    private CertificatePolicies(final ASN1Sequence asn1Sequence) {
        this.policyInformation = new PolicyInformation[asn1Sequence.size()];
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            this.policyInformation[i] = PolicyInformation.getInstance(asn1Sequence.getObjectAt(i));
        }
    }
    
    public CertificatePolicies(final PolicyInformation policyInformation) {
        this.policyInformation = new PolicyInformation[] { policyInformation };
    }
    
    public CertificatePolicies(final PolicyInformation[] policyInformation) {
        this.policyInformation = policyInformation;
    }
    
    public static CertificatePolicies fromExtensions(final Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.certificatePolicies));
    }
    
    public static CertificatePolicies getInstance(final Object o) {
        if (o instanceof CertificatePolicies) {
            return (CertificatePolicies)o;
        }
        if (o != null) {
            return new CertificatePolicies(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static CertificatePolicies getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public PolicyInformation getPolicyInformation(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        int n = 0;
        while (true) {
            final PolicyInformation[] policyInformation = this.policyInformation;
            if (n == policyInformation.length) {
                return null;
            }
            if (asn1ObjectIdentifier.equals(policyInformation[n].getPolicyIdentifier())) {
                return this.policyInformation[n];
            }
            ++n;
        }
    }
    
    public PolicyInformation[] getPolicyInformation() {
        final PolicyInformation[] policyInformation = this.policyInformation;
        final PolicyInformation[] array = new PolicyInformation[policyInformation.length];
        System.arraycopy(policyInformation, 0, array, 0, policyInformation.length);
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.policyInformation);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.policyInformation.length; ++i) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(this.policyInformation[i]);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("CertificatePolicies: [");
        sb2.append((Object)sb);
        sb2.append("]");
        return sb2.toString();
    }
}
