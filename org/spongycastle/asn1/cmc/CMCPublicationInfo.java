package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.crmf.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class CMCPublicationInfo extends ASN1Object
{
    private final ASN1Sequence certHashes;
    private final AlgorithmIdentifier hashAlg;
    private final PKIPublicationInfo pubInfo;
    
    private CMCPublicationInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.hashAlg = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.certHashes = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
            this.pubInfo = PKIPublicationInfo.getInstance(asn1Sequence.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public CMCPublicationInfo(final AlgorithmIdentifier hashAlg, final byte[][] array, final PKIPublicationInfo pubInfo) {
        this.hashAlg = hashAlg;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != array.length; ++i) {
            asn1EncodableVector.add(new DEROctetString(Arrays.clone(array[i])));
        }
        this.certHashes = new DERSequence(asn1EncodableVector);
        this.pubInfo = pubInfo;
    }
    
    public static CMCPublicationInfo getInstance(final Object o) {
        if (o instanceof CMCPublicationInfo) {
            return (CMCPublicationInfo)o;
        }
        if (o != null) {
            return new CMCPublicationInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public byte[][] getCertHashes() {
        final int size = this.certHashes.size();
        final byte[][] array = new byte[size][];
        for (int i = 0; i != size; ++i) {
            array[i] = Arrays.clone(ASN1OctetString.getInstance(this.certHashes.getObjectAt(i)).getOctets());
        }
        return array;
    }
    
    public AlgorithmIdentifier getHashAlg() {
        return this.hashAlg;
    }
    
    public PKIPublicationInfo getPubInfo() {
        return this.pubInfo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.hashAlg);
        asn1EncodableVector.add(this.certHashes);
        asn1EncodableVector.add(this.pubInfo);
        return new DERSequence(asn1EncodableVector);
    }
}
