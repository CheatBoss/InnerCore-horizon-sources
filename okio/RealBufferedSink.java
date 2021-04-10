package okio;

import java.io.*;
import java.nio.*;

final class RealBufferedSink implements BufferedSink
{
    public final Buffer buffer;
    boolean closed;
    public final Sink sink;
    
    RealBufferedSink(final Sink sink) {
        this.buffer = new Buffer();
        if (sink != null) {
            this.sink = sink;
            return;
        }
        throw new NullPointerException("sink == null");
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
        try {
            if (this.buffer.size > 0L) {
                this.sink.write(this.buffer, this.buffer.size);
            }
        }
        finally {}
        Throwable t = null;
        try {
            this.sink.close();
        }
        finally {
            final Throwable t2;
            t = t2;
            if (t2 == null) {
                final Throwable t3;
                t = t3;
            }
        }
        this.closed = true;
        if (t != null) {
            Util.sneakyRethrow(t);
        }
    }
    
    @Override
    public BufferedSink emit() throws IOException {
        if (!this.closed) {
            final long size = this.buffer.size();
            if (size > 0L) {
                this.sink.write(this.buffer, size);
            }
            return this;
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink emitCompleteSegments() throws IOException {
        if (!this.closed) {
            final long completeSegmentByteCount = this.buffer.completeSegmentByteCount();
            if (completeSegmentByteCount > 0L) {
                this.sink.write(this.buffer, completeSegmentByteCount);
            }
            return this;
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public void flush() throws IOException {
        if (!this.closed) {
            if (this.buffer.size > 0L) {
                final Sink sink = this.sink;
                final Buffer buffer = this.buffer;
                sink.write(buffer, buffer.size);
            }
            this.sink.flush();
            return;
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public boolean isOpen() {
        return this.closed ^ true;
    }
    
    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("buffer(");
        sb.append(this.sink);
        sb.append(")");
        return sb.toString();
    }
    
    @Override
    public int write(final ByteBuffer byteBuffer) throws IOException {
        if (!this.closed) {
            final int write = this.buffer.write(byteBuffer);
            this.emitCompleteSegments();
            return write;
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink write(final ByteString byteString) throws IOException {
        if (!this.closed) {
            this.buffer.write(byteString);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink write(final byte[] array) throws IOException {
        if (!this.closed) {
            this.buffer.write(array);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink write(final byte[] array, final int n, final int n2) throws IOException {
        if (!this.closed) {
            this.buffer.write(array, n, n2);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public void write(final Buffer buffer, final long n) throws IOException {
        if (!this.closed) {
            this.buffer.write(buffer, n);
            this.emitCompleteSegments();
            return;
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public long writeAll(final Source source) throws IOException {
        if (source != null) {
            long n = 0L;
            while (true) {
                final long read = source.read(this.buffer, 8192L);
                if (read == -1L) {
                    break;
                }
                this.emitCompleteSegments();
                n += read;
            }
            return n;
        }
        throw new IllegalArgumentException("source == null");
    }
    
    @Override
    public BufferedSink writeByte(final int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeByte(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink writeDecimalLong(final long n) throws IOException {
        if (!this.closed) {
            this.buffer.writeDecimalLong(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink writeHexadecimalUnsignedLong(final long n) throws IOException {
        if (!this.closed) {
            this.buffer.writeHexadecimalUnsignedLong(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink writeInt(final int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeInt(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink writeShort(final int n) throws IOException {
        if (!this.closed) {
            this.buffer.writeShort(n);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
    
    @Override
    public BufferedSink writeUtf8(final String s) throws IOException {
        if (!this.closed) {
            this.buffer.writeUtf8(s);
            return this.emitCompleteSegments();
        }
        throw new IllegalStateException("closed");
    }
}
