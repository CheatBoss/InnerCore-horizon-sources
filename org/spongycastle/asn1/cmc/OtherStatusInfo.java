package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;
import java.io.*;

public class OtherStatusInfo extends ASN1Object implements ASN1Choice
{
    private final ExtendedFailInfo extendedFailInfo;
    private final CMCFailInfo failInfo;
    private final PendInfo pendInfo;
    
    OtherStatusInfo(final CMCFailInfo cmcFailInfo) {
        this(cmcFailInfo, null, null);
    }
    
    private OtherStatusInfo(final CMCFailInfo failInfo, final PendInfo pendInfo, final ExtendedFailInfo extendedFailInfo) {
        this.failInfo = failInfo;
        this.pendInfo = pendInfo;
        this.extendedFailInfo = extendedFailInfo;
    }
    
    OtherStatusInfo(final ExtendedFailInfo extendedFailInfo) {
        this(null, null, extendedFailInfo);
    }
    
    OtherStatusInfo(final PendInfo pendInfo) {
        this(null, pendInfo, null);
    }
    
    public static OtherStatusInfo getInstance(final Object o) {
        if (o instanceof OtherStatusInfo) {
            return (OtherStatusInfo)o;
        }
        if (o instanceof ASN1Encodable) {
            final ASN1Primitive asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
            if (asn1Primitive instanceof ASN1Integer) {
                return new OtherStatusInfo(CMCFailInfo.getInstance(asn1Primitive));
            }
            if (asn1Primitive instanceof ASN1Sequence) {
                if (((ASN1Sequence)asn1Primitive).getObjectAt(0) instanceof ASN1ObjectIdentifier) {
                    return new OtherStatusInfo(ExtendedFailInfo.getInstance(asn1Primitive));
                }
                return new OtherStatusInfo(PendInfo.getInstance(asn1Primitive));
            }
        }
        else if (o instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[])o));
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("parsing error: ");
                sb.append(ex.getMessage());
                throw new IllegalArgumentException(sb.toString());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("unknown object in getInstance(): ");
        sb2.append(o.getClass().getName());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public boolean isExtendedFailInfo() {
        return this.extendedFailInfo != null;
    }
    
    public boolean isFailInfo() {
        return this.failInfo != null;
    }
    
    public boolean isPendingInfo() {
        return this.pendInfo != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final PendInfo pendInfo = this.pendInfo;
        if (pendInfo != null) {
            return pendInfo.toASN1Primitive();
        }
        final CMCFailInfo failInfo = this.failInfo;
        if (failInfo != null) {
            return failInfo.toASN1Primitive();
        }
        return this.extendedFailInfo.toASN1Primitive();
    }
}
