package org.apache.james.mime4j.io;

import java.io.*;

public class LimitedInputStream extends PositionInputStream
{
    private final long limit;
    
    public LimitedInputStream(final InputStream inputStream, final long limit) {
        super(inputStream);
        if (limit >= 0L) {
            this.limit = limit;
            return;
        }
        throw new IllegalArgumentException("Limit may not be negative");
    }
    
    private void enforceLimit() throws IOException {
        if (this.position < this.limit) {
            return;
        }
        throw new IOException("Input stream limit exceeded");
    }
    
    private int getBytesLeft() {
        return (int)Math.min(2147483647L, this.limit - this.position);
    }
    
    @Override
    public int read() throws IOException {
        this.enforceLimit();
        return super.read();
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        this.enforceLimit();
        return super.read(array, n, Math.min(n2, this.getBytesLeft()));
    }
    
    @Override
    public long skip(final long n) throws IOException {
        this.enforceLimit();
        return super.skip(Math.min(n, this.getBytesLeft()));
    }
}
