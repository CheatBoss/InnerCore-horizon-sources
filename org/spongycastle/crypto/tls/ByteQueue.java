package org.spongycastle.crypto.tls;

import java.io.*;

public class ByteQueue
{
    private static final int DEFAULT_CAPACITY = 1024;
    private int available;
    private byte[] databuf;
    private boolean readOnlyBuf;
    private int skipped;
    
    public ByteQueue() {
        this(1024);
    }
    
    public ByteQueue(final int n) {
        this.skipped = 0;
        this.available = 0;
        this.readOnlyBuf = false;
        byte[] empty_BYTES;
        if (n == 0) {
            empty_BYTES = TlsUtils.EMPTY_BYTES;
        }
        else {
            empty_BYTES = new byte[n];
        }
        this.databuf = empty_BYTES;
    }
    
    public ByteQueue(final byte[] databuf, final int skipped, final int available) {
        this.skipped = 0;
        this.available = 0;
        this.readOnlyBuf = false;
        this.databuf = databuf;
        this.skipped = skipped;
        this.available = available;
        this.readOnlyBuf = true;
    }
    
    public static int nextTwoPow(int n) {
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        return (n | n >> 16) + 1;
    }
    
    public void addData(final byte[] array, final int n, final int n2) {
        if (!this.readOnlyBuf) {
            final int skipped = this.skipped;
            final int available = this.available;
            if (skipped + available + n2 > this.databuf.length) {
                final int nextTwoPow = nextTwoPow(available + n2);
                final byte[] databuf = this.databuf;
                if (nextTwoPow > databuf.length) {
                    final byte[] databuf2 = new byte[nextTwoPow];
                    System.arraycopy(databuf, this.skipped, databuf2, 0, this.available);
                    this.databuf = databuf2;
                }
                else {
                    System.arraycopy(databuf, this.skipped, databuf, 0, this.available);
                }
                this.skipped = 0;
            }
            System.arraycopy(array, n, this.databuf, this.skipped + this.available, n2);
            this.available += n2;
            return;
        }
        throw new IllegalStateException("Cannot add data to read-only buffer");
    }
    
    public int available() {
        return this.available;
    }
    
    public void copyTo(final OutputStream outputStream, final int n) throws IOException {
        if (n <= this.available) {
            outputStream.write(this.databuf, this.skipped, n);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot copy ");
        sb.append(n);
        sb.append(" bytes, only got ");
        sb.append(this.available);
        throw new IllegalStateException(sb.toString());
    }
    
    public void read(final byte[] array, final int n, final int n2, final int n3) {
        if (array.length - n < n2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Buffer size of ");
            sb.append(array.length);
            sb.append(" is too small for a read of ");
            sb.append(n2);
            sb.append(" bytes");
            throw new IllegalArgumentException(sb.toString());
        }
        if (this.available - n3 >= n2) {
            System.arraycopy(this.databuf, this.skipped + n3, array, n, n2);
            return;
        }
        throw new IllegalStateException("Not enough data to read");
    }
    
    public ByteArrayInputStream readFrom(final int n) {
        final int available = this.available;
        if (n <= available) {
            final int skipped = this.skipped;
            this.available = available - n;
            this.skipped = skipped + n;
            return new ByteArrayInputStream(this.databuf, skipped, n);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot read ");
        sb.append(n);
        sb.append(" bytes, only got ");
        sb.append(this.available);
        throw new IllegalStateException(sb.toString());
    }
    
    public void removeData(final int n) {
        final int available = this.available;
        if (n <= available) {
            this.available = available - n;
            this.skipped += n;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot remove ");
        sb.append(n);
        sb.append(" bytes, only got ");
        sb.append(this.available);
        throw new IllegalStateException(sb.toString());
    }
    
    public void removeData(final byte[] array, final int n, final int n2, final int n3) {
        this.read(array, n, n2, n3);
        this.removeData(n3 + n2);
    }
    
    public byte[] removeData(final int n, final int n2) {
        final byte[] array = new byte[n];
        this.removeData(array, 0, n, n2);
        return array;
    }
    
    public void shrink() {
        final int available = this.available;
        if (available == 0) {
            this.databuf = TlsUtils.EMPTY_BYTES;
            this.skipped = 0;
            return;
        }
        final int nextTwoPow = nextTwoPow(available);
        final byte[] databuf = this.databuf;
        if (nextTwoPow < databuf.length) {
            final byte[] databuf2 = new byte[nextTwoPow];
            System.arraycopy(databuf, this.skipped, databuf2, 0, this.available);
            this.databuf = databuf2;
            this.skipped = 0;
        }
    }
}
