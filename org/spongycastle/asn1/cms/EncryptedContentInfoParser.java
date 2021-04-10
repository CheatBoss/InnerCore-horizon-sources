package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import java.io.*;
import org.spongycastle.asn1.*;

public class EncryptedContentInfoParser
{
    private AlgorithmIdentifier _contentEncryptionAlgorithm;
    private ASN1ObjectIdentifier _contentType;
    private ASN1TaggedObjectParser _encryptedContent;
    
    public EncryptedContentInfoParser(final ASN1SequenceParser asn1SequenceParser) throws IOException {
        this._contentType = (ASN1ObjectIdentifier)asn1SequenceParser.readObject();
        this._contentEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1SequenceParser.readObject().toASN1Primitive());
        this._encryptedContent = (ASN1TaggedObjectParser)asn1SequenceParser.readObject();
    }
    
    public AlgorithmIdentifier getContentEncryptionAlgorithm() {
        return this._contentEncryptionAlgorithm;
    }
    
    public ASN1ObjectIdentifier getContentType() {
        return this._contentType;
    }
    
    public ASN1Encodable getEncryptedContent(final int n) throws IOException {
        return this._encryptedContent.getObjectParser(n, false);
    }
}
