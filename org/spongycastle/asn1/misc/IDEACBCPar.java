package org.spongycastle.asn1.misc;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class IDEACBCPar extends ASN1Object
{
    ASN1OctetString iv;
    
    public IDEACBCPar(final ASN1Sequence asn1Sequence) {
        ASN1OctetString iv;
        if (asn1Sequence.size() == 1) {
            iv = (ASN1OctetString)asn1Sequence.getObjectAt(0);
        }
        else {
            iv = null;
        }
        this.iv = iv;
    }
    
    public IDEACBCPar(final byte[] array) {
        this.iv = new DEROctetString(array);
    }
    
    public static IDEACBCPar getInstance(final Object o) {
        if (o instanceof IDEACBCPar) {
            return (IDEACBCPar)o;
        }
        if (o != null) {
            return new IDEACBCPar(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getIV() {
        final ASN1OctetString iv = this.iv;
        if (iv != null) {
            return Arrays.clone(iv.getOctets());
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1OctetString iv = this.iv;
        if (iv != null) {
            asn1EncodableVector.add(iv);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
