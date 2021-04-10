package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class V2Form extends ASN1Object
{
    IssuerSerial baseCertificateID;
    GeneralNames issuerName;
    ObjectDigestInfo objectDigestInfo;
    
    public V2Form(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() <= 3) {
            int i;
            if (!(asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject)) {
                this.issuerName = GeneralNames.getInstance(asn1Sequence.getObjectAt(0));
                i = 1;
            }
            else {
                i = 0;
            }
            while (i != asn1Sequence.size()) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(i));
                if (instance.getTagNo() == 0) {
                    this.baseCertificateID = IssuerSerial.getInstance(instance, false);
                }
                else {
                    if (instance.getTagNo() != 1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Bad tag number: ");
                        sb.append(instance.getTagNo());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    this.objectDigestInfo = ObjectDigestInfo.getInstance(instance, false);
                }
                ++i;
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Bad sequence size: ");
        sb2.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public V2Form(final GeneralNames generalNames) {
        this(generalNames, null, null);
    }
    
    public V2Form(final GeneralNames generalNames, final IssuerSerial issuerSerial) {
        this(generalNames, issuerSerial, null);
    }
    
    public V2Form(final GeneralNames issuerName, final IssuerSerial baseCertificateID, final ObjectDigestInfo objectDigestInfo) {
        this.issuerName = issuerName;
        this.baseCertificateID = baseCertificateID;
        this.objectDigestInfo = objectDigestInfo;
    }
    
    public V2Form(final GeneralNames generalNames, final ObjectDigestInfo objectDigestInfo) {
        this(generalNames, null, objectDigestInfo);
    }
    
    public static V2Form getInstance(final Object o) {
        if (o instanceof V2Form) {
            return (V2Form)o;
        }
        if (o != null) {
            return new V2Form(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static V2Form getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public IssuerSerial getBaseCertificateID() {
        return this.baseCertificateID;
    }
    
    public GeneralNames getIssuerName() {
        return this.issuerName;
    }
    
    public ObjectDigestInfo getObjectDigestInfo() {
        return this.objectDigestInfo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final GeneralNames issuerName = this.issuerName;
        if (issuerName != null) {
            asn1EncodableVector.add(issuerName);
        }
        if (this.baseCertificateID != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.baseCertificateID));
        }
        if (this.objectDigestInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.objectDigestInfo));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
