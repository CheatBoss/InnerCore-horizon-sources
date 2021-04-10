package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x509.*;
import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class PublishTrustAnchors extends ASN1Object
{
    private final ASN1Sequence anchorHashes;
    private final AlgorithmIdentifier hashAlgorithm;
    private final ASN1Integer seqNumber;
    
    public PublishTrustAnchors(final BigInteger bigInteger, final AlgorithmIdentifier hashAlgorithm, final byte[][] array) {
        this.seqNumber = new ASN1Integer(bigInteger);
        this.hashAlgorithm = hashAlgorithm;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != array.length; ++i) {
            asn1EncodableVector.add(new DEROctetString(Arrays.clone(array[i])));
        }
        this.anchorHashes = new DERSequence(asn1EncodableVector);
    }
    
    private PublishTrustAnchors(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.seqNumber = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
            this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.anchorHashes = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public static PublishTrustAnchors getInstance(final Object o) {
        if (o instanceof PublishTrustAnchors) {
            return (PublishTrustAnchors)o;
        }
        if (o != null) {
            return new PublishTrustAnchors(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[][] getAnchorHashes() {
        final int size = this.anchorHashes.size();
        final byte[][] array = new byte[size][];
        for (int i = 0; i != size; ++i) {
            array[i] = Arrays.clone(ASN1OctetString.getInstance(this.anchorHashes.getObjectAt(i)).getOctets());
        }
        return array;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public BigInteger getSeqNumber() {
        return this.seqNumber.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.seqNumber);
        asn1EncodableVector.add(this.hashAlgorithm);
        asn1EncodableVector.add(this.anchorHashes);
        return new DERSequence(asn1EncodableVector);
    }
}
