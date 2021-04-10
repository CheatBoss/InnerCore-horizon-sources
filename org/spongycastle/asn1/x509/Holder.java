package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class Holder extends ASN1Object
{
    public static final int V1_CERTIFICATE_HOLDER = 0;
    public static final int V2_CERTIFICATE_HOLDER = 1;
    IssuerSerial baseCertificateID;
    GeneralNames entityName;
    ObjectDigestInfo objectDigestInfo;
    private int version;
    
    private Holder(final ASN1Sequence asn1Sequence) {
        this.version = 1;
        if (asn1Sequence.size() <= 3) {
            for (int i = 0; i != asn1Sequence.size(); ++i) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(i));
                final int tagNo = instance.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        if (tagNo != 2) {
                            throw new IllegalArgumentException("unknown tag in Holder");
                        }
                        this.objectDigestInfo = ObjectDigestInfo.getInstance(instance, false);
                    }
                    else {
                        this.entityName = GeneralNames.getInstance(instance, false);
                    }
                }
                else {
                    this.baseCertificateID = IssuerSerial.getInstance(instance, false);
                }
            }
            this.version = 1;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    private Holder(final ASN1TaggedObject asn1TaggedObject) {
        this.version = 1;
        final int tagNo = asn1TaggedObject.getTagNo();
        if (tagNo != 0) {
            if (tagNo != 1) {
                throw new IllegalArgumentException("unknown tag in Holder");
            }
            this.entityName = GeneralNames.getInstance(asn1TaggedObject, true);
        }
        else {
            this.baseCertificateID = IssuerSerial.getInstance(asn1TaggedObject, true);
        }
        this.version = 0;
    }
    
    public Holder(final GeneralNames generalNames) {
        this(generalNames, 1);
    }
    
    public Holder(final GeneralNames entityName, final int version) {
        this.version = 1;
        this.entityName = entityName;
        this.version = version;
    }
    
    public Holder(final IssuerSerial issuerSerial) {
        this(issuerSerial, 1);
    }
    
    public Holder(final IssuerSerial baseCertificateID, final int version) {
        this.version = 1;
        this.baseCertificateID = baseCertificateID;
        this.version = version;
    }
    
    public Holder(final ObjectDigestInfo objectDigestInfo) {
        this.version = 1;
        this.objectDigestInfo = objectDigestInfo;
    }
    
    public static Holder getInstance(final Object o) {
        if (o instanceof Holder) {
            return (Holder)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new Holder(ASN1TaggedObject.getInstance(o));
        }
        if (o != null) {
            return new Holder(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public IssuerSerial getBaseCertificateID() {
        return this.baseCertificateID;
    }
    
    public GeneralNames getEntityName() {
        return this.entityName;
    }
    
    public ObjectDigestInfo getObjectDigestInfo() {
        return this.objectDigestInfo;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.version == 1) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            if (this.baseCertificateID != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 0, this.baseCertificateID));
            }
            if (this.entityName != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 1, this.entityName));
            }
            if (this.objectDigestInfo != null) {
                asn1EncodableVector.add(new DERTaggedObject(false, 2, this.objectDigestInfo));
            }
            return new DERSequence(asn1EncodableVector);
        }
        if (this.entityName != null) {
            return new DERTaggedObject(true, 1, this.entityName);
        }
        return new DERTaggedObject(true, 0, this.baseCertificateID);
    }
}
