package org.spongycastle.crypto.tls;

import java.io.*;

class TlsInputStream extends InputStream
{
    private byte[] buf;
    private TlsProtocol handler;
    
    TlsInputStream(final TlsProtocol handler) {
        this.buf = new byte[1];
        this.handler = null;
        this.handler = handler;
    }
    
    @Override
    public int available() throws IOException {
        return this.handler.applicationDataAvailable();
    }
    
    @Override
    public void close() throws IOException {
        this.handler.close();
    }
    
    @Override
    public int read() throws IOException {
        if (this.read(this.buf) < 0) {
            return -1;
        }
        return this.buf[0] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        return this.handler.readApplicationData(array, n, n2);
    }
}
