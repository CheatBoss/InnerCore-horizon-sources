package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.crmf.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class RevAnnContent extends ASN1Object
{
    private ASN1GeneralizedTime badSinceDate;
    private CertId certId;
    private Extensions crlDetails;
    private PKIStatus status;
    private ASN1GeneralizedTime willBeRevokedAt;
    
    private RevAnnContent(final ASN1Sequence asn1Sequence) {
        this.status = PKIStatus.getInstance(asn1Sequence.getObjectAt(0));
        this.certId = CertId.getInstance(asn1Sequence.getObjectAt(1));
        this.willBeRevokedAt = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(2));
        this.badSinceDate = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(3));
        if (asn1Sequence.size() > 4) {
            this.crlDetails = Extensions.getInstance(asn1Sequence.getObjectAt(4));
        }
    }
    
    public static RevAnnContent getInstance(final Object o) {
        if (o instanceof RevAnnContent) {
            return (RevAnnContent)o;
        }
        if (o != null) {
            return new RevAnnContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1GeneralizedTime getBadSinceDate() {
        return this.badSinceDate;
    }
    
    public CertId getCertId() {
        return this.certId;
    }
    
    public Extensions getCrlDetails() {
        return this.crlDetails;
    }
    
    public PKIStatus getStatus() {
        return this.status;
    }
    
    public ASN1GeneralizedTime getWillBeRevokedAt() {
        return this.willBeRevokedAt;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.status);
        asn1EncodableVector.add(this.certId);
        asn1EncodableVector.add(this.willBeRevokedAt);
        asn1EncodableVector.add(this.badSinceDate);
        final Extensions crlDetails = this.crlDetails;
        if (crlDetails != null) {
            asn1EncodableVector.add(crlDetails);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
