package com.bumptech.glide.util;

import java.io.*;

public class MarkEnforcingInputStream extends FilterInputStream
{
    private static final int END_OF_STREAM = -1;
    private static final int UNSET = Integer.MIN_VALUE;
    private int availableBytes;
    
    public MarkEnforcingInputStream(final InputStream inputStream) {
        super(inputStream);
        this.availableBytes = Integer.MIN_VALUE;
    }
    
    private long getBytesToRead(final long n) {
        if (this.availableBytes == 0) {
            return -1L;
        }
        if (this.availableBytes != Integer.MIN_VALUE && n > this.availableBytes) {
            return this.availableBytes;
        }
        return n;
    }
    
    private void updateAvailableBytesAfterRead(final long n) {
        if (this.availableBytes != Integer.MIN_VALUE && n != -1L) {
            this.availableBytes -= (int)n;
        }
    }
    
    @Override
    public int available() throws IOException {
        if (this.availableBytes == Integer.MIN_VALUE) {
            return super.available();
        }
        return Math.min(this.availableBytes, super.available());
    }
    
    @Override
    public void mark(final int availableBytes) {
        super.mark(availableBytes);
        this.availableBytes = availableBytes;
    }
    
    @Override
    public int read() throws IOException {
        if (this.getBytesToRead(1L) == -1L) {
            return -1;
        }
        final int read = super.read();
        this.updateAvailableBytesAfterRead(1L);
        return read;
    }
    
    @Override
    public int read(final byte[] array, int read, int n) throws IOException {
        n = (int)this.getBytesToRead(n);
        if (n == -1) {
            return -1;
        }
        read = super.read(array, read, n);
        this.updateAvailableBytesAfterRead(read);
        return read;
    }
    
    @Override
    public void reset() throws IOException {
        super.reset();
        this.availableBytes = Integer.MIN_VALUE;
    }
    
    @Override
    public long skip(long n) throws IOException {
        n = this.getBytesToRead(n);
        if (n == -1L) {
            return -1L;
        }
        n = super.skip(n);
        this.updateAvailableBytesAfterRead(n);
        return n;
    }
}
