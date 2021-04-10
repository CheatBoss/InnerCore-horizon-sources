package org.spongycastle.asn1;

import java.io.*;

public class DEROctetString extends ASN1OctetString
{
    public DEROctetString(final ASN1Encodable asn1Encodable) throws IOException {
        super(asn1Encodable.toASN1Primitive().getEncoded("DER"));
    }
    
    public DEROctetString(final byte[] array) {
        super(array);
    }
    
    static void encode(final DEROutputStream derOutputStream, final byte[] array) throws IOException {
        derOutputStream.writeEncoded(4, array);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(4, this.string);
    }
    
    @Override
    int encodedLength() {
        return StreamUtil.calculateBodyLength(this.string.length) + 1 + this.string.length;
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
}
