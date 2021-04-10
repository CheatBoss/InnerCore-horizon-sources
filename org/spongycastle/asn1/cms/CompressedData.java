package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class CompressedData extends ASN1Object
{
    private AlgorithmIdentifier compressionAlgorithm;
    private ContentInfo encapContentInfo;
    private ASN1Integer version;
    
    private CompressedData(final ASN1Sequence asn1Sequence) {
        this.version = (ASN1Integer)asn1Sequence.getObjectAt(0);
        this.compressionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.encapContentInfo = ContentInfo.getInstance(asn1Sequence.getObjectAt(2));
    }
    
    public CompressedData(final AlgorithmIdentifier compressionAlgorithm, final ContentInfo encapContentInfo) {
        this.version = new ASN1Integer(0L);
        this.compressionAlgorithm = compressionAlgorithm;
        this.encapContentInfo = encapContentInfo;
    }
    
    public static CompressedData getInstance(final Object o) {
        if (o instanceof CompressedData) {
            return (CompressedData)o;
        }
        if (o != null) {
            return new CompressedData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static CompressedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public AlgorithmIdentifier getCompressionAlgorithmIdentifier() {
        return this.compressionAlgorithm;
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
        asn1EncodableVector.add(this.compressionAlgorithm);
        asn1EncodableVector.add(this.encapContentInfo);
        return new BERSequence(asn1EncodableVector);
    }
}
