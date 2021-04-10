package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class AttributeCertificate extends ASN1Object
{
    AttributeCertificateInfo acinfo;
    AlgorithmIdentifier signatureAlgorithm;
    DERBitString signatureValue;
    
    public AttributeCertificate(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.acinfo = AttributeCertificateInfo.getInstance(asn1Sequence.getObjectAt(0));
            this.signatureAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.signatureValue = DERBitString.getInstance(asn1Sequence.getObjectAt(2));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public AttributeCertificate(final AttributeCertificateInfo acinfo, final AlgorithmIdentifier signatureAlgorithm, final DERBitString signatureValue) {
        this.acinfo = acinfo;
        this.signatureAlgorithm = signatureAlgorithm;
        this.signatureValue = signatureValue;
    }
    
    public static AttributeCertificate getInstance(final Object o) {
        if (o instanceof AttributeCertificate) {
            return (AttributeCertificate)o;
        }
        if (o != null) {
            return new AttributeCertificate(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AttributeCertificateInfo getAcinfo() {
        return this.acinfo;
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }
    
    public DERBitString getSignatureValue() {
        return this.signatureValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.acinfo);
        asn1EncodableVector.add(this.signatureAlgorithm);
        asn1EncodableVector.add(this.signatureValue);
        return new DERSequence(asn1EncodableVector);
    }
}
