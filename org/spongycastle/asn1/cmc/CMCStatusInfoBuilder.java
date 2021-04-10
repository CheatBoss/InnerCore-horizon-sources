package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class CMCStatusInfoBuilder
{
    private final ASN1Sequence bodyList;
    private final CMCStatus cMCStatus;
    private CMCStatusInfo.OtherInfo otherInfo;
    private DERUTF8String statusString;
    
    public CMCStatusInfoBuilder(final CMCStatus cmcStatus, final BodyPartID bodyPartID) {
        this.cMCStatus = cmcStatus;
        this.bodyList = new DERSequence(bodyPartID);
    }
    
    public CMCStatusInfoBuilder(final CMCStatus cmcStatus, final BodyPartID[] array) {
        this.cMCStatus = cmcStatus;
        this.bodyList = new DERSequence(array);
    }
    
    public CMCStatusInfo build() {
        return new CMCStatusInfo(this.cMCStatus, this.bodyList, this.statusString, this.otherInfo);
    }
    
    public CMCStatusInfoBuilder setOtherInfo(final CMCFailInfo cmcFailInfo) {
        this.otherInfo = new CMCStatusInfo.OtherInfo(cmcFailInfo);
        return this;
    }
    
    public CMCStatusInfoBuilder setOtherInfo(final PendInfo pendInfo) {
        this.otherInfo = new CMCStatusInfo.OtherInfo(pendInfo);
        return this;
    }
    
    public CMCStatusInfoBuilder setStatusString(final String s) {
        this.statusString = new DERUTF8String(s);
        return this;
    }
}
