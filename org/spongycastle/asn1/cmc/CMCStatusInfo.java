package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class CMCStatusInfo extends ASN1Object
{
    private final ASN1Sequence bodyList;
    private final CMCStatus cMCStatus;
    private final OtherInfo otherInfo;
    private final DERUTF8String statusString;
    
    private CMCStatusInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 2 || asn1Sequence.size() > 4) {
            throw new IllegalArgumentException("incorrect sequence size");
        }
        this.cMCStatus = CMCStatus.getInstance(asn1Sequence.getObjectAt(0));
        this.bodyList = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() > 3) {
            this.statusString = DERUTF8String.getInstance(asn1Sequence.getObjectAt(2));
            this.otherInfo = getInstance(asn1Sequence.getObjectAt(3));
            return;
        }
        if (asn1Sequence.size() <= 2) {
            this.statusString = null;
            this.otherInfo = null;
            return;
        }
        if (asn1Sequence.getObjectAt(2) instanceof DERUTF8String) {
            this.statusString = DERUTF8String.getInstance(asn1Sequence.getObjectAt(2));
            this.otherInfo = null;
            return;
        }
        this.statusString = null;
        this.otherInfo = getInstance(asn1Sequence.getObjectAt(2));
    }
    
    CMCStatusInfo(final CMCStatus cmcStatus, final ASN1Sequence bodyList, final DERUTF8String statusString, final OtherInfo otherInfo) {
        this.cMCStatus = cmcStatus;
        this.bodyList = bodyList;
        this.statusString = statusString;
        this.otherInfo = otherInfo;
    }
    
    public static CMCStatusInfo getInstance(final Object o) {
        if (o instanceof CMCStatusInfo) {
            return (CMCStatusInfo)o;
        }
        if (o != null) {
            return new CMCStatusInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BodyPartID[] getBodyList() {
        return Utils.toBodyPartIDArray(this.bodyList);
    }
    
    public CMCStatus getCMCStatus() {
        return this.cMCStatus;
    }
    
    public OtherInfo getOtherInfo() {
        return this.otherInfo;
    }
    
    public DERUTF8String getStatusString() {
        return this.statusString;
    }
    
    public boolean hasOtherInfo() {
        return this.otherInfo != null;
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
        final OtherInfo otherInfo = this.otherInfo;
        if (otherInfo != null) {
            asn1EncodableVector.add(otherInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    public static class OtherInfo extends ASN1Object implements ASN1Choice
    {
        private final CMCFailInfo failInfo;
        private final PendInfo pendInfo;
        
        OtherInfo(final CMCFailInfo cmcFailInfo) {
            this(cmcFailInfo, null);
        }
        
        private OtherInfo(final CMCFailInfo failInfo, final PendInfo pendInfo) {
            this.failInfo = failInfo;
            this.pendInfo = pendInfo;
        }
        
        OtherInfo(final PendInfo pendInfo) {
            this(null, pendInfo);
        }
        
        private static OtherInfo getInstance(final Object o) {
            if (o instanceof OtherInfo) {
                return (OtherInfo)o;
            }
            if (o instanceof ASN1Encodable) {
                final ASN1Primitive asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
                if (asn1Primitive instanceof ASN1Integer) {
                    return new OtherInfo(CMCFailInfo.getInstance(asn1Primitive));
                }
                if (asn1Primitive instanceof ASN1Sequence) {
                    return new OtherInfo(PendInfo.getInstance(asn1Primitive));
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown object in getInstance(): ");
            sb.append(o.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        
        public boolean isFailInfo() {
            return this.failInfo != null;
        }
        
        @Override
        public ASN1Primitive toASN1Primitive() {
            final PendInfo pendInfo = this.pendInfo;
            if (pendInfo != null) {
                return pendInfo.toASN1Primitive();
            }
            return this.failInfo.toASN1Primitive();
        }
    }
}
