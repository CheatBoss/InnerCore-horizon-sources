package org.spongycastle.asn1.ess;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class OtherSigningCertificate extends ASN1Object
{
    ASN1Sequence certs;
    ASN1Sequence policies;
    
    private OtherSigningCertificate(final ASN1Sequence asn1Sequence) {
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
    
    public OtherSigningCertificate(final OtherCertID otherCertID) {
        this.certs = new DERSequence(otherCertID);
    }
    
    public static OtherSigningCertificate getInstance(final Object o) {
        if (o instanceof OtherSigningCertificate) {
            return (OtherSigningCertificate)o;
        }
        if (o != null) {
            return new OtherSigningCertificate(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public OtherCertID[] getCerts() {
        final OtherCertID[] array = new OtherCertID[this.certs.size()];
        for (int i = 0; i != this.certs.size(); ++i) {
            array[i] = OtherCertID.getInstance(this.certs.getObjectAt(i));
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
