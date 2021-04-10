package org.spongycastle.asn1.cmp;

import java.util.*;
import org.spongycastle.asn1.*;

public class ErrorMsgContent extends ASN1Object
{
    private ASN1Integer errorCode;
    private PKIFreeText errorDetails;
    private PKIStatusInfo pkiStatusInfo;
    
    private ErrorMsgContent(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.pkiStatusInfo = PKIStatusInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final Object nextElement = objects.nextElement();
            if (nextElement instanceof ASN1Integer) {
                this.errorCode = ASN1Integer.getInstance(nextElement);
            }
            else {
                this.errorDetails = PKIFreeText.getInstance(nextElement);
            }
        }
    }
    
    public ErrorMsgContent(final PKIStatusInfo pkiStatusInfo) {
        this(pkiStatusInfo, null, null);
    }
    
    public ErrorMsgContent(final PKIStatusInfo pkiStatusInfo, final ASN1Integer errorCode, final PKIFreeText errorDetails) {
        if (pkiStatusInfo != null) {
            this.pkiStatusInfo = pkiStatusInfo;
            this.errorCode = errorCode;
            this.errorDetails = errorDetails;
            return;
        }
        throw new IllegalArgumentException("'pkiStatusInfo' cannot be null");
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(asn1Encodable);
        }
    }
    
    public static ErrorMsgContent getInstance(final Object o) {
        if (o instanceof ErrorMsgContent) {
            return (ErrorMsgContent)o;
        }
        if (o != null) {
            return new ErrorMsgContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer getErrorCode() {
        return this.errorCode;
    }
    
    public PKIFreeText getErrorDetails() {
        return this.errorDetails;
    }
    
    public PKIStatusInfo getPKIStatusInfo() {
        return this.pkiStatusInfo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pkiStatusInfo);
        this.addOptional(asn1EncodableVector, this.errorCode);
        this.addOptional(asn1EncodableVector, this.errorDetails);
        return new DERSequence(asn1EncodableVector);
    }
}
