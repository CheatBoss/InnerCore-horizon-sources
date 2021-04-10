package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class SCVPReqRes extends ASN1Object
{
    private final ContentInfo request;
    private final ContentInfo response;
    
    private SCVPReqRes(final ASN1Sequence asn1Sequence) {
        ASN1Encodable asn1Encodable;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            this.request = ContentInfo.getInstance(ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(0)), true);
            asn1Encodable = asn1Sequence.getObjectAt(1);
        }
        else {
            this.request = null;
            asn1Encodable = asn1Sequence.getObjectAt(0);
        }
        this.response = ContentInfo.getInstance(asn1Encodable);
    }
    
    public SCVPReqRes(final ContentInfo response) {
        this.request = null;
        this.response = response;
    }
    
    public SCVPReqRes(final ContentInfo request, final ContentInfo response) {
        this.request = request;
        this.response = response;
    }
    
    public static SCVPReqRes getInstance(final Object o) {
        if (o instanceof SCVPReqRes) {
            return (SCVPReqRes)o;
        }
        if (o != null) {
            return new SCVPReqRes(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ContentInfo getRequest() {
        return this.request;
    }
    
    public ContentInfo getResponse() {
        return this.response;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.request != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.request));
        }
        asn1EncodableVector.add(this.response);
        return new DERSequence(asn1EncodableVector);
    }
}
