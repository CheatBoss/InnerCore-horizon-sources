package org.spongycastle.asn1;

import java.io.*;

public class BERApplicationSpecificParser implements ASN1ApplicationSpecificParser
{
    private final ASN1StreamParser parser;
    private final int tag;
    
    BERApplicationSpecificParser(final int tag, final ASN1StreamParser parser) {
        this.tag = tag;
        this.parser = parser;
    }
    
    @Override
    public ASN1Primitive getLoadedObject() throws IOException {
        return new BERApplicationSpecific(this.tag, this.parser.readVector());
    }
    
    @Override
    public ASN1Encodable readObject() throws IOException {
        return this.parser.readObject();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            return this.getLoadedObject();
        }
        catch (IOException ex) {
            throw new ASN1ParsingException(ex.getMessage(), ex);
        }
    }
}
