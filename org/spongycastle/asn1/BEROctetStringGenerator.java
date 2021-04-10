package org.spongycastle.asn1;

import java.io.*;

public class BEROctetStringGenerator extends BERGenerator
{
    public BEROctetStringGenerator(final OutputStream outputStream) throws IOException {
        super(outputStream);
        this.writeBERHeader(36);
    }
    
    public BEROctetStringGenerator(final OutputStream outputStream, final int n, final boolean b) throws IOException {
        super(outputStream, n, b);
        this.writeBERHeader(36);
    }
    
    public OutputStream getOctetOutputStream() {
        return this.getOctetOutputStream(new byte[1000]);
    }
    
    public OutputStream getOctetOutputStream(final byte[] array) {
        return new BufferedBEROctetStream(array);
    }
    
    private class BufferedBEROctetStream extends OutputStream
    {
        private byte[] _buf;
        private DEROutputStream _derOut;
        private int _off;
        
        BufferedBEROctetStream(final byte[] buf) {
            this._buf = buf;
            this._off = 0;
            this._derOut = new DEROutputStream(BEROctetStringGenerator.this._out);
        }
        
        @Override
        public void close() throws IOException {
            final int off = this._off;
            if (off != 0) {
                final byte[] array = new byte[off];
                System.arraycopy(this._buf, 0, array, 0, off);
                DEROctetString.encode(this._derOut, array);
            }
            BEROctetStringGenerator.this.writeBEREnd();
        }
        
        @Override
        public void write(final int n) throws IOException {
            final byte[] buf = this._buf;
            final int off = this._off;
            final int off2 = off + 1;
            this._off = off2;
            buf[off] = (byte)n;
            if (off2 == buf.length) {
                DEROctetString.encode(this._derOut, buf);
                this._off = 0;
            }
        }
        
        @Override
        public void write(final byte[] array, int n, int i) throws IOException {
            while (i > 0) {
                final int min = Math.min(i, this._buf.length - this._off);
                System.arraycopy(array, n, this._buf, this._off, min);
                final int off = this._off + min;
                this._off = off;
                final byte[] buf = this._buf;
                if (off < buf.length) {
                    return;
                }
                DEROctetString.encode(this._derOut, buf);
                this._off = 0;
                n += min;
                i -= min;
            }
        }
    }
}
