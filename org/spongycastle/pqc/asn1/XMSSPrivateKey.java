package org.spongycastle.pqc.asn1;

import org.spongycastle.util.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class XMSSPrivateKey extends ASN1Object
{
    private final byte[] bdsState;
    private final int index;
    private final byte[] publicSeed;
    private final byte[] root;
    private final byte[] secretKeyPRF;
    private final byte[] secretKeySeed;
    
    public XMSSPrivateKey(final int index, final byte[] array, final byte[] array2, final byte[] array3, final byte[] array4, final byte[] array5) {
        this.index = index;
        this.secretKeySeed = Arrays.clone(array);
        this.secretKeyPRF = Arrays.clone(array2);
        this.publicSeed = Arrays.clone(array3);
        this.root = Arrays.clone(array4);
        this.bdsState = Arrays.clone(array5);
    }
    
    private XMSSPrivateKey(final ASN1Sequence asn1Sequence) {
        if (!ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getValue().equals(BigInteger.valueOf(0L))) {
            throw new IllegalArgumentException("unknown version of sequence");
        }
        if (asn1Sequence.size() != 2 && asn1Sequence.size() != 3) {
            throw new IllegalArgumentException("key sequence wrong size");
        }
        final ASN1Sequence instance = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
        this.index = ASN1Integer.getInstance(instance.getObjectAt(0)).getValue().intValue();
        this.secretKeySeed = Arrays.clone(ASN1OctetString.getInstance(instance.getObjectAt(1)).getOctets());
        this.secretKeyPRF = Arrays.clone(ASN1OctetString.getInstance(instance.getObjectAt(2)).getOctets());
        this.publicSeed = Arrays.clone(ASN1OctetString.getInstance(instance.getObjectAt(3)).getOctets());
        this.root = Arrays.clone(ASN1OctetString.getInstance(instance.getObjectAt(4)).getOctets());
        byte[] clone;
        if (asn1Sequence.size() == 3) {
            clone = Arrays.clone(ASN1OctetString.getInstance(ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(2)), true).getOctets());
        }
        else {
            clone = null;
        }
        this.bdsState = clone;
    }
    
    public static XMSSPrivateKey getInstance(final Object o) {
        if (o instanceof XMSSPrivateKey) {
            return (XMSSPrivateKey)o;
        }
        if (o != null) {
            return new XMSSPrivateKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[] getBdsState() {
        return Arrays.clone(this.bdsState);
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public byte[] getPublicSeed() {
        return Arrays.clone(this.publicSeed);
    }
    
    public byte[] getRoot() {
        return Arrays.clone(this.root);
    }
    
    public byte[] getSecretKeyPRF() {
        return Arrays.clone(this.secretKeyPRF);
    }
    
    public byte[] getSecretKeySeed() {
        return Arrays.clone(this.secretKeySeed);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(0L));
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        asn1EncodableVector2.add(new ASN1Integer(this.index));
        asn1EncodableVector2.add(new DEROctetString(this.secretKeySeed));
        asn1EncodableVector2.add(new DEROctetString(this.secretKeyPRF));
        asn1EncodableVector2.add(new DEROctetString(this.publicSeed));
        asn1EncodableVector2.add(new DEROctetString(this.root));
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        asn1EncodableVector.add(new DERTaggedObject(true, 0, new DEROctetString(this.bdsState)));
        return new DERSequence(asn1EncodableVector);
    }
}
