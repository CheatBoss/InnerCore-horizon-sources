package org.apache.james.mime4j.io;

import java.io.*;
import org.apache.james.mime4j.util.*;

public class BufferedLineReaderInputStream extends LineReaderInputStream
{
    private byte[] buffer;
    private int buflen;
    private int bufpos;
    private final int maxLineLen;
    private boolean truncated;
    
    public BufferedLineReaderInputStream(final InputStream inputStream, final int n) {
        this(inputStream, n, -1);
    }
    
    public BufferedLineReaderInputStream(final InputStream inputStream, final int n, final int maxLineLen) {
        super(inputStream);
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream may not be null");
        }
        if (n > 0) {
            this.buffer = new byte[n];
            this.bufpos = 0;
            this.buflen = 0;
            this.maxLineLen = maxLineLen;
            this.truncated = false;
            return;
        }
        throw new IllegalArgumentException("Buffer size may not be negative or zero");
    }
    
    private void expand(int bufpos) {
        final byte[] buffer = new byte[bufpos];
        final int buflen = this.buflen;
        bufpos = this.bufpos;
        final int n = buflen - bufpos;
        if (n > 0) {
            System.arraycopy(this.buffer, bufpos, buffer, bufpos, n);
        }
        this.buffer = buffer;
    }
    
    public byte[] buf() {
        return this.buffer;
    }
    
    public int capacity() {
        return this.buffer.length;
    }
    
    public byte charAt(final int n) {
        if (n >= this.bufpos && n <= this.buflen) {
            return this.buffer[n];
        }
        throw new IndexOutOfBoundsException();
    }
    
    public void clear() {
        this.bufpos = 0;
        this.buflen = 0;
    }
    
    public void ensureCapacity(final int n) {
        if (n > this.buffer.length) {
            this.expand(n);
        }
    }
    
    public int fillBuffer() throws IOException {
        final int bufpos = this.bufpos;
        if (bufpos > 0) {
            final int buflen = this.buflen - bufpos;
            if (buflen > 0) {
                final byte[] buffer = this.buffer;
                System.arraycopy(buffer, bufpos, buffer, 0, buflen);
            }
            this.bufpos = 0;
            this.buflen = buflen;
        }
        final int buflen2 = this.buflen;
        final int read = this.in.read(this.buffer, buflen2, this.buffer.length - buflen2);
        if (read == -1) {
            return -1;
        }
        this.buflen = buflen2 + read;
        return read;
    }
    
    public boolean hasBufferedData() {
        return this.bufpos < this.buflen;
    }
    
    public int indexOf(final byte b) {
        final int bufpos = this.bufpos;
        return this.indexOf(b, bufpos, this.buflen - bufpos);
    }
    
    public int indexOf(final byte b, int i, int n) {
        if (i >= this.bufpos && n >= 0) {
            n += i;
            if (n <= this.buflen) {
                while (i < n) {
                    if (this.buffer[i] == b) {
                        return i;
                    }
                    ++i;
                }
                return -1;
            }
        }
        throw new IndexOutOfBoundsException();
    }
    
    public int indexOf(final byte[] array) {
        final int bufpos = this.bufpos;
        return this.indexOf(array, bufpos, this.buflen - bufpos);
    }
    
    public int indexOf(final byte[] array, final int n, final int n2) {
        if (array == null) {
            throw new IllegalArgumentException("Pattern may not be null");
        }
        if (n < this.bufpos || n2 < 0 || n + n2 > this.buflen) {
            throw new IndexOutOfBoundsException();
        }
        if (n2 < array.length) {
            return -1;
        }
        final int[] array2 = new int[256];
        for (int i = 0; i < 256; ++i) {
            array2[i] = array.length + 1;
        }
        for (int j = 0; j < array.length; ++j) {
            array2[array[j] & 0xFF] = array.length - j;
        }
        int k = 0;
    Label_0108:
        while (k <= n2 - array.length) {
            final int n3 = n + k;
            int l = 0;
            while (true) {
                while (l < array.length) {
                    if (this.buffer[n3 + l] != array[l]) {
                        final boolean b = false;
                        if (b) {
                            return n3;
                        }
                        final int n4 = n3 + array.length;
                        final byte[] buffer = this.buffer;
                        if (n4 >= buffer.length) {
                            return -1;
                        }
                        k += array2[buffer[n4] & 0xFF];
                        continue Label_0108;
                    }
                    else {
                        ++l;
                    }
                }
                final boolean b = true;
                continue;
            }
        }
        return -1;
    }
    
    public int length() {
        return this.buflen - this.bufpos;
    }
    
    public int limit() {
        return this.buflen;
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    public int pos() {
        return this.bufpos;
    }
    
    @Override
    public int read() throws IOException {
        if (this.truncated) {
            return -1;
        }
        while (!this.hasBufferedData()) {
            if (this.fillBuffer() == -1) {
                return -1;
            }
        }
        return this.buffer[this.bufpos++] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        if (this.truncated) {
            return -1;
        }
        if (array == null) {
            return 0;
        }
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, final int n, int n2) throws IOException {
        if (this.truncated) {
            return -1;
        }
        if (array == null) {
            return 0;
        }
        while (!this.hasBufferedData()) {
            if (this.fillBuffer() == -1) {
                return -1;
            }
        }
        final int n3 = this.buflen - this.bufpos;
        if (n3 <= n2) {
            n2 = n3;
        }
        System.arraycopy(this.buffer, this.bufpos, array, n, n2);
        this.bufpos += n2;
        return n2;
    }
    
    @Override
    public int readLine(final ByteArrayBuffer byteArrayBuffer) throws IOException {
        if (byteArrayBuffer == null) {
            throw new IllegalArgumentException("Buffer may not be null");
        }
        if (this.truncated) {
            return -1;
        }
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4;
        while (true) {
            n4 = n;
            if (n2 != 0) {
                break;
            }
            int n5 = n;
            if (!this.hasBufferedData()) {
                final int fillBuffer = this.fillBuffer();
                if ((n5 = fillBuffer) == -1) {
                    n4 = fillBuffer;
                    break;
                }
            }
            final int index = this.indexOf((byte)10);
            int length;
            int n6;
            if (index != -1) {
                length = index + 1 - this.pos();
                n6 = 1;
            }
            else {
                length = this.length();
                n6 = n2;
            }
            int n7 = n3;
            if (length > 0) {
                byteArrayBuffer.append(this.buf(), this.pos(), length);
                this.skip(length);
                n7 = n3 + length;
            }
            n = n5;
            n2 = n6;
            n3 = n7;
            if (this.maxLineLen <= 0) {
                continue;
            }
            if (byteArrayBuffer.length() >= this.maxLineLen) {
                throw new MaxLineLimitException("Maximum line length limit exceeded");
            }
            n = n5;
            n2 = n6;
            n3 = n7;
        }
        if (n3 == 0 && n4 == -1) {
            return -1;
        }
        return n3;
    }
    
    public int skip(int min) {
        min = Math.min(min, this.buflen - this.bufpos);
        this.bufpos += min;
        return min;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[pos: ");
        sb.append(this.bufpos);
        sb.append("]");
        sb.append("[limit: ");
        sb.append(this.buflen);
        sb.append("]");
        sb.append("[");
        for (int i = this.bufpos; i < this.buflen; ++i) {
            sb.append((char)this.buffer[i]);
        }
        sb.append("]");
        return sb.toString();
    }
    
    public void truncate() {
        this.clear();
        this.truncated = true;
    }
}
