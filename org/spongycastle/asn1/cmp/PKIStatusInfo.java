package org.spongycastle.asn1.cmp;

import java.math.*;
import org.spongycastle.asn1.*;

public class PKIStatusInfo extends ASN1Object
{
    DERBitString failInfo;
    ASN1Integer status;
    PKIFreeText statusString;
    
    private PKIStatusInfo(final ASN1Sequence asn1Sequence) {
        this.status = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        this.statusString = null;
        this.failInfo = null;
        ASN1Encodable asn1Encodable = null;
        Label_0052: {
            if (asn1Sequence.size() <= 2) {
                if (asn1Sequence.size() > 1) {
                    asn1Encodable = asn1Sequence.getObjectAt(1);
                    if (asn1Encodable instanceof DERBitString) {
                        break Label_0052;
                    }
                    this.statusString = PKIFreeText.getInstance(asn1Encodable);
                }
                return;
            }
            this.statusString = PKIFreeText.getInstance(asn1Sequence.getObjectAt(1));
            asn1Encodable = asn1Sequence.getObjectAt(2);
        }
        this.failInfo = DERBitString.getInstance(asn1Encodable);
    }
    
    public PKIStatusInfo(final PKIStatus pkiStatus) {
        this.status = ASN1Integer.getInstance(pkiStatus.toASN1Primitive());
    }
    
    public PKIStatusInfo(final PKIStatus pkiStatus, final PKIFreeText statusString) {
        this.status = ASN1Integer.getInstance(pkiStatus.toASN1Primitive());
        this.statusString = statusString;
    }
    
    public PKIStatusInfo(final PKIStatus pkiStatus, final PKIFreeText statusString, final PKIFailureInfo failInfo) {
        this.status = ASN1Integer.getInstance(pkiStatus.toASN1Primitive());
        this.statusString = statusString;
        this.failInfo = failInfo;
    }
    
    public static PKIStatusInfo getInstance(final Object o) {
        if (o instanceof PKIStatusInfo) {
            return (PKIStatusInfo)o;
        }
        if (o != null) {
            return new PKIStatusInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static PKIStatusInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public DERBitString getFailInfo() {
        return this.failInfo;
    }
    
    public BigInteger getStatus() {
        return this.status.getValue();
    }
    
    public PKIFreeText getStatusString() {
        return this.statusString;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.status);
        final PKIFreeText statusString = this.statusString;
        if (statusString != null) {
            asn1EncodableVector.add(statusString);
        }
        final DERBitString failInfo = this.failInfo;
        if (failInfo != null) {
            asn1EncodableVector.add(failInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
