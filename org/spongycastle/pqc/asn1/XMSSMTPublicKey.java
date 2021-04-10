package org.spongycastle.pqc.asn1;

import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class XMSSMTPublicKey extends ASN1Object
{
    private final byte[] publicSeed;
    private final byte[] root;
    
    private XMSSMTPublicKey(final ASN1Sequence asn1Sequence) {
        if (ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getValue().equals(BigInteger.valueOf(0L))) {
            this.publicSeed = Arrays.clone(ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1)).getOctets());
            this.root = Arrays.clone(ASN1OctetString.getInstance(asn1Sequence.getObjectAt(2)).getOctets());
            return;
        }
        throw new IllegalArgumentException("unknown version of sequence");
    }
    
    public XMSSMTPublicKey(final byte[] array, final byte[] array2) {
        this.publicSeed = Arrays.clone(array);
        this.root = Arrays.clone(array2);
    }
    
    public static XMSSMTPublicKey getInstance(final Object o) {
        if (o instanceof XMSSMTPublicKey) {
            return (XMSSMTPublicKey)o;
        }
        if (o != null) {
            return new XMSSMTPublicKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getPublicSeed() {
        return Arrays.clone(this.publicSeed);
    }
    
    public byte[] getRoot() {
        return Arrays.clone(this.root);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(0L));
        asn1EncodableVector.add(new DEROctetString(this.publicSeed));
        asn1EncodableVector.add(new DEROctetString(this.root));
        return new DERSequence(asn1EncodableVector);
    }
}
