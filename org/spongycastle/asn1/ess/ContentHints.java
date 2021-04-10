package org.spongycastle.asn1.ess;

import org.spongycastle.asn1.*;

public class ContentHints extends ASN1Object
{
    private DERUTF8String contentDescription;
    private ASN1ObjectIdentifier contentType;
    
    public ContentHints(final ASN1ObjectIdentifier contentType) {
        this.contentType = contentType;
        this.contentDescription = null;
    }
    
    public ContentHints(final ASN1ObjectIdentifier contentType, final DERUTF8String contentDescription) {
        this.contentType = contentType;
        this.contentDescription = contentDescription;
    }
    
    private ContentHints(final ASN1Sequence asn1Sequence) {
        int n = 0;
        final ASN1Encodable object = asn1Sequence.getObjectAt(0);
        if (object.toASN1Primitive() instanceof DERUTF8String) {
            this.contentDescription = DERUTF8String.getInstance(object);
            n = 1;
        }
        this.contentType = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(n));
    }
    
    public static ContentHints getInstance(final Object o) {
        if (o instanceof ContentHints) {
            return (ContentHints)o;
        }
        if (o != null) {
            return new ContentHints(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public DERUTF8String getContentDescription() {
        return this.contentDescription;
    }
    
    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final DERUTF8String contentDescription = this.contentDescription;
        if (contentDescription != null) {
            asn1EncodableVector.add(contentDescription);
        }
        asn1EncodableVector.add(this.contentType);
        return new DERSequence(asn1EncodableVector);
    }
}
