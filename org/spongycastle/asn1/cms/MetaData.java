package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class MetaData extends ASN1Object
{
    private DERUTF8String fileName;
    private ASN1Boolean hashProtected;
    private DERIA5String mediaType;
    private Attributes otherMetaData;
    
    public MetaData(final ASN1Boolean hashProtected, final DERUTF8String fileName, final DERIA5String mediaType, final Attributes otherMetaData) {
        this.hashProtected = hashProtected;
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.otherMetaData = otherMetaData;
    }
    
    private MetaData(final ASN1Sequence asn1Sequence) {
        this.hashProtected = ASN1Boolean.getInstance(asn1Sequence.getObjectAt(0));
        final int size = asn1Sequence.size();
        int n2;
        final int n = n2 = 1;
        if (1 < size) {
            n2 = n;
            if (asn1Sequence.getObjectAt(1) instanceof DERUTF8String) {
                this.fileName = DERUTF8String.getInstance(asn1Sequence.getObjectAt(1));
                n2 = 2;
            }
        }
        int n3;
        if ((n3 = n2) < asn1Sequence.size()) {
            n3 = n2;
            if (asn1Sequence.getObjectAt(n2) instanceof DERIA5String) {
                this.mediaType = DERIA5String.getInstance(asn1Sequence.getObjectAt(n2));
                n3 = n2 + 1;
            }
        }
        if (n3 < asn1Sequence.size()) {
            this.otherMetaData = Attributes.getInstance(asn1Sequence.getObjectAt(n3));
        }
    }
    
    public static MetaData getInstance(final Object o) {
        if (o instanceof MetaData) {
            return (MetaData)o;
        }
        if (o != null) {
            return new MetaData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public DERUTF8String getFileName() {
        return this.fileName;
    }
    
    public DERIA5String getMediaType() {
        return this.mediaType;
    }
    
    public Attributes getOtherMetaData() {
        return this.otherMetaData;
    }
    
    public boolean isHashProtected() {
        return this.hashProtected.isTrue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.hashProtected);
        final DERUTF8String fileName = this.fileName;
        if (fileName != null) {
            asn1EncodableVector.add(fileName);
        }
        final DERIA5String mediaType = this.mediaType;
        if (mediaType != null) {
            asn1EncodableVector.add(mediaType);
        }
        final Attributes otherMetaData = this.otherMetaData;
        if (otherMetaData != null) {
            asn1EncodableVector.add(otherMetaData);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
