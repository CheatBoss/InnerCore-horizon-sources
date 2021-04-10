package org.spongycastle.asn1.isismtt.ocsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class CertHash extends ASN1Object
{
    private byte[] certificateHash;
    private AlgorithmIdentifier hashAlgorithm;
    
    private CertHash(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.certificateHash = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1)).getOctets();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public CertHash(final AlgorithmIdentifier hashAlgorithm, final byte[] array) {
        this.hashAlgorithm = hashAlgorithm;
        System.arraycopy(array, 0, this.certificateHash = new byte[array.length], 0, array.length);
    }
    
    public static CertHash getInstance(final Object o) {
        if (o == null || o instanceof CertHash) {
            return (CertHash)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertHash((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public byte[] getCertificateHash() {
        return this.certificateHash;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.hashAlgorithm);
        asn1EncodableVector.add(new DEROctetString(this.certificateHash));
        return new DERSequence(asn1EncodableVector);
    }
}
