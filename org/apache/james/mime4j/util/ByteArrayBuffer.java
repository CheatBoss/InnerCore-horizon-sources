package org.apache.james.mime4j.util;

public final class ByteArrayBuffer implements ByteSequence
{
    private byte[] buffer;
    private int len;
    
    public ByteArrayBuffer(final int n) {
        if (n >= 0) {
            this.buffer = new byte[n];
            return;
        }
        throw new IllegalArgumentException("Buffer capacity may not be negative");
    }
    
    public ByteArrayBuffer(final byte[] buffer, final int len, final boolean b) {
        if (buffer == null) {
            throw new IllegalArgumentException();
        }
        if (len >= 0 && len <= buffer.length) {
            if (b) {
                this.buffer = buffer;
            }
            else {
                System.arraycopy(buffer, 0, this.buffer = new byte[len], 0, len);
            }
            this.len = len;
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public ByteArrayBuffer(final byte[] array, final boolean b) {
        this(array, array.length, b);
    }
    
    private void expand(final int n) {
        final byte[] buffer = new byte[Math.max(this.buffer.length << 1, n)];
        System.arraycopy(this.buffer, 0, buffer, 0, this.len);
        this.buffer = buffer;
    }
    
    public void append(final int n) {
        final int len = this.len + 1;
        if (len > this.buffer.length) {
            this.expand(len);
        }
        this.buffer[this.len] = (byte)n;
        this.len = len;
    }
    
    public void append(final byte[] array, final int n, final int n2) {
        if (array == null) {
            return;
        }
        if (n >= 0 && n <= array.length && n2 >= 0) {
            final int n3 = n + n2;
            if (n3 >= 0 && n3 <= array.length) {
                if (n2 == 0) {
                    return;
                }
                final int len = this.len + n2;
                if (len > this.buffer.length) {
                    this.expand(len);
                }
                System.arraycopy(array, n, this.buffer, this.len, n2);
                this.len = len;
                return;
            }
        }
        throw new IndexOutOfBoundsException();
    }
    
    public byte[] buffer() {
        return this.buffer;
    }
    
    @Override
    public byte byteAt(final int n) {
        if (n >= 0 && n < this.len) {
            return this.buffer[n];
        }
        throw new IndexOutOfBoundsException();
    }
    
    public int capacity() {
        return this.buffer.length;
    }
    
    public void clear() {
        this.len = 0;
    }
    
    public int indexOf(final byte b) {
        return this.indexOf(b, 0, this.len);
    }
    
    public int indexOf(final byte b, int n, int i) {
        int n2 = n;
        if (n < 0) {
            n2 = 0;
        }
        final int len = this.len;
        if ((n = i) > len) {
            n = len;
        }
        if ((i = n2) > n) {
            return -1;
        }
        while (i < n) {
            if (this.buffer[i] == b) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public boolean isEmpty() {
        return this.len == 0;
    }
    
    public boolean isFull() {
        return this.len == this.buffer.length;
    }
    
    @Override
    public int length() {
        return this.len;
    }
    
    public void setLength(final int len) {
        if (len >= 0 && len <= this.buffer.length) {
            this.len = len;
            return;
        }
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public byte[] toByteArray() {
        final int len = this.len;
        final byte[] array = new byte[len];
        if (len > 0) {
            System.arraycopy(this.buffer, 0, array, 0, len);
        }
        return array;
    }
    
    @Override
    public String toString() {
        return new String(this.toByteArray());
    }
}
