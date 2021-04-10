package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class CMCStatusInfoV2 extends ASN1Object
{
    private final ASN1Sequence bodyList;
    private final CMCStatus cMCStatus;
    private final OtherStatusInfo otherStatusInfo;
    private final DERUTF8String statusString;
    
    private CMCStatusInfoV2(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 2 || asn1Sequence.size() > 4) {
            throw new IllegalArgumentException("incorrect sequence size");
        }
        this.cMCStatus = CMCStatus.getInstance(asn1Sequence.getObjectAt(0));
        this.bodyList = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() <= 2) {
            this.statusString = null;
            this.otherStatusInfo = null;
            return;
        }
        if (asn1Sequence.size() == 4) {
            this.statusString = DERUTF8String.getInstance(asn1Sequence.getObjectAt(2));
            this.otherStatusInfo = OtherStatusInfo.getInstance(asn1Sequence.getObjectAt(3));
            return;
        }
        if (asn1Sequence.getObjectAt(2) instanceof DERUTF8String) {
            this.statusString = DERUTF8String.getInstance(asn1Sequence.getObjectAt(2));
            this.otherStatusInfo = null;
            return;
        }
        this.statusString = null;
        this.otherStatusInfo = OtherStatusInfo.getInstance(asn1Sequence.getObjectAt(2));
    }
    
    CMCStatusInfoV2(final CMCStatus cmcStatus, final ASN1Sequence bodyList, final DERUTF8String statusString, final OtherStatusInfo otherStatusInfo) {
        this.cMCStatus = cmcStatus;
        this.bodyList = bodyList;
        this.statusString = statusString;
        this.otherStatusInfo = otherStatusInfo;
    }
    
    public static CMCStatusInfoV2 getInstance(final Object o) {
        if (o instanceof CMCStatusInfoV2) {
            return (CMCStatusInfoV2)o;
        }
        if (o != null) {
            return new CMCStatusInfoV2(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BodyPartID[] getBodyList() {
        return Utils.toBodyPartIDArray(this.bodyList);
    }
    
    public OtherStatusInfo getOtherStatusInfo() {
        return this.otherStatusInfo;
    }
    
    public DERUTF8String getStatusString() {
        return this.statusString;
    }
    
    public CMCStatus getcMCStatus() {
        return this.cMCStatus;
    }
    
    public boolean hasOtherInfo() {
        return this.otherStatusInfo != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.cMCStatus);
        asn1EncodableVector.add(this.bodyList);
        final DERUTF8String statusString = this.statusString;
        if (statusString != null) {
            asn1EncodableVector.add(statusString);
        }
        final OtherStatusInfo otherStatusInfo = this.otherStatusInfo;
        if (otherStatusInfo != null) {
            asn1EncodableVector.add(otherStatusInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
