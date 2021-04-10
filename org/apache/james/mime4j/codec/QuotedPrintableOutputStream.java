package org.apache.james.mime4j.codec;

import java.io.*;

public class QuotedPrintableOutputStream extends FilterOutputStream
{
    private boolean closed;
    private QuotedPrintableEncoder encoder;
    
    public QuotedPrintableOutputStream(final OutputStream outputStream, final boolean b) {
        super(outputStream);
        this.closed = false;
        (this.encoder = new QuotedPrintableEncoder(1024, b)).initEncoding(outputStream);
    }
    
    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        try {
            this.encoder.completeEncoding();
        }
        finally {
            this.closed = true;
        }
    }
    
    @Override
    public void flush() throws IOException {
        this.encoder.flushOutput();
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.write(new byte[] { (byte)n }, 0, 1);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        if (!this.closed) {
            this.encoder.encodeChunk(array, n, n2);
            return;
        }
        throw new IOException("QuotedPrintableOutputStream has been closed");
    }
}
