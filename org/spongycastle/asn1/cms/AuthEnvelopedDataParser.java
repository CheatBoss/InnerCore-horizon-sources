package org.spongycastle.asn1.cms;

import java.io.*;
import org.spongycastle.asn1.*;

public class AuthEnvelopedDataParser
{
    private EncryptedContentInfoParser authEncryptedContentInfoParser;
    private ASN1Encodable nextObject;
    private boolean originatorInfoCalled;
    private ASN1SequenceParser seq;
    private ASN1Integer version;
    
    public AuthEnvelopedDataParser(final ASN1SequenceParser seq) throws IOException {
        this.seq = seq;
        final ASN1Integer instance = ASN1Integer.getInstance(seq.readObject());
        this.version = instance;
        if (instance.getValue().intValue() == 0) {
            return;
        }
        throw new ASN1ParsingException("AuthEnvelopedData version number must be 0");
    }
    
    public ASN1SetParser getAuthAttrs() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        final ASN1Encodable nextObject = this.nextObject;
        if (nextObject instanceof ASN1TaggedObjectParser) {
            this.nextObject = null;
            return (ASN1SetParser)((ASN1TaggedObjectParser)nextObject).getObjectParser(17, false);
        }
        if (this.authEncryptedContentInfoParser.getContentType().equals(CMSObjectIdentifiers.data)) {
            return null;
        }
        throw new ASN1ParsingException("authAttrs must be present with non-data content");
    }
    
    public EncryptedContentInfoParser getAuthEncryptedContentInfo() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        final ASN1Encodable nextObject = this.nextObject;
        EncryptedContentInfoParser authEncryptedContentInfoParser = null;
        if (nextObject != null) {
            final ASN1SequenceParser asn1SequenceParser = (ASN1SequenceParser)nextObject;
            this.nextObject = null;
            authEncryptedContentInfoParser = new EncryptedContentInfoParser(asn1SequenceParser);
            this.authEncryptedContentInfoParser = authEncryptedContentInfoParser;
        }
        return authEncryptedContentInfoParser;
    }
    
    public ASN1OctetString getMac() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        final ASN1Encodable nextObject = this.nextObject;
        this.nextObject = null;
        return ASN1OctetString.getInstance(nextObject.toASN1Primitive());
    }
    
    public OriginatorInfo getOriginatorInfo() throws IOException {
        this.originatorInfoCalled = true;
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        final ASN1Encodable nextObject = this.nextObject;
        if (nextObject instanceof ASN1TaggedObjectParser && ((ASN1TaggedObjectParser)nextObject).getTagNo() == 0) {
            final ASN1SequenceParser asn1SequenceParser = (ASN1SequenceParser)((ASN1TaggedObjectParser)this.nextObject).getObjectParser(16, false);
            this.nextObject = null;
            return OriginatorInfo.getInstance(asn1SequenceParser.toASN1Primitive());
        }
        return null;
    }
    
    public ASN1SetParser getRecipientInfos() throws IOException {
        if (!this.originatorInfoCalled) {
            this.getOriginatorInfo();
        }
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        final ASN1SetParser asn1SetParser = (ASN1SetParser)this.nextObject;
        this.nextObject = null;
        return asn1SetParser;
    }
    
    public ASN1SetParser getUnauthAttrs() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        final ASN1Encodable nextObject = this.nextObject;
        if (nextObject != null) {
            this.nextObject = null;
            return (ASN1SetParser)((ASN1TaggedObjectParser)nextObject).getObjectParser(17, false);
        }
        return null;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
}
