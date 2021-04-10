package org.spongycastle.asn1;

import java.io.*;

public class DERSequenceGenerator extends DERGenerator
{
    private final ByteArrayOutputStream _bOut;
    
    public DERSequenceGenerator(final OutputStream outputStream) throws IOException {
        super(outputStream);
        this._bOut = new ByteArrayOutputStream();
    }
    
    public DERSequenceGenerator(final OutputStream outputStream, final int n, final boolean b) throws IOException {
        super(outputStream, n, b);
        this._bOut = new ByteArrayOutputStream();
    }
    
    public void addObject(final ASN1Encodable asn1Encodable) throws IOException {
        asn1Encodable.toASN1Primitive().encode(new DEROutputStream(this._bOut));
    }
    
    public void close() throws IOException {
        this.writeDEREncoded(48, this._bOut.toByteArray());
    }
    
    @Override
    public OutputStream getRawOutputStream() {
        return this._bOut;
    }
}
