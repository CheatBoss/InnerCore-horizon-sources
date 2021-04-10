package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class SingleResponse extends ASN1Object
{
    private CertID certID;
    private CertStatus certStatus;
    private ASN1GeneralizedTime nextUpdate;
    private Extensions singleExtensions;
    private ASN1GeneralizedTime thisUpdate;
    
    private SingleResponse(final ASN1Sequence asn1Sequence) {
        this.certID = CertID.getInstance(asn1Sequence.getObjectAt(0));
        this.certStatus = CertStatus.getInstance(asn1Sequence.getObjectAt(1));
        this.thisUpdate = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(2));
        if (asn1Sequence.size() > 4) {
            this.nextUpdate = ASN1GeneralizedTime.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(3), true);
            this.singleExtensions = Extensions.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(4), true);
            return;
        }
        if (asn1Sequence.size() > 3) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(3);
            if (asn1TaggedObject.getTagNo() == 0) {
                this.nextUpdate = ASN1GeneralizedTime.getInstance(asn1TaggedObject, true);
                return;
            }
            this.singleExtensions = Extensions.getInstance(asn1TaggedObject, true);
        }
    }
    
    public SingleResponse(final CertID certID, final CertStatus certStatus, final ASN1GeneralizedTime thisUpdate, final ASN1GeneralizedTime nextUpdate, final Extensions singleExtensions) {
        this.certID = certID;
        this.certStatus = certStatus;
        this.thisUpdate = thisUpdate;
        this.nextUpdate = nextUpdate;
        this.singleExtensions = singleExtensions;
    }
    
    public SingleResponse(final CertID certID, final CertStatus certStatus, final ASN1GeneralizedTime asn1GeneralizedTime, final ASN1GeneralizedTime asn1GeneralizedTime2, final X509Extensions x509Extensions) {
        this(certID, certStatus, asn1GeneralizedTime, asn1GeneralizedTime2, Extensions.getInstance(x509Extensions));
    }
    
    public static SingleResponse getInstance(final Object o) {
        if (o instanceof SingleResponse) {
            return (SingleResponse)o;
        }
        if (o != null) {
            return new SingleResponse(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static SingleResponse getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public CertID getCertID() {
        return this.certID;
    }
    
    public CertStatus getCertStatus() {
        return this.certStatus;
    }
    
    public ASN1GeneralizedTime getNextUpdate() {
        return this.nextUpdate;
    }
    
    public Extensions getSingleExtensions() {
        return this.singleExtensions;
    }
    
    public ASN1GeneralizedTime getThisUpdate() {
        return this.thisUpdate;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certID);
        asn1EncodableVector.add(this.certStatus);
        asn1EncodableVector.add(this.thisUpdate);
        if (this.nextUpdate != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.nextUpdate));
        }
        if (this.singleExtensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.singleExtensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
