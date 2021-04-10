package org.spongycastle.asn1;

import java.io.*;

public class DERSequenceParser implements ASN1SequenceParser
{
    private ASN1StreamParser _parser;
    
    DERSequenceParser(final ASN1StreamParser parser) {
        this._parser = parser;
    }
    
    @Override
    public ASN1Primitive getLoadedObject() throws IOException {
        return new DERSequence(this._parser.readVector());
    }
    
    @Override
    public ASN1Encodable readObject() throws IOException {
        return this._parser.readObject();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            return this.getLoadedObject();
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}
