package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import java.io.*;

public class CompressedDataParser
{
    private AlgorithmIdentifier _compressionAlgorithm;
    private ContentInfoParser _encapContentInfo;
    private ASN1Integer _version;
    
    public CompressedDataParser(final ASN1SequenceParser asn1SequenceParser) throws IOException {
        this._version = (ASN1Integer)asn1SequenceParser.readObject();
        this._compressionAlgorithm = AlgorithmIdentifier.getInstance(asn1SequenceParser.readObject().toASN1Primitive());
        this._encapContentInfo = new ContentInfoParser((ASN1SequenceParser)asn1SequenceParser.readObject());
    }
    
    public AlgorithmIdentifier getCompressionAlgorithmIdentifier() {
        return this._compressionAlgorithm;
    }
    
    public ContentInfoParser getEncapContentInfo() {
        return this._encapContentInfo;
    }
    
    public ASN1Integer getVersion() {
        return this._version;
    }
}
