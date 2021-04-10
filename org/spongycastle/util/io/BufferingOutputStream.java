package org.spongycastle.util.io;

import java.io.*;
import org.spongycastle.util.*;

public class BufferingOutputStream extends OutputStream
{
    private final byte[] buf;
    private int bufOff;
    private final OutputStream other;
    
    public BufferingOutputStream(final OutputStream other) {
        this.other = other;
        this.buf = new byte[4096];
    }
    
    public BufferingOutputStream(final OutputStream other, final int n) {
        this.other = other;
        this.buf = new byte[n];
    }
    
    @Override
    public void close() throws IOException {
        this.flush();
        this.other.close();
    }
    
    @Override
    public void flush() throws IOException {
        this.other.write(this.buf, 0, this.bufOff);
        this.bufOff = 0;
        Arrays.fill(this.buf, (byte)0);
    }
    
    @Override
    public void write(final int n) throws IOException {
        final byte[] buf = this.buf;
        final int bufOff = this.bufOff;
        final int bufOff2 = bufOff + 1;
        this.bufOff = bufOff2;
        buf[bufOff] = (byte)n;
        if (bufOff2 == buf.length) {
            this.flush();
        }
    }
    
    @Override
    public void write(final byte[] array, int n, int n2) throws IOException {
        final byte[] buf = this.buf;
        final int length = buf.length;
        final int bufOff = this.bufOff;
        if (n2 < length - bufOff) {
            System.arraycopy(array, n, buf, bufOff, n2);
            this.bufOff += n2;
            return;
        }
        final int n3 = buf.length - bufOff;
        System.arraycopy(array, n, buf, bufOff, n3);
        this.bufOff += n3;
        this.flush();
        final int n4 = n + n3;
        n = n2 - n3;
        n2 = n4;
        byte[] buf2;
        while (true) {
            buf2 = this.buf;
            if (n < buf2.length) {
                break;
            }
            this.other.write(array, n2, buf2.length);
            final byte[] buf3 = this.buf;
            n2 += buf3.length;
            n -= buf3.length;
        }
        if (n > 0) {
            System.arraycopy(array, n2, buf2, this.bufOff, n);
            this.bufOff += n;
        }
    }
}
