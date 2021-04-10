package org.spongycastle.asn1.cmc;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class PendInfo extends ASN1Object
{
    private final ASN1GeneralizedTime pendTime;
    private final byte[] pendToken;
    
    private PendInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.pendToken = Arrays.clone(ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0)).getOctets());
            this.pendTime = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public PendInfo(final byte[] array, final ASN1GeneralizedTime pendTime) {
        this.pendToken = Arrays.clone(array);
        this.pendTime = pendTime;
    }
    
    public static PendInfo getInstance(final Object o) {
        if (o instanceof PendInfo) {
            return (PendInfo)o;
        }
        if (o != null) {
            return new PendInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1GeneralizedTime getPendTime() {
        return this.pendTime;
    }
    
    public byte[] getPendToken() {
        return this.pendToken;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DEROctetString(this.pendToken));
        asn1EncodableVector.add(this.pendTime);
        return new DERSequence(asn1EncodableVector);
    }
}
