package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class ContentInfo extends ASN1Object implements CMSObjectIdentifiers
{
    private ASN1Encodable content;
    private ASN1ObjectIdentifier contentType;
    
    public ContentInfo(final ASN1ObjectIdentifier contentType, final ASN1Encodable content) {
        this.contentType = contentType;
        this.content = content;
    }
    
    public ContentInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 1 || asn1Sequence.size() > 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        this.contentType = (ASN1ObjectIdentifier)asn1Sequence.getObjectAt(0);
        if (asn1Sequence.size() <= 1) {
            return;
        }
        final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(1);
        if (asn1TaggedObject.isExplicit() && asn1TaggedObject.getTagNo() == 0) {
            this.content = asn1TaggedObject.getObject();
            return;
        }
        throw new IllegalArgumentException("Bad tag for 'content'");
    }
    
    public static ContentInfo getInstance(final Object o) {
        if (o instanceof ContentInfo) {
            return (ContentInfo)o;
        }
        if (o != null) {
            return new ContentInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static ContentInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1Encodable getContent() {
        return this.content;
    }
    
    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.contentType);
        if (this.content != null) {
            asn1EncodableVector.add(new BERTaggedObject(0, this.content));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
