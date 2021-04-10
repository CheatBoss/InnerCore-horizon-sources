package org.spongycastle.asn1;

import java.io.*;

public class BERSequenceGenerator extends BERGenerator
{
    public BERSequenceGenerator(final OutputStream outputStream) throws IOException {
        super(outputStream);
        this.writeBERHeader(48);
    }
    
    public BERSequenceGenerator(final OutputStream outputStream, final int n, final boolean b) throws IOException {
        super(outputStream, n, b);
        this.writeBERHeader(48);
    }
    
    public void addObject(final ASN1Encodable asn1Encodable) throws IOException {
        asn1Encodable.toASN1Primitive().encode(new BEROutputStream(this._out));
    }
    
    public void close() throws IOException {
        this.writeBEREnd();
    }
}
