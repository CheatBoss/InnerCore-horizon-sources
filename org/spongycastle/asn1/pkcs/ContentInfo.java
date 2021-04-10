package org.spongycastle.asn1.pkcs;

import java.util.*;
import org.spongycastle.asn1.*;

public class ContentInfo extends ASN1Object implements PKCSObjectIdentifiers
{
    private ASN1Encodable content;
    private ASN1ObjectIdentifier contentType;
    private boolean isBer;
    
    public ContentInfo(final ASN1ObjectIdentifier contentType, final ASN1Encodable content) {
        this.isBer = true;
        this.contentType = contentType;
        this.content = content;
    }
    
    private ContentInfo(final ASN1Sequence asn1Sequence) {
        this.isBer = true;
        final Enumeration objects = asn1Sequence.getObjects();
        this.contentType = objects.nextElement();
        if (objects.hasMoreElements()) {
            this.content = ((ASN1TaggedObject)objects.nextElement()).getObject();
        }
        this.isBer = (asn1Sequence instanceof BERSequence);
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
            asn1EncodableVector.add(new BERTaggedObject(true, 0, this.content));
        }
        if (this.isBer) {
            return new BERSequence(asn1EncodableVector);
        }
        return new DLSequence(asn1EncodableVector);
    }
}
