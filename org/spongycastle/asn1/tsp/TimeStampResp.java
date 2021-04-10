package org.spongycastle.asn1.tsp;

import org.spongycastle.asn1.cmp.*;
import org.spongycastle.asn1.cms.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class TimeStampResp extends ASN1Object
{
    PKIStatusInfo pkiStatusInfo;
    ContentInfo timeStampToken;
    
    private TimeStampResp(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.pkiStatusInfo = PKIStatusInfo.getInstance(objects.nextElement());
        if (objects.hasMoreElements()) {
            this.timeStampToken = ContentInfo.getInstance(objects.nextElement());
        }
    }
    
    public TimeStampResp(final PKIStatusInfo pkiStatusInfo, final ContentInfo timeStampToken) {
        this.pkiStatusInfo = pkiStatusInfo;
        this.timeStampToken = timeStampToken;
    }
    
    public static TimeStampResp getInstance(final Object o) {
        if (o instanceof TimeStampResp) {
            return (TimeStampResp)o;
        }
        if (o != null) {
            return new TimeStampResp(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public PKIStatusInfo getStatus() {
        return this.pkiStatusInfo;
    }
    
    public ContentInfo getTimeStampToken() {
        return this.timeStampToken;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pkiStatusInfo);
        final ContentInfo timeStampToken = this.timeStampToken;
        if (timeStampToken != null) {
            asn1EncodableVector.add(timeStampToken);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
