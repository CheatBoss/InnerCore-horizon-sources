package org.spongycastle.crypto.tls;

import java.io.*;

public class ByteQueueInputStream extends InputStream
{
    private ByteQueue buffer;
    
    public ByteQueueInputStream() {
        this.buffer = new ByteQueue();
    }
    
    public void addBytes(final byte[] array) {
        this.buffer.addData(array, 0, array.length);
    }
    
    @Override
    public int available() {
        return this.buffer.available();
    }
    
    @Override
    public void close() {
    }
    
    public int peek(final byte[] array) {
        final int min = Math.min(this.buffer.available(), array.length);
        this.buffer.read(array, 0, min, 0);
        return min;
    }
    
    @Override
    public int read() {
        if (this.buffer.available() == 0) {
            return -1;
        }
        return this.buffer.removeData(1, 0)[0] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array) {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, final int n, int min) {
        min = Math.min(this.buffer.available(), min);
        this.buffer.removeData(array, n, min, 0);
        return min;
    }
    
    @Override
    public long skip(final long n) {
        final int min = Math.min((int)n, this.buffer.available());
        this.buffer.removeData(min);
        return min;
    }
}
