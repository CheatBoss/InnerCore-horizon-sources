package org.spongycastle.asn1;

import java.io.*;

public class ASN1OutputStream
{
    private OutputStream os;
    
    public ASN1OutputStream(final OutputStream os) {
        this.os = os;
    }
    
    public void close() throws IOException {
        this.os.close();
    }
    
    public void flush() throws IOException {
        this.os.flush();
    }
    
    ASN1OutputStream getDERSubStream() {
        return new DEROutputStream(this.os);
    }
    
    ASN1OutputStream getDLSubStream() {
        return new DLOutputStream(this.os);
    }
    
    void write(final int n) throws IOException {
        this.os.write(n);
    }
    
    void write(final byte[] array) throws IOException {
        this.os.write(array);
    }
    
    void write(final byte[] array, final int n, final int n2) throws IOException {
        this.os.write(array, n, n2);
    }
    
    void writeEncoded(final int n, final int n2, final byte[] array) throws IOException {
        this.writeTag(n, n2);
        this.writeLength(array.length);
        this.write(array);
    }
    
    void writeEncoded(final int n, final byte[] array) throws IOException {
        this.write(n);
        this.writeLength(array.length);
        this.write(array);
    }
    
    void writeImplicitObject(final ASN1Primitive asn1Primitive) throws IOException {
        if (asn1Primitive != null) {
            asn1Primitive.encode(new ImplicitOutputStream(this.os));
            return;
        }
        throw new IOException("null object detected");
    }
    
    void writeLength(final int n) throws IOException {
        if (n > 127) {
            int n2 = n;
            int n3 = 1;
            while (true) {
                n2 >>>= 8;
                if (n2 == 0) {
                    break;
                }
                ++n3;
            }
            this.write((byte)(n3 | 0x80));
            for (int i = (n3 - 1) * 8; i >= 0; i -= 8) {
                this.write((byte)(n >> i));
            }
        }
        else {
            this.write((byte)n);
        }
    }
    
    protected void writeNull() throws IOException {
        this.os.write(5);
        this.os.write(0);
    }
    
    public void writeObject(final ASN1Encodable asn1Encodable) throws IOException {
        if (asn1Encodable != null) {
            asn1Encodable.toASN1Primitive().encode(this);
            return;
        }
        throw new IOException("null object detected");
    }
    
    void writeTag(int n, int n2) throws IOException {
        if (n2 < 31) {
            this.write(n | n2);
            return;
        }
        this.write(n | 0x1F);
        if (n2 < 128) {
            this.write(n2);
            return;
        }
        final byte[] array = new byte[5];
        final byte b = (byte)(n2 & 0x7F);
        n = 4;
        array[4] = b;
        int n3;
        int n4;
        do {
            n3 = n2 >> 7;
            n4 = n - 1;
            array[n4] = (byte)((n3 & 0x7F) | 0x80);
            n = n4;
        } while ((n2 = n3) > 127);
        this.write(array, n4, 5 - n4);
    }
    
    private class ImplicitOutputStream extends ASN1OutputStream
    {
        private boolean first;
        
        public ImplicitOutputStream(final OutputStream outputStream) {
            super(outputStream);
            this.first = true;
        }
        
        public void write(final int n) throws IOException {
            if (this.first) {
                this.first = false;
                return;
            }
            super.write(n);
        }
    }
}
