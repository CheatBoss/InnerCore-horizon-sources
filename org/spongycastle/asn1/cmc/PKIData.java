package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class PKIData extends ASN1Object
{
    private final TaggedContentInfo[] cmsSequence;
    private final TaggedAttribute[] controlSequence;
    private final OtherMsg[] otherMsgSequence;
    private final TaggedRequest[] reqSequence;
    
    private PKIData(ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 4) {
            final int n = 0;
            final ASN1Sequence asn1Sequence2 = (ASN1Sequence)asn1Sequence.getObjectAt(0);
            this.controlSequence = new TaggedAttribute[asn1Sequence2.size()];
            int n2 = 0;
            while (true) {
                final TaggedAttribute[] controlSequence = this.controlSequence;
                if (n2 >= controlSequence.length) {
                    break;
                }
                controlSequence[n2] = TaggedAttribute.getInstance(asn1Sequence2.getObjectAt(n2));
                ++n2;
            }
            final ASN1Sequence asn1Sequence3 = (ASN1Sequence)asn1Sequence.getObjectAt(1);
            this.reqSequence = new TaggedRequest[asn1Sequence3.size()];
            int n3 = 0;
            while (true) {
                final TaggedRequest[] reqSequence = this.reqSequence;
                if (n3 >= reqSequence.length) {
                    break;
                }
                reqSequence[n3] = TaggedRequest.getInstance(asn1Sequence3.getObjectAt(n3));
                ++n3;
            }
            final ASN1Sequence asn1Sequence4 = (ASN1Sequence)asn1Sequence.getObjectAt(2);
            this.cmsSequence = new TaggedContentInfo[asn1Sequence4.size()];
            int n4 = 0;
            while (true) {
                final TaggedContentInfo[] cmsSequence = this.cmsSequence;
                if (n4 >= cmsSequence.length) {
                    break;
                }
                cmsSequence[n4] = TaggedContentInfo.getInstance(asn1Sequence4.getObjectAt(n4));
                ++n4;
            }
            asn1Sequence = (ASN1Sequence)asn1Sequence.getObjectAt(3);
            this.otherMsgSequence = new OtherMsg[asn1Sequence.size()];
            int n5 = n;
            while (true) {
                final OtherMsg[] otherMsgSequence = this.otherMsgSequence;
                if (n5 >= otherMsgSequence.length) {
                    break;
                }
                otherMsgSequence[n5] = OtherMsg.getInstance(asn1Sequence.getObjectAt(n5));
                ++n5;
            }
            return;
        }
        throw new IllegalArgumentException("Sequence not 4 elements.");
    }
    
    public PKIData(final TaggedAttribute[] controlSequence, final TaggedRequest[] reqSequence, final TaggedContentInfo[] cmsSequence, final OtherMsg[] otherMsgSequence) {
        this.controlSequence = controlSequence;
        this.reqSequence = reqSequence;
        this.cmsSequence = cmsSequence;
        this.otherMsgSequence = otherMsgSequence;
    }
    
    public static PKIData getInstance(final Object o) {
        if (o instanceof PKIData) {
            return (PKIData)o;
        }
        if (o != null) {
            return new PKIData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public TaggedContentInfo[] getCmsSequence() {
        return this.cmsSequence;
    }
    
    public TaggedAttribute[] getControlSequence() {
        return this.controlSequence;
    }
    
    public OtherMsg[] getOtherMsgSequence() {
        return this.otherMsgSequence;
    }
    
    public TaggedRequest[] getReqSequence() {
        return this.reqSequence;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(new ASN1Encodable[] { new DERSequence(this.controlSequence), new DERSequence(this.reqSequence), new DERSequence(this.cmsSequence), new DERSequence(this.otherMsgSequence) });
    }
}
