package org.spongycastle.asn1.cms;

import java.io.*;
import org.spongycastle.asn1.*;

public class SignedDataParser
{
    private boolean _certsCalled;
    private boolean _crlsCalled;
    private Object _nextObject;
    private ASN1SequenceParser _seq;
    private ASN1Integer _version;
    
    private SignedDataParser(final ASN1SequenceParser seq) throws IOException {
        this._seq = seq;
        this._version = (ASN1Integer)seq.readObject();
    }
    
    public static SignedDataParser getInstance(final Object o) throws IOException {
        if (o instanceof ASN1Sequence) {
            return new SignedDataParser(((ASN1Sequence)o).parser());
        }
        if (o instanceof ASN1SequenceParser) {
            return new SignedDataParser((ASN1SequenceParser)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object encountered: ");
        sb.append(o.getClass().getName());
        throw new IOException(sb.toString());
    }
    
    public ASN1SetParser getCertificates() throws IOException {
        this._certsCalled = true;
        final ASN1Encodable object = this._seq.readObject();
        this._nextObject = object;
        if (object instanceof ASN1TaggedObjectParser && ((ASN1TaggedObjectParser)object).getTagNo() == 0) {
            final ASN1SetParser asn1SetParser = (ASN1SetParser)((ASN1TaggedObjectParser)this._nextObject).getObjectParser(17, false);
            this._nextObject = null;
            return asn1SetParser;
        }
        return null;
    }
    
    public ASN1SetParser getCrls() throws IOException {
        if (!this._certsCalled) {
            throw new IOException("getCerts() has not been called.");
        }
        this._crlsCalled = true;
        if (this._nextObject == null) {
            this._nextObject = this._seq.readObject();
        }
        final Object nextObject = this._nextObject;
        if (nextObject instanceof ASN1TaggedObjectParser && ((ASN1TaggedObjectParser)nextObject).getTagNo() == 1) {
            final ASN1SetParser asn1SetParser = (ASN1SetParser)((ASN1TaggedObjectParser)this._nextObject).getObjectParser(17, false);
            this._nextObject = null;
            return asn1SetParser;
        }
        return null;
    }
    
    public ASN1SetParser getDigestAlgorithms() throws IOException {
        final ASN1Encodable object = this._seq.readObject();
        if (object instanceof ASN1Set) {
            return ((ASN1Set)object).parser();
        }
        return (ASN1SetParser)object;
    }
    
    public ContentInfoParser getEncapContentInfo() throws IOException {
        return new ContentInfoParser((ASN1SequenceParser)this._seq.readObject());
    }
    
    public ASN1SetParser getSignerInfos() throws IOException {
        if (this._certsCalled && this._crlsCalled) {
            if (this._nextObject == null) {
                this._nextObject = this._seq.readObject();
            }
            return (ASN1SetParser)this._nextObject;
        }
        throw new IOException("getCerts() and/or getCrls() has not been called.");
    }
    
    public ASN1Integer getVersion() {
        return this._version;
    }
}
