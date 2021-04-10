package org.spongycastle.asn1;

import java.io.*;

public class DLOutputStream extends ASN1OutputStream
{
    public DLOutputStream(final OutputStream outputStream) {
        super(outputStream);
    }
    
    @Override
    public void writeObject(final ASN1Encodable asn1Encodable) throws IOException {
        if (asn1Encodable != null) {
            asn1Encodable.toASN1Primitive().toDLObject().encode(this);
            return;
        }
        throw new IOException("null object detected");
    }
}
