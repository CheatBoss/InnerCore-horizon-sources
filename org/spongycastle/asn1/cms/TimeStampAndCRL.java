package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class TimeStampAndCRL extends ASN1Object
{
    private CertificateList crl;
    private ContentInfo timeStamp;
    
    private TimeStampAndCRL(final ASN1Sequence asn1Sequence) {
        this.timeStamp = ContentInfo.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() == 2) {
            this.crl = CertificateList.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public TimeStampAndCRL(final ContentInfo timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public static TimeStampAndCRL getInstance(final Object o) {
        if (o instanceof TimeStampAndCRL) {
            return (TimeStampAndCRL)o;
        }
        if (o != null) {
            return new TimeStampAndCRL(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CertificateList getCRL() {
        return this.crl;
    }
    
    public CertificateList getCertificateList() {
        return this.crl;
    }
    
    public ContentInfo getTimeStampToken() {
        return this.timeStamp;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.timeStamp);
        final CertificateList crl = this.crl;
        if (crl != null) {
            asn1EncodableVector.add(crl);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
