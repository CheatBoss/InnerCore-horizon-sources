package org.spongycastle.asn1.ess;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class SigningCertificateV2 extends ASN1Object
{
    ASN1Sequence certs;
    ASN1Sequence policies;
    
    private SigningCertificateV2(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1 && asn1Sequence.size() <= 2) {
            this.certs = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(0));
            if (asn1Sequence.size() > 1) {
                this.policies = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public SigningCertificateV2(final ESSCertIDv2 essCertIDv2) {
        this.certs = new DERSequence(essCertIDv2);
    }
    
    public SigningCertificateV2(final ESSCertIDv2[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.certs = new DERSequence(asn1EncodableVector);
    }
    
    public SigningCertificateV2(final ESSCertIDv2[] array, final PolicyInformation[] array2) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final int n = 0;
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.certs = new DERSequence(asn1EncodableVector);
        if (array2 != null) {
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            for (int j = n; j < array2.length; ++j) {
                asn1EncodableVector2.add(array2[j]);
            }
            this.policies = new DERSequence(asn1EncodableVector2);
        }
    }
    
    public static SigningCertificateV2 getInstance(final Object o) {
        if (o == null || o instanceof SigningCertificateV2) {
            return (SigningCertificateV2)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SigningCertificateV2((ASN1Sequence)o);
        }
        return null;
    }
    
    public ESSCertIDv2[] getCerts() {
        final ESSCertIDv2[] array = new ESSCertIDv2[this.certs.size()];
        for (int i = 0; i != this.certs.size(); ++i) {
            array[i] = ESSCertIDv2.getInstance(this.certs.getObjectAt(i));
        }
        return array;
    }
    
    public PolicyInformation[] getPolicies() {
        final ASN1Sequence policies = this.policies;
        if (policies == null) {
            return null;
        }
        final PolicyInformation[] array = new PolicyInformation[policies.size()];
        for (int i = 0; i != this.policies.size(); ++i) {
            array[i] = PolicyInformation.getInstance(this.policies.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certs);
        final ASN1Sequence policies = this.policies;
        if (policies != null) {
            asn1EncodableVector.add(policies);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
