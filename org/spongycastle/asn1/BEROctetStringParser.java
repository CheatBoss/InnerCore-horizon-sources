package org.spongycastle.asn1;

import org.spongycastle.util.io.*;
import java.io.*;

public class BEROctetStringParser implements ASN1OctetStringParser
{
    private ASN1StreamParser _parser;
    
    BEROctetStringParser(final ASN1StreamParser parser) {
        this._parser = parser;
    }
    
    @Override
    public ASN1Primitive getLoadedObject() throws IOException {
        return new BEROctetString(Streams.readAll(this.getOctetStream()));
    }
    
    @Override
    public InputStream getOctetStream() {
        return new ConstructedOctetStream(this._parser);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        try {
            return this.getLoadedObject();
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("IOException converting stream to byte array: ");
            sb.append(ex.getMessage());
            throw new ASN1ParsingException(sb.toString(), ex);
        }
    }
}
