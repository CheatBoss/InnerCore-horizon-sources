package org.spongycastle.asn1;

import java.io.*;

public abstract class ASN1Primitive extends ASN1Object
{
    ASN1Primitive() {
    }
    
    public static ASN1Primitive fromByteArray(final byte[] array) throws IOException {
        final ASN1InputStream asn1InputStream = new ASN1InputStream(array);
        try {
            final ASN1Primitive object = asn1InputStream.readObject();
            if (asn1InputStream.available() == 0) {
                return object;
            }
            throw new IOException("Extra data detected in stream");
        }
        catch (ClassCastException ex) {
            throw new IOException("cannot recognise object in stream");
        }
    }
    
    abstract boolean asn1Equals(final ASN1Primitive p0);
    
    abstract void encode(final ASN1OutputStream p0) throws IOException;
    
    abstract int encodedLength() throws IOException;
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o instanceof ASN1Encodable && this.asn1Equals(((ASN1Encodable)o).toASN1Primitive()));
    }
    
    @Override
    public abstract int hashCode();
    
    abstract boolean isConstructed();
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this;
    }
    
    ASN1Primitive toDERObject() {
        return this;
    }
    
    ASN1Primitive toDLObject() {
        return this;
    }
}
