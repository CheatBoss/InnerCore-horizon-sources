package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class CertResponse extends ASN1Object
{
    private ASN1Integer certReqId;
    private CertifiedKeyPair certifiedKeyPair;
    private ASN1OctetString rspInfo;
    private PKIStatusInfo status;
    
    public CertResponse(final ASN1Integer asn1Integer, final PKIStatusInfo pkiStatusInfo) {
        this(asn1Integer, pkiStatusInfo, null, null);
    }
    
    public CertResponse(final ASN1Integer certReqId, final PKIStatusInfo status, final CertifiedKeyPair certifiedKeyPair, final ASN1OctetString rspInfo) {
        if (certReqId == null) {
            throw new IllegalArgumentException("'certReqId' cannot be null");
        }
        if (status != null) {
            this.certReqId = certReqId;
            this.status = status;
            this.certifiedKeyPair = certifiedKeyPair;
            this.rspInfo = rspInfo;
            return;
        }
        throw new IllegalArgumentException("'status' cannot be null");
    }
    
    private CertResponse(final ASN1Sequence asn1Sequence) {
        this.certReqId = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        this.status = PKIStatusInfo.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() >= 3) {
            if (asn1Sequence.size() == 3) {
                final ASN1Encodable object = asn1Sequence.getObjectAt(2);
                if (object instanceof ASN1OctetString) {
                    this.rspInfo = ASN1OctetString.getInstance(object);
                    return;
                }
                this.certifiedKeyPair = CertifiedKeyPair.getInstance(object);
            }
            else {
                this.certifiedKeyPair = CertifiedKeyPair.getInstance(asn1Sequence.getObjectAt(2));
                this.rspInfo = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(3));
            }
        }
    }
    
    public static CertResponse getInstance(final Object o) {
        if (o instanceof CertResponse) {
            return (CertResponse)o;
        }
        if (o != null) {
            return new CertResponse(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer getCertReqId() {
        return this.certReqId;
    }
    
    public CertifiedKeyPair getCertifiedKeyPair() {
        return this.certifiedKeyPair;
    }
    
    public PKIStatusInfo getStatus() {
        return this.status;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certReqId);
        asn1EncodableVector.add(this.status);
        final CertifiedKeyPair certifiedKeyPair = this.certifiedKeyPair;
        if (certifiedKeyPair != null) {
            asn1EncodableVector.add(certifiedKeyPair);
        }
        final ASN1OctetString rspInfo = this.rspInfo;
        if (rspInfo != null) {
            asn1EncodableVector.add(rspInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
