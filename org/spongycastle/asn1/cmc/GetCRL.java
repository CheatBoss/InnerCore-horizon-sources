package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class GetCRL extends ASN1Object
{
    private GeneralName cRLName;
    private final X500Name issuerName;
    private ReasonFlags reasons;
    private ASN1GeneralizedTime time;
    
    private GetCRL(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        final boolean b = true;
        if (size >= 1 && asn1Sequence.size() <= 4) {
            this.issuerName = X500Name.getInstance(asn1Sequence.getObjectAt(0));
            int n = b ? 1 : 0;
            if (asn1Sequence.size() > 1) {
                n = (b ? 1 : 0);
                if (asn1Sequence.getObjectAt(1).toASN1Primitive() instanceof ASN1TaggedObject) {
                    this.cRLName = GeneralName.getInstance(asn1Sequence.getObjectAt(1));
                    n = 2;
                }
            }
            int n2;
            if (asn1Sequence.size() > (n2 = n)) {
                n2 = n;
                if (asn1Sequence.getObjectAt(n).toASN1Primitive() instanceof ASN1GeneralizedTime) {
                    this.time = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(n));
                    n2 = n + 1;
                }
            }
            if (asn1Sequence.size() > n2 && asn1Sequence.getObjectAt(n2).toASN1Primitive() instanceof DERBitString) {
                this.reasons = new ReasonFlags(DERBitString.getInstance(asn1Sequence.getObjectAt(n2)));
            }
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public GetCRL(final X500Name issuerName, final GeneralName crlName, final ASN1GeneralizedTime time, final ReasonFlags reasons) {
        this.issuerName = issuerName;
        this.cRLName = crlName;
        this.time = time;
        this.reasons = reasons;
    }
    
    public static GetCRL getInstance(final Object o) {
        if (o instanceof GetCRL) {
            return (GetCRL)o;
        }
        if (o != null) {
            return new GetCRL(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public X500Name getIssuerName() {
        return this.issuerName;
    }
    
    public ReasonFlags getReasons() {
        return this.reasons;
    }
    
    public ASN1GeneralizedTime getTime() {
        return this.time;
    }
    
    public GeneralName getcRLName() {
        return this.cRLName;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.issuerName);
        final GeneralName crlName = this.cRLName;
        if (crlName != null) {
            asn1EncodableVector.add(crlName);
        }
        final ASN1GeneralizedTime time = this.time;
        if (time != null) {
            asn1EncodableVector.add(time);
        }
        final ReasonFlags reasons = this.reasons;
        if (reasons != null) {
            asn1EncodableVector.add(reasons);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
