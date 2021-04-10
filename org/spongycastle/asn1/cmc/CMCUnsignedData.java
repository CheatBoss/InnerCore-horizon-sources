package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public class CMCUnsignedData extends ASN1Object
{
    private final BodyPartPath bodyPartPath;
    private final ASN1Encodable content;
    private final ASN1ObjectIdentifier identifier;
    
    private CMCUnsignedData(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.bodyPartPath = BodyPartPath.getInstance(asn1Sequence.getObjectAt(0));
            this.identifier = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.content = asn1Sequence.getObjectAt(2);
            return;
        }
        throw new IllegalArgumentException("incorrect sequence size");
    }
    
    public CMCUnsignedData(final BodyPartPath bodyPartPath, final ASN1ObjectIdentifier identifier, final ASN1Encodable content) {
        this.bodyPartPath = bodyPartPath;
        this.identifier = identifier;
        this.content = content;
    }
    
    public static CMCUnsignedData getInstance(final Object o) {
        if (o instanceof CMCUnsignedData) {
            return (CMCUnsignedData)o;
        }
        if (o != null) {
            return new CMCUnsignedData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BodyPartPath getBodyPartPath() {
        return this.bodyPartPath;
    }
    
    public ASN1Encodable getContent() {
        return this.content;
    }
    
    public ASN1ObjectIdentifier getIdentifier() {
        return this.identifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.bodyPartPath);
        asn1EncodableVector.add(this.identifier);
        asn1EncodableVector.add(this.content);
        return new DERSequence(asn1EncodableVector);
    }
}
