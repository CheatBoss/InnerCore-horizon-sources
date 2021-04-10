package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class DigestedData extends ASN1Object
{
    private ASN1OctetString digest;
    private AlgorithmIdentifier digestAlgorithm;
    private ContentInfo encapContentInfo;
    private ASN1Integer version;
    
    private DigestedData(final ASN1Sequence asn1Sequence) {
        this.version = (ASN1Integer)asn1Sequence.getObjectAt(0);
        this.digestAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.encapContentInfo = ContentInfo.getInstance(asn1Sequence.getObjectAt(2));
        this.digest = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(3));
    }
    
    public DigestedData(final AlgorithmIdentifier digestAlgorithm, final ContentInfo encapContentInfo, final byte[] array) {
        this.version = new ASN1Integer(0L);
        this.digestAlgorithm = digestAlgorithm;
        this.encapContentInfo = encapContentInfo;
        this.digest = new DEROctetString(array);
    }
    
    public static DigestedData getInstance(final Object o) {
        if (o instanceof DigestedData) {
            return (DigestedData)o;
        }
        if (o != null) {
            return new DigestedData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static DigestedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public byte[] getDigest() {
        return this.digest.getOctets();
    }
    
    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }
    
    public ContentInfo getEncapContentInfo() {
        return this.encapContentInfo;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.digestAlgorithm);
        asn1EncodableVector.add(this.encapContentInfo);
        asn1EncodableVector.add(this.digest);
        return new BERSequence(asn1EncodableVector);
    }
}
