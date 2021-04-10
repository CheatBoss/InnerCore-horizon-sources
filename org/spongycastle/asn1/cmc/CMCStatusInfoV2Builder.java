package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class CMCStatusInfoV2Builder
{
    private final ASN1Sequence bodyList;
    private final CMCStatus cMCStatus;
    private OtherStatusInfo otherInfo;
    private DERUTF8String statusString;
    
    public CMCStatusInfoV2Builder(final CMCStatus cmcStatus, final BodyPartID bodyPartID) {
        this.cMCStatus = cmcStatus;
        this.bodyList = new DERSequence(bodyPartID);
    }
    
    public CMCStatusInfoV2Builder(final CMCStatus cmcStatus, final BodyPartID[] array) {
        this.cMCStatus = cmcStatus;
        this.bodyList = new DERSequence(array);
    }
    
    public CMCStatusInfoV2 build() {
        return new CMCStatusInfoV2(this.cMCStatus, this.bodyList, this.statusString, this.otherInfo);
    }
    
    public CMCStatusInfoV2Builder setOtherInfo(final CMCFailInfo cmcFailInfo) {
        this.otherInfo = new OtherStatusInfo(cmcFailInfo);
        return this;
    }
    
    public CMCStatusInfoV2Builder setOtherInfo(final ExtendedFailInfo extendedFailInfo) {
        this.otherInfo = new OtherStatusInfo(extendedFailInfo);
        return this;
    }
    
    public CMCStatusInfoV2Builder setOtherInfo(final PendInfo pendInfo) {
        this.otherInfo = new OtherStatusInfo(pendInfo);
        return this;
    }
    
    public CMCStatusInfoV2Builder setStatusString(final String s) {
        this.statusString = new DERUTF8String(s);
        return this;
    }
}
