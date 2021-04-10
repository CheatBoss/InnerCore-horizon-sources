package org.spongycastle.asn1;

import java.io.*;

public class DEROctetStringParser implements ASN1OctetStringParser
{
    private DefiniteLengthInputStream stream;
    
    DEROctetStringParser(final DefiniteLengthInputStream stream) {
        this.stream = stream;
    }
    
    @Override
    public ASN1Primitive getLoadedObject() throws IOException {
        return new DEROctetString(this.stream.toByteArray());
    }
    
    @Override
    public InputStream getOctetStream() {
        return this.stream;
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
