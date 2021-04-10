package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.*;

public class OCSPResponse extends ASN1Object
{
    ResponseBytes responseBytes;
    OCSPResponseStatus responseStatus;
    
    private OCSPResponse(final ASN1Sequence asn1Sequence) {
        this.responseStatus = OCSPResponseStatus.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() == 2) {
            this.responseBytes = ResponseBytes.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true);
        }
    }
    
    public OCSPResponse(final OCSPResponseStatus responseStatus, final ResponseBytes responseBytes) {
        this.responseStatus = responseStatus;
        this.responseBytes = responseBytes;
    }
    
    public static OCSPResponse getInstance(final Object o) {
        if (o instanceof OCSPResponse) {
            return (OCSPResponse)o;
        }
        if (o != null) {
            return new OCSPResponse(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static OCSPResponse getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ResponseBytes getResponseBytes() {
        return this.responseBytes;
    }
    
    public OCSPResponseStatus getResponseStatus() {
        return this.responseStatus;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.responseStatus);
        if (this.responseBytes != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.responseBytes));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
