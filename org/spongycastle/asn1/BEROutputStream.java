package org.spongycastle.asn1;

import java.io.*;

public class BEROutputStream extends DEROutputStream
{
    public BEROutputStream(final OutputStream outputStream) {
        super(outputStream);
    }
    
    public void writeObject(final Object o) throws IOException {
        if (o == null) {
            this.writeNull();
            return;
        }
        ASN1Primitive asn1Primitive;
        if (o instanceof ASN1Primitive) {
            asn1Primitive = (ASN1Primitive)o;
        }
        else {
            if (!(o instanceof ASN1Encodable)) {
                throw new IOException("object not BEREncodable");
            }
            asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
        }
        asn1Primitive.encode(this);
    }
}
