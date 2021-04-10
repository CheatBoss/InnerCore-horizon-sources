package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class OtherMsg extends ASN1Object
{
    private final BodyPartID bodyPartID;
    private final ASN1ObjectIdentifier otherMsgType;
    private final ASN1Encodable otherMsgValue;
    
    private OtherMsg(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.bodyPartID = BodyPartID.getInstance(asn1Sequence.getObjectAt(0));
            this.otherMsgType = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.otherMsgValue = asn1Sequence.getObjectAt(2);
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public OtherMsg(final BodyPartID bodyPartID, final ASN1ObjectIdentifier otherMsgType, final ASN1Encodable otherMsgValue) {
        this.bodyPartID = bodyPartID;
        this.otherMsgType = otherMsgType;
        this.otherMsgValue = otherMsgValue;
    }
    
    public static OtherMsg getInstance(final Object o) {
        if (o instanceof OtherMsg) {
            return (OtherMsg)o;
        }
        if (o != null) {
            return new OtherMsg(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static OtherMsg getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public BodyPartID getBodyPartID() {
        return this.bodyPartID;
    }
    
    public ASN1ObjectIdentifier getOtherMsgType() {
        return this.otherMsgType;
    }
    
    public ASN1Encodable getOtherMsgValue() {
        return this.otherMsgValue;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.bodyPartID);
        asn1EncodableVector.add(this.otherMsgType);
        asn1EncodableVector.add(this.otherMsgValue);
        return new DERSequence(asn1EncodableVector);
    }
}
