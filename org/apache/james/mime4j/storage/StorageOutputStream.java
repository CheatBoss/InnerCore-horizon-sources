package org.apache.james.mime4j.storage;

import java.io.*;

public abstract class StorageOutputStream extends OutputStream
{
    private boolean closed;
    private byte[] singleByte;
    private boolean usedUp;
    
    protected StorageOutputStream() {
    }
    
    @Override
    public void close() throws IOException {
        this.closed = true;
    }
    
    public final Storage toStorage() throws IOException {
        if (!this.usedUp) {
            if (!this.closed) {
                this.close();
            }
            this.usedUp = true;
            return this.toStorage0();
        }
        throw new IllegalStateException("toStorage may be invoked only once");
    }
    
    protected abstract Storage toStorage0() throws IOException;
    
    @Override
    public final void write(final int n) throws IOException {
        if (!this.closed) {
            if (this.singleByte == null) {
                this.singleByte = new byte[1];
            }
            final byte[] singleByte = this.singleByte;
            singleByte[0] = (byte)n;
            this.write0(singleByte, 0, 1);
            return;
        }
        throw new IOException("StorageOutputStream has been closed");
    }
    
    @Override
    public final void write(final byte[] array) throws IOException {
        if (this.closed) {
            throw new IOException("StorageOutputStream has been closed");
        }
        if (array == null) {
            throw null;
        }
        if (array.length == 0) {
            return;
        }
        this.write0(array, 0, array.length);
    }
    
    @Override
    public final void write(final byte[] array, final int n, final int n2) throws IOException {
        if (this.closed) {
            throw new IOException("StorageOutputStream has been closed");
        }
        if (array == null) {
            throw null;
        }
        if (n < 0 || n2 < 0 || n + n2 > array.length) {
            throw new IndexOutOfBoundsException();
        }
        if (n2 == 0) {
            return;
        }
        this.write0(array, n, n2);
    }
    
    protected abstract void write0(final byte[] p0, final int p1, final int p2) throws IOException;
}
