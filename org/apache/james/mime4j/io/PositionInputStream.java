package org.apache.james.mime4j.io;

import java.io.*;

public class PositionInputStream extends FilterInputStream
{
    private long markedPosition;
    protected long position;
    
    public PositionInputStream(final InputStream inputStream) {
        super(inputStream);
        this.position = 0L;
        this.markedPosition = 0L;
    }
    
    @Override
    public int available() throws IOException {
        return this.in.available();
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
    }
    
    public long getPosition() {
        return this.position;
    }
    
    @Override
    public void mark(final int n) {
        this.in.mark(n);
        this.markedPosition = this.position;
    }
    
    @Override
    public boolean markSupported() {
        return this.in.markSupported();
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read != -1) {
            ++this.position;
        }
        return read;
    }
    
    @Override
    public int read(final byte[] array, int read, final int n) throws IOException {
        read = this.in.read(array, read, n);
        if (read > 0) {
            this.position += read;
        }
        return read;
    }
    
    @Override
    public void reset() throws IOException {
        this.in.reset();
        this.position = this.markedPosition;
    }
    
    @Override
    public long skip(long skip) throws IOException {
        skip = this.in.skip(skip);
        if (skip > 0L) {
            this.position += skip;
        }
        return skip;
    }
}
