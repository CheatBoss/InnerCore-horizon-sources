package org.spongycastle.asn1.cms;

import java.io.*;
import org.spongycastle.asn1.*;

public class ContentInfoParser
{
    private ASN1TaggedObjectParser content;
    private ASN1ObjectIdentifier contentType;
    
    public ContentInfoParser(final ASN1SequenceParser asn1SequenceParser) throws IOException {
        this.contentType = (ASN1ObjectIdentifier)asn1SequenceParser.readObject();
        this.content = (ASN1TaggedObjectParser)asn1SequenceParser.readObject();
    }
    
    public ASN1Encodable getContent(final int n) throws IOException {
        final ASN1TaggedObjectParser content = this.content;
        if (content != null) {
            return content.getObjectParser(n, true);
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getContentType() {
        return this.contentType;
    }
}
