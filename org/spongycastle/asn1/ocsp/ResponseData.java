package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class ResponseData extends ASN1Object
{
    private static final ASN1Integer V1;
    private ASN1GeneralizedTime producedAt;
    private ResponderID responderID;
    private Extensions responseExtensions;
    private ASN1Sequence responses;
    private ASN1Integer version;
    private boolean versionPresent;
    
    static {
        V1 = new ASN1Integer(0L);
    }
    
    public ResponseData(final ASN1Integer version, final ResponderID responderID, final ASN1GeneralizedTime producedAt, final ASN1Sequence responses, final Extensions responseExtensions) {
        this.version = version;
        this.responderID = responderID;
        this.producedAt = producedAt;
        this.responses = responses;
        this.responseExtensions = responseExtensions;
    }
    
    private ResponseData(final ASN1Sequence asn1Sequence) {
        int n = 0;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject && ((ASN1TaggedObject)asn1Sequence.getObjectAt(0)).getTagNo() == 0) {
            this.versionPresent = true;
            this.version = ASN1Integer.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(0), true);
            n = 1;
        }
        else {
            this.version = ResponseData.V1;
        }
        final int n2 = n + 1;
        this.responderID = ResponderID.getInstance(asn1Sequence.getObjectAt(n));
        final int n3 = n2 + 1;
        this.producedAt = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(n2));
        final int n4 = n3 + 1;
        this.responses = (ASN1Sequence)asn1Sequence.getObjectAt(n3);
        if (asn1Sequence.size() > n4) {
            this.responseExtensions = Extensions.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n4), true);
        }
    }
    
    public ResponseData(final ResponderID responderID, final ASN1GeneralizedTime asn1GeneralizedTime, final ASN1Sequence asn1Sequence, final Extensions extensions) {
        this(ResponseData.V1, responderID, asn1GeneralizedTime, asn1Sequence, extensions);
    }
    
    public ResponseData(final ResponderID responderID, final ASN1GeneralizedTime asn1GeneralizedTime, final ASN1Sequence asn1Sequence, final X509Extensions x509Extensions) {
        this(ResponseData.V1, responderID, ASN1GeneralizedTime.getInstance(asn1GeneralizedTime), asn1Sequence, Extensions.getInstance(x509Extensions));
    }
    
    public static ResponseData getInstance(final Object o) {
        if (o instanceof ResponseData) {
            return (ResponseData)o;
        }
        if (o != null) {
            return new ResponseData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static ResponseData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1GeneralizedTime getProducedAt() {
        return this.producedAt;
    }
    
    public ResponderID getResponderID() {
        return this.responderID;
    }
    
    public Extensions getResponseExtensions() {
        return this.responseExtensions;
    }
    
    public ASN1Sequence getResponses() {
        return this.responses;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.versionPresent || !this.version.equals(ResponseData.V1)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.version));
        }
        asn1EncodableVector.add(this.responderID);
        asn1EncodableVector.add(this.producedAt);
        asn1EncodableVector.add(this.responses);
        if (this.responseExtensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.responseExtensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
