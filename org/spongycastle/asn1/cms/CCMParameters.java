package org.spongycastle.asn1.cms;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class CCMParameters extends ASN1Object
{
    private int icvLen;
    private byte[] nonce;
    
    private CCMParameters(final ASN1Sequence asn1Sequence) {
        this.nonce = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0)).getOctets();
        int intValue;
        if (asn1Sequence.size() == 2) {
            intValue = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1)).getValue().intValue();
        }
        else {
            intValue = 12;
        }
        this.icvLen = intValue;
    }
    
    public CCMParameters(final byte[] array, final int icvLen) {
        this.nonce = Arrays.clone(array);
        this.icvLen = icvLen;
    }
    
    public static CCMParameters getInstance(final Object o) {
        if (o instanceof CCMParameters) {
            return (CCMParameters)o;
        }
        if (o != null) {
            return new CCMParameters(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public int getIcvLen() {
        return this.icvLen;
    }
    
    public byte[] getNonce() {
        return Arrays.clone(this.nonce);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DEROctetString(this.nonce));
        if (this.icvLen != 12) {
            asn1EncodableVector.add(new ASN1Integer(this.icvLen));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
