package org.spongycastle.asn1.cryptopro;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class Gost2814789KeyWrapParameters extends ASN1Object
{
    private final ASN1ObjectIdentifier encryptionParamSet;
    private final byte[] ukm;
    
    public Gost2814789KeyWrapParameters(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        this(asn1ObjectIdentifier, null);
    }
    
    public Gost2814789KeyWrapParameters(final ASN1ObjectIdentifier encryptionParamSet, final byte[] array) {
        this.encryptionParamSet = encryptionParamSet;
        this.ukm = Arrays.clone(array);
    }
    
    private Gost2814789KeyWrapParameters(final ASN1Sequence asn1Sequence) {
        byte[] octets;
        if (asn1Sequence.size() == 2) {
            this.encryptionParamSet = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            octets = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1)).getOctets();
        }
        else {
            if (asn1Sequence.size() != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown sequence length: ");
                sb.append(asn1Sequence.size());
                throw new IllegalArgumentException(sb.toString());
            }
            this.encryptionParamSet = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            octets = null;
        }
        this.ukm = octets;
    }
    
    public static Gost2814789KeyWrapParameters getInstance(final Object o) {
        if (o instanceof Gost2814789KeyWrapParameters) {
            return (Gost2814789KeyWrapParameters)o;
        }
        if (o != null) {
            return new Gost2814789KeyWrapParameters(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getEncryptionParamSet() {
        return this.encryptionParamSet;
    }
    
    public byte[] getUkm() {
        return this.ukm;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.encryptionParamSet);
        if (this.ukm != null) {
            asn1EncodableVector.add(new DEROctetString(this.ukm));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
