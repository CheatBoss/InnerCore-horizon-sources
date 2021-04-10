package org.spongycastle.pqc.asn1;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class GMSSPublicKey extends ASN1Object
{
    private byte[] publicKey;
    private ASN1Integer version;
    
    private GMSSPublicKey(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
            this.publicKey = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1)).getOctets();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("size of seq = ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public GMSSPublicKey(final byte[] publicKey) {
        this.version = new ASN1Integer(0L);
        this.publicKey = publicKey;
    }
    
    public static GMSSPublicKey getInstance(final Object o) {
        if (o instanceof GMSSPublicKey) {
            return (GMSSPublicKey)o;
        }
        if (o != null) {
            return new GMSSPublicKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getPublicKey() {
        return Arrays.clone(this.publicKey);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(new DEROctetString(this.publicKey));
        return new DERSequence(asn1EncodableVector);
    }
}
