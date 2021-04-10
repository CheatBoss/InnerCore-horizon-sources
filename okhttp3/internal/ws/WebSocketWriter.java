package okhttp3.internal.ws;

import java.util.*;
import java.io.*;
import okio.*;

final class WebSocketWriter
{
    boolean activeWriter;
    final Buffer buffer;
    final FrameSink frameSink;
    final boolean isClient;
    private final Buffer.UnsafeCursor maskCursor;
    private final byte[] maskKey;
    final Random random;
    final BufferedSink sink;
    final Buffer sinkBuffer;
    boolean writerClosed;
    
    WebSocketWriter(final boolean isClient, final BufferedSink sink, final Random random) {
        this.buffer = new Buffer();
        this.frameSink = new FrameSink();
        if (sink == null) {
            throw new NullPointerException("sink == null");
        }
        if (random != null) {
            this.isClient = isClient;
            this.sink = sink;
            this.sinkBuffer = sink.buffer();
            this.random = random;
            final Buffer.UnsafeCursor unsafeCursor = null;
            byte[] maskKey;
            if (isClient) {
                maskKey = new byte[4];
            }
            else {
                maskKey = null;
            }
            this.maskKey = maskKey;
            Buffer.UnsafeCursor maskCursor = unsafeCursor;
            if (isClient) {
                maskCursor = new Buffer.UnsafeCursor();
            }
            this.maskCursor = maskCursor;
            return;
        }
        throw new NullPointerException("random == null");
    }
    
    private void writeControlFrame(final int n, final ByteString byteString) throws IOException {
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        final int size = byteString.size();
        if (size <= 125L) {
            this.sinkBuffer.writeByte(n | 0x80);
            if (this.isClient) {
                this.sinkBuffer.writeByte(size | 0x80);
                this.random.nextBytes(this.maskKey);
                this.sinkBuffer.write(this.maskKey);
                if (size > 0) {
                    final long size2 = this.sinkBuffer.size();
                    this.sinkBuffer.write(byteString);
                    this.sinkBuffer.readAndWriteUnsafe(this.maskCursor);
                    this.maskCursor.seek(size2);
                    WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
                    this.maskCursor.close();
                }
            }
            else {
                this.sinkBuffer.writeByte(size);
                this.sinkBuffer.write(byteString);
            }
            this.sink.flush();
            return;
        }
        throw new IllegalArgumentException("Payload size must be less than or equal to 125");
    }
    
    Sink newMessageSink(final int formatOpcode, final long contentLength) {
        if (!this.activeWriter) {
            this.activeWriter = true;
            this.frameSink.formatOpcode = formatOpcode;
            this.frameSink.contentLength = contentLength;
            this.frameSink.isFirstFrame = true;
            this.frameSink.closed = false;
            return this.frameSink;
        }
        throw new IllegalStateException("Another message writer is active. Did you call close()?");
    }
    
    void writeClose(final int n, final ByteString byteString) throws IOException {
        ByteString byteString2 = ByteString.EMPTY;
        if (n != 0 || byteString != null) {
            if (n != 0) {
                WebSocketProtocol.validateCloseCode(n);
            }
            final Buffer buffer = new Buffer();
            buffer.writeShort(n);
            if (byteString != null) {
                buffer.write(byteString);
            }
            byteString2 = buffer.readByteString();
        }
        try {
            this.writeControlFrame(8, byteString2);
        }
        finally {
            this.writerClosed = true;
        }
    }
    
    void writeMessageFrame(int n, final long n2, final boolean b, final boolean b2) throws IOException {
        if (!this.writerClosed) {
            final int n3 = 0;
            if (!b) {
                n = 0;
            }
            int n4 = n;
            if (b2) {
                n4 = (n | 0x80);
            }
            this.sinkBuffer.writeByte(n4);
            n = n3;
            if (this.isClient) {
                n = 128;
            }
            if (n2 <= 125L) {
                this.sinkBuffer.writeByte((int)n2 | n);
            }
            else if (n2 <= 65535L) {
                this.sinkBuffer.writeByte(n | 0x7E);
                this.sinkBuffer.writeShort((int)n2);
            }
            else {
                this.sinkBuffer.writeByte(n | 0x7F);
                this.sinkBuffer.writeLong(n2);
            }
            if (this.isClient) {
                this.random.nextBytes(this.maskKey);
                this.sinkBuffer.write(this.maskKey);
                if (n2 > 0L) {
                    final long size = this.sinkBuffer.size();
                    this.sinkBuffer.write(this.buffer, n2);
                    this.sinkBuffer.readAndWriteUnsafe(this.maskCursor);
                    this.maskCursor.seek(size);
                    WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
                    this.maskCursor.close();
                }
            }
            else {
                this.sinkBuffer.write(this.buffer, n2);
            }
            this.sink.emit();
            return;
        }
        throw new IOException("closed");
    }
    
    void writePing(final ByteString byteString) throws IOException {
        this.writeControlFrame(9, byteString);
    }
    
    void writePong(final ByteString byteString) throws IOException {
        this.writeControlFrame(10, byteString);
    }
    
    final class FrameSink implements Sink
    {
        boolean closed;
        long contentLength;
        int formatOpcode;
        boolean isFirstFrame;
        
        @Override
        public void close() throws IOException {
            if (!this.closed) {
                final WebSocketWriter this$0 = WebSocketWriter.this;
                this$0.writeMessageFrame(this.formatOpcode, this$0.buffer.size(), this.isFirstFrame, true);
                this.closed = true;
                WebSocketWriter.this.activeWriter = false;
                return;
            }
            throw new IOException("closed");
        }
        
        @Override
        public void flush() throws IOException {
            if (!this.closed) {
                final WebSocketWriter this$0 = WebSocketWriter.this;
                this$0.writeMessageFrame(this.formatOpcode, this$0.buffer.size(), this.isFirstFrame, false);
                this.isFirstFrame = false;
                return;
            }
            throw new IOException("closed");
        }
        
        @Override
        public Timeout timeout() {
            return WebSocketWriter.this.sink.timeout();
        }
        
        @Override
        public void write(final Buffer buffer, long completeSegmentByteCount) throws IOException {
            if (!this.closed) {
                WebSocketWriter.this.buffer.write(buffer, completeSegmentByteCount);
                final boolean b = this.isFirstFrame && this.contentLength != -1L && WebSocketWriter.this.buffer.size() > this.contentLength - 8192L;
                completeSegmentByteCount = WebSocketWriter.this.buffer.completeSegmentByteCount();
                if (completeSegmentByteCount > 0L && !b) {
                    WebSocketWriter.this.writeMessageFrame(this.formatOpcode, completeSegmentByteCount, this.isFirstFrame, false);
                    this.isFirstFrame = false;
                }
                return;
            }
            throw new IOException("closed");
        }
    }
}
