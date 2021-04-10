package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class AttributeCertificateInfo extends ASN1Object
{
    private AttCertValidityPeriod attrCertValidityPeriod;
    private ASN1Sequence attributes;
    private Extensions extensions;
    private Holder holder;
    private AttCertIssuer issuer;
    private DERBitString issuerUniqueID;
    private ASN1Integer serialNumber;
    private AlgorithmIdentifier signature;
    private ASN1Integer version;
    
    private AttributeCertificateInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 6 && asn1Sequence.size() <= 9) {
            int n = 0;
            if (asn1Sequence.getObjectAt(0) instanceof ASN1Integer) {
                this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
                n = 1;
            }
            else {
                this.version = new ASN1Integer(0L);
            }
            this.holder = Holder.getInstance(asn1Sequence.getObjectAt(n));
            this.issuer = AttCertIssuer.getInstance(asn1Sequence.getObjectAt(n + 1));
            this.signature = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n + 2));
            this.serialNumber = ASN1Integer.getInstance(asn1Sequence.getObjectAt(n + 3));
            this.attrCertValidityPeriod = AttCertValidityPeriod.getInstance(asn1Sequence.getObjectAt(n + 4));
            this.attributes = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(n + 5));
            for (int i = n + 6; i < asn1Sequence.size(); ++i) {
                final ASN1Encodable object = asn1Sequence.getObjectAt(i);
                if (object instanceof DERBitString) {
                    this.issuerUniqueID = DERBitString.getInstance(asn1Sequence.getObjectAt(i));
                }
                else if (object instanceof ASN1Sequence || object instanceof Extensions) {
                    this.extensions = Extensions.getInstance(asn1Sequence.getObjectAt(i));
                }
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static AttributeCertificateInfo getInstance(final Object o) {
        if (o instanceof AttributeCertificateInfo) {
            return (AttributeCertificateInfo)o;
        }
        if (o != null) {
            return new AttributeCertificateInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static AttributeCertificateInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public AttCertValidityPeriod getAttrCertValidityPeriod() {
        return this.attrCertValidityPeriod;
    }
    
    public ASN1Sequence getAttributes() {
        return this.attributes;
    }
    
    public Extensions getExtensions() {
        return this.extensions;
    }
    
    public Holder getHolder() {
        return this.holder;
    }
    
    public AttCertIssuer getIssuer() {
        return this.issuer;
    }
    
    public DERBitString getIssuerUniqueID() {
        return this.issuerUniqueID;
    }
    
    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }
    
    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.version.getValue().intValue() != 0) {
            asn1EncodableVector.add(this.version);
        }
        asn1EncodableVector.add(this.holder);
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.signature);
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(this.attrCertValidityPeriod);
        asn1EncodableVector.add(this.attributes);
        final DERBitString issuerUniqueID = this.issuerUniqueID;
        if (issuerUniqueID != null) {
            asn1EncodableVector.add(issuerUniqueID);
        }
        final Extensions extensions = this.extensions;
        if (extensions != null) {
            asn1EncodableVector.add(extensions);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
