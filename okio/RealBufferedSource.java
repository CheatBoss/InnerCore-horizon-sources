package okio;

import java.nio.*;
import java.io.*;
import java.nio.charset.*;

final class RealBufferedSource implements BufferedSource
{
    public final Buffer buffer;
    boolean closed;
    public final Source source;
    
    RealBufferedSource(final Source source) {
        this.buffer = new Buffer();
        if (source != null) {
            this.source = source;
            return;
        }
        throw new NullPointerException("source == null");
    }
    
    @Override
    public Buffer buffer() {
        return this.buffer;
    }
    
    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.source.close();
        this.buffer.clear();
    }
    
    @Override
    public boolean exhausted() throws IOException {
        if (!this.closed) {
            return this.buffer.exhausted() && this.source.read(this.buffer, 8192L) == -1L;
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public long indexOf(final byte b) throws IOException {
        return this.indexOf(b, 0L, Long.MAX_VALUE);
    }
    
    public long indexOf(final byte b, long max, final long n) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (max >= 0L && n >= max) {
            while (max < n) {
                final long index = this.buffer.indexOf(b, max, n);
                if (index != -1L) {
                    return index;
                }
                final long size = this.buffer.size;
                if (size >= n) {
                    break;
                }
                if (this.source.read(this.buffer, 8192L) == -1L) {
                    return -1L;
                }
                max = Math.max(max, size);
            }
            return -1L;
        }
        throw new IllegalArgumentException(String.format("fromIndex=%s toIndex=%s", max, n));
    }
    
    @Override
    public InputStream inputStream() {
        return new InputStream() {
            @Override
            public int available() throws IOException {
                if (!RealBufferedSource.this.closed) {
                    return (int)Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
                }
                throw new IOException("closed");
            }
            
            @Override
            public void close() throws IOException {
                RealBufferedSource.this.close();
            }
            
            @Override
            public int read() throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                if (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L) == -1L) {
                    return -1;
                }
                return RealBufferedSource.this.buffer.readByte() & 0xFF;
            }
            
            @Override
            public int read(final byte[] array, final int n, final int n2) throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                Util.checkOffsetAndCount(array.length, n, n2);
                if (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L) == -1L) {
                    return -1;
                }
                return RealBufferedSource.this.buffer.read(array, n, n2);
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append(RealBufferedSource.this);
                sb.append(".inputStream()");
                return sb.toString();
            }
        };
    }
    
    @Override
    public boolean isOpen() {
        return this.closed ^ true;
    }
    
    @Override
    public boolean rangeEquals(final long n, final ByteString byteString) throws IOException {
        return this.rangeEquals(n, byteString, 0, byteString.size());
    }
    
    public boolean rangeEquals(final long n, final ByteString byteString, final int n2, final int n3) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (n < 0L || n2 < 0 || n3 < 0) {
            return false;
        }
        if (byteString.size() - n2 < n3) {
            return false;
        }
        for (int i = 0; i < n3; ++i) {
            final long n4 = i + n;
            if (!this.request(n4 + 1L)) {
                return false;
            }
            if (this.buffer.getByte(n4) != byteString.getByte(n2 + i)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int read(final ByteBuffer byteBuffer) throws IOException {
        if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
            return -1;
        }
        return this.buffer.read(byteBuffer);
    }
    
    @Override
    public long read(final Buffer buffer, long min) throws IOException {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (min < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(min);
            throw new IllegalArgumentException(sb.toString());
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
            return -1L;
        }
        min = Math.min(min, this.buffer.size);
        return this.buffer.read(buffer, min);
    }
    
    @Override
    public byte readByte() throws IOException {
        this.require(1L);
        return this.buffer.readByte();
    }
    
    @Override
    public byte[] readByteArray() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }
    
    @Override
    public byte[] readByteArray(final long n) throws IOException {
        this.require(n);
        return this.buffer.readByteArray(n);
    }
    
    @Override
    public ByteString readByteString(final long n) throws IOException {
        this.require(n);
        return this.buffer.readByteString(n);
    }
    
    @Override
    public void readFully(final Buffer buffer, final long n) throws IOException {
        try {
            this.require(n);
            this.buffer.readFully(buffer, n);
        }
        catch (EOFException ex) {
            buffer.writeAll(this.buffer);
            throw ex;
        }
    }
    
    @Override
    public void readFully(final byte[] array) throws IOException {
        try {
            this.require(array.length);
            this.buffer.readFully(array);
        }
        catch (EOFException ex) {
            int n = 0;
            while (this.buffer.size > 0L) {
                final Buffer buffer = this.buffer;
                final int read = buffer.read(array, n, (int)buffer.size);
                if (read == -1) {
                    throw new AssertionError();
                }
                n += read;
            }
            throw ex;
        }
    }
    
    @Override
    public long readHexadecimalUnsignedLong() throws IOException {
        this.require(1L);
        int n = 0;
        while (true) {
            final int n2 = n + 1;
            if (!this.request(n2)) {
                break;
            }
            final byte byte1 = this.buffer.getByte(n);
            if ((byte1 < 48 || byte1 > 57) && (byte1 < 97 || byte1 > 102) && (byte1 < 65 || byte1 > 70)) {
                if (n != 0) {
                    break;
                }
                throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", byte1));
            }
            else {
                n = n2;
            }
        }
        return this.buffer.readHexadecimalUnsignedLong();
    }
    
    @Override
    public int readInt() throws IOException {
        this.require(4L);
        return this.buffer.readInt();
    }
    
    @Override
    public int readIntLe() throws IOException {
        this.require(4L);
        return this.buffer.readIntLe();
    }
    
    @Override
    public long readLong() throws IOException {
        this.require(8L);
        return this.buffer.readLong();
    }
    
    @Override
    public short readShort() throws IOException {
        this.require(2L);
        return this.buffer.readShort();
    }
    
    @Override
    public short readShortLe() throws IOException {
        this.require(2L);
        return this.buffer.readShortLe();
    }
    
    @Override
    public String readString(final Charset charset) throws IOException {
        if (charset != null) {
            this.buffer.writeAll(this.source);
            return this.buffer.readString(charset);
        }
        throw new IllegalArgumentException("charset == null");
    }
    
    @Override
    public String readUtf8LineStrict() throws IOException {
        return this.readUtf8LineStrict(Long.MAX_VALUE);
    }
    
    @Override
    public String readUtf8LineStrict(final long n) throws IOException {
        if (n < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("limit < 0: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        long n2;
        if (n == Long.MAX_VALUE) {
            n2 = Long.MAX_VALUE;
        }
        else {
            n2 = n + 1L;
        }
        final long index = this.indexOf((byte)10, 0L, n2);
        if (index != -1L) {
            return this.buffer.readUtf8Line(index);
        }
        if (n2 < Long.MAX_VALUE && this.request(n2) && this.buffer.getByte(n2 - 1L) == 13 && this.request(n2 + 1L) && this.buffer.getByte(n2) == 10) {
            return this.buffer.readUtf8Line(n2);
        }
        final Buffer buffer = new Buffer();
        final Buffer buffer2 = this.buffer;
        buffer2.copyTo(buffer, 0L, Math.min(32L, buffer2.size()));
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("\\n not found: limit=");
        sb2.append(Math.min(this.buffer.size(), n));
        sb2.append(" content=");
        sb2.append(buffer.readByteString().hex());
        sb2.append('\u2026');
        throw new EOFException(sb2.toString());
    }
    
    @Override
    public boolean request(final long n) throws IOException {
        if (n < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        if (!this.closed) {
            while (this.buffer.size < n) {
                if (this.source.read(this.buffer, 8192L) == -1L) {
                    return false;
                }
            }
            return true;
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public void require(final long n) throws IOException {
        if (this.request(n)) {
            return;
        }
        throw new EOFException();
    }
    
    @Override
    public void skip(long n) throws IOException {
        if (!this.closed) {
            while (n > 0L) {
                if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
                    throw new EOFException();
                }
                final long min = Math.min(n, this.buffer.size());
                this.buffer.skip(min);
                n -= min;
            }
            return;
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("buffer(");
        sb.append(this.source);
        sb.append(")");
        return sb.toString();
    }
}
