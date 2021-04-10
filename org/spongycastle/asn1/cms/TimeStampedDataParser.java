package org.spongycastle.asn1.cms;

import java.io.*;
import org.spongycastle.asn1.*;

public class TimeStampedDataParser
{
    private ASN1OctetStringParser content;
    private DERIA5String dataUri;
    private MetaData metaData;
    private ASN1SequenceParser parser;
    private Evidence temporalEvidence;
    private ASN1Integer version;
    
    private TimeStampedDataParser(final ASN1SequenceParser parser) throws IOException {
        this.parser = parser;
        this.version = ASN1Integer.getInstance(parser.readObject());
        ASN1Encodable asn1Encodable2;
        final ASN1Encodable asn1Encodable = asn1Encodable2 = parser.readObject();
        if (asn1Encodable instanceof DERIA5String) {
            this.dataUri = DERIA5String.getInstance(asn1Encodable);
            asn1Encodable2 = parser.readObject();
        }
        ASN1Encodable object = null;
        Label_0089: {
            if (!(asn1Encodable2 instanceof MetaData)) {
                object = asn1Encodable2;
                if (!(asn1Encodable2 instanceof ASN1SequenceParser)) {
                    break Label_0089;
                }
            }
            this.metaData = MetaData.getInstance(asn1Encodable2.toASN1Primitive());
            object = parser.readObject();
        }
        if (object instanceof ASN1OctetStringParser) {
            this.content = (ASN1OctetStringParser)object;
        }
    }
    
    public static TimeStampedDataParser getInstance(final Object o) throws IOException {
        if (o instanceof ASN1Sequence) {
            return new TimeStampedDataParser(((ASN1Sequence)o).parser());
        }
        if (o instanceof ASN1SequenceParser) {
            return new TimeStampedDataParser((ASN1SequenceParser)o);
        }
        return null;
    }
    
    public ASN1OctetStringParser getContent() {
        return this.content;
    }
    
    public DERIA5String getDataUri() {
        return this.dataUri;
    }
    
    public MetaData getMetaData() {
        return this.metaData;
    }
    
    public Evidence getTemporalEvidence() throws IOException {
        if (this.temporalEvidence == null) {
            this.temporalEvidence = Evidence.getInstance(this.parser.readObject().toASN1Primitive());
        }
        return this.temporalEvidence;
    }
}
