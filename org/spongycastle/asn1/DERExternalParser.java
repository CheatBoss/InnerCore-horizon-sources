package org.spongycastle.asn1;

import java.io.*;

public class DERExternalParser implements ASN1Encodable, InMemoryRepresentable
{
    private ASN1StreamParser _parser;
    
    public DERExternalParser(final ASN1StreamParser parser) {
        this._parser = parser;
    }
    
    @Override
    public ASN1Primitive getLoadedObject() throws IOException {
        try {
            return new DERExternal(this._parser.readVector());
        }
        catch (IllegalArgumentException ex) {
            throw new ASN1Exception(ex.getMessage(), ex);
        }
    }
    
    public ASN1Encodable readObject() throws IOException {
        return this._parser.readObject();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            return this.getLoadedObject();
        }
        catch (IllegalArgumentException ex) {
            throw new ASN1ParsingException("unable to get DER object", ex);
        }
        catch (IOException ex2) {
            throw new ASN1ParsingException("unable to get DER object", ex2);
        }
    }
}
