package com.android.dx.util;

import java.io.*;

public final class ByteArray
{
    private final byte[] bytes;
    private final int size;
    private final int start;
    
    public ByteArray(final byte[] array) {
        this(array, 0, array.length);
    }
    
    public ByteArray(final byte[] bytes, final int start, final int n) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        if (start < 0) {
            throw new IllegalArgumentException("start < 0");
        }
        if (n < start) {
            throw new IllegalArgumentException("end < start");
        }
        if (n > bytes.length) {
            throw new IllegalArgumentException("end > bytes.length");
        }
        this.bytes = bytes;
        this.start = start;
        this.size = n - start;
    }
    
    private void checkOffsets(final int n, final int n2) {
        if (n >= 0 && n2 >= n && n2 <= this.size) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("bad range: ");
        sb.append(n);
        sb.append("..");
        sb.append(n2);
        sb.append("; actual size ");
        sb.append(this.size);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private int getByte0(final int n) {
        return this.bytes[this.start + n];
    }
    
    private int getUnsignedByte0(final int n) {
        return this.bytes[this.start + n] & 0xFF;
    }
    
    public int getByte(final int n) {
        this.checkOffsets(n, n + 1);
        return this.getByte0(n);
    }
    
    public void getBytes(final byte[] array, final int n) {
        if (array.length - n < this.size) {
            throw new IndexOutOfBoundsException("(out.length - offset) < size()");
        }
        System.arraycopy(this.bytes, this.start, array, n, this.size);
    }
    
    public int getInt(final int n) {
        this.checkOffsets(n, n + 4);
        return this.getByte0(n) << 24 | this.getUnsignedByte0(n + 1) << 16 | this.getUnsignedByte0(n + 2) << 8 | this.getUnsignedByte0(n + 3);
    }
    
    public long getLong(final int n) {
        this.checkOffsets(n, n + 8);
        return ((long)(this.getByte0(n + 4) << 24 | this.getUnsignedByte0(n + 5) << 16 | this.getUnsignedByte0(n + 6) << 8 | this.getUnsignedByte0(n + 7)) & 0xFFFFFFFFL) | (long)(this.getByte0(n) << 24 | this.getUnsignedByte0(n + 1) << 16 | this.getUnsignedByte0(n + 2) << 8 | this.getUnsignedByte0(n + 3)) << 32;
    }
    
    public int getShort(final int n) {
        this.checkOffsets(n, n + 2);
        return this.getByte0(n) << 8 | this.getUnsignedByte0(n + 1);
    }
    
    public int getUnsignedByte(final int n) {
        this.checkOffsets(n, n + 1);
        return this.getUnsignedByte0(n);
    }
    
    public int getUnsignedShort(final int n) {
        this.checkOffsets(n, n + 2);
        return this.getUnsignedByte0(n) << 8 | this.getUnsignedByte0(n + 1);
    }
    
    public MyDataInputStream makeDataInputStream() {
        return new MyDataInputStream(this.makeInputStream());
    }
    
    public MyInputStream makeInputStream() {
        return new MyInputStream();
    }
    
    public int size() {
        return this.size;
    }
    
    public ByteArray slice(final int n, final int n2) {
        this.checkOffsets(n, n2);
        return new ByteArray(this.bytes, this.start + n, this.start + n2);
    }
    
    public int underlyingOffset(final int n, final byte[] array) {
        if (array != this.bytes) {
            throw new IllegalArgumentException("wrong bytes");
        }
        return this.start + n;
    }
    
    public interface GetCursor
    {
        int getCursor();
    }
    
    public static class MyDataInputStream extends DataInputStream
    {
        private final MyInputStream wrapped;
        
        public MyDataInputStream(final MyInputStream wrapped) {
            super(wrapped);
            this.wrapped = wrapped;
        }
    }
    
    public class MyInputStream extends InputStream
    {
        private int cursor;
        private int mark;
        
        public MyInputStream() {
            this.cursor = 0;
            this.mark = 0;
        }
        
        @Override
        public int available() {
            return ByteArray.this.size - this.cursor;
        }
        
        @Override
        public void mark(final int n) {
            this.mark = this.cursor;
        }
        
        @Override
        public boolean markSupported() {
            return true;
        }
        
        @Override
        public int read() throws IOException {
            if (this.cursor >= ByteArray.this.size) {
                return -1;
            }
            final int access$100 = ByteArray.this.getUnsignedByte0(this.cursor);
            ++this.cursor;
            return access$100;
        }
        
        @Override
        public int read(final byte[] array, final int n, int n2) {
            int n3 = n2;
            if (n + n2 > array.length) {
                n3 = array.length - n;
            }
            final int n4 = ByteArray.this.size - this.cursor;
            if ((n2 = n3) > n4) {
                n2 = n4;
            }
            System.arraycopy(ByteArray.this.bytes, this.cursor + ByteArray.this.start, array, n, n2);
            this.cursor += n2;
            return n2;
        }
        
        @Override
        public void reset() {
            this.cursor = this.mark;
        }
    }
}
