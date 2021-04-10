package okhttp3.internal.ws;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import okio.*;

final class WebSocketReader
{
    boolean closed;
    private final Buffer controlFrameBuffer;
    final FrameCallback frameCallback;
    long frameLength;
    final boolean isClient;
    boolean isControlFrame;
    boolean isFinalFrame;
    private final Buffer.UnsafeCursor maskCursor;
    private final byte[] maskKey;
    private final Buffer messageFrameBuffer;
    int opcode;
    final BufferedSource source;
    
    WebSocketReader(final boolean isClient, final BufferedSource source, final FrameCallback frameCallback) {
        this.controlFrameBuffer = new Buffer();
        this.messageFrameBuffer = new Buffer();
        if (source == null) {
            throw new NullPointerException("source == null");
        }
        if (frameCallback != null) {
            this.isClient = isClient;
            this.source = source;
            this.frameCallback = frameCallback;
            final Buffer.UnsafeCursor unsafeCursor = null;
            byte[] maskKey;
            if (isClient) {
                maskKey = null;
            }
            else {
                maskKey = new byte[4];
            }
            this.maskKey = maskKey;
            Buffer.UnsafeCursor maskCursor;
            if (isClient) {
                maskCursor = unsafeCursor;
            }
            else {
                maskCursor = new Buffer.UnsafeCursor();
            }
            this.maskCursor = maskCursor;
            return;
        }
        throw new NullPointerException("frameCallback == null");
    }
    
    private void readControlFrame() throws IOException {
        final long frameLength = this.frameLength;
        if (frameLength > 0L) {
            this.source.readFully(this.controlFrameBuffer, frameLength);
            if (!this.isClient) {
                this.controlFrameBuffer.readAndWriteUnsafe(this.maskCursor);
                this.maskCursor.seek(0L);
                WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
                this.maskCursor.close();
            }
        }
        switch (this.opcode) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown control opcode: ");
                sb.append(Integer.toHexString(this.opcode));
                throw new ProtocolException(sb.toString());
            }
            case 10: {
                this.frameCallback.onReadPong(this.controlFrameBuffer.readByteString());
            }
            case 9: {
                this.frameCallback.onReadPing(this.controlFrameBuffer.readByteString());
            }
            case 8: {
                int short1 = 1005;
                final long size = this.controlFrameBuffer.size();
                if (size != 1L) {
                    String utf8;
                    if (size != 0L) {
                        short1 = this.controlFrameBuffer.readShort();
                        utf8 = this.controlFrameBuffer.readUtf8();
                        final String closeCodeExceptionMessage = WebSocketProtocol.closeCodeExceptionMessage(short1);
                        if (closeCodeExceptionMessage != null) {
                            throw new ProtocolException(closeCodeExceptionMessage);
                        }
                    }
                    else {
                        utf8 = "";
                    }
                    this.frameCallback.onReadClose(short1, utf8);
                    this.closed = true;
                    return;
                }
                throw new ProtocolException("Malformed close payload length of 1.");
            }
        }
    }
    
    private void readHeader() throws IOException {
        if (!this.closed) {
            long n = this.source.timeout().timeoutNanos();
            this.source.timeout().clearTimeout();
            try {
                final int n2 = this.source.readByte() & 0xFF;
                this.source.timeout().timeout(n, TimeUnit.NANOSECONDS);
                this.opcode = (n2 & 0xF);
                final boolean b = false;
                this.isFinalFrame = ((n2 & 0x80) != 0x0);
                final boolean isControlFrame = (n2 & 0x8) != 0x0;
                this.isControlFrame = isControlFrame;
                if (isControlFrame && !this.isFinalFrame) {
                    throw new ProtocolException("Control frames must be final.");
                }
                final boolean b2 = (n2 & 0x40) != 0x0;
                final boolean b3 = (n2 & 0x20) != 0x0;
                final boolean b4 = (n2 & 0x10) != 0x0;
                if (b2 || b3 || b4) {
                    throw new ProtocolException("Reserved flags are unsupported.");
                }
                final int n3 = this.source.readByte() & 0xFF;
                boolean b5 = b;
                if ((n3 & 0x80) != 0x0) {
                    b5 = true;
                }
                if (b5 == this.isClient) {
                    String s;
                    if (this.isClient) {
                        s = "Server-sent frames must not be masked.";
                    }
                    else {
                        s = "Client-sent frames must be masked.";
                    }
                    throw new ProtocolException(s);
                }
                n = (n3 & 0x7F);
                this.frameLength = n;
                if (n == 126L) {
                    this.frameLength = ((long)this.source.readShort() & 0xFFFFL);
                }
                else if (n == 127L) {
                    n = this.source.readLong();
                    this.frameLength = n;
                    if (n < 0L) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Frame length 0x");
                        sb.append(Long.toHexString(this.frameLength));
                        sb.append(" > 0x7FFFFFFFFFFFFFFF");
                        throw new ProtocolException(sb.toString());
                    }
                }
                if (this.isControlFrame && this.frameLength > 125L) {
                    throw new ProtocolException("Control frame must be less than 125B.");
                }
                if (b5) {
                    this.source.readFully(this.maskKey);
                }
                return;
            }
            finally {
                this.source.timeout().timeout(n, TimeUnit.NANOSECONDS);
            }
        }
        throw new IOException("closed");
    }
    
    private void readMessage() throws IOException {
        while (!this.closed) {
            final long frameLength = this.frameLength;
            if (frameLength > 0L) {
                this.source.readFully(this.messageFrameBuffer, frameLength);
                if (!this.isClient) {
                    this.messageFrameBuffer.readAndWriteUnsafe(this.maskCursor);
                    this.maskCursor.seek(this.messageFrameBuffer.size() - this.frameLength);
                    WebSocketProtocol.toggleMask(this.maskCursor, this.maskKey);
                    this.maskCursor.close();
                }
            }
            if (this.isFinalFrame) {
                return;
            }
            this.readUntilNonControlFrame();
            if (this.opcode == 0) {
                continue;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected continuation opcode. Got: ");
            sb.append(Integer.toHexString(this.opcode));
            throw new ProtocolException(sb.toString());
        }
        throw new IOException("closed");
    }
    
    private void readMessageFrame() throws IOException {
        final int opcode = this.opcode;
        if (opcode != 1 && opcode != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown opcode: ");
            sb.append(Integer.toHexString(opcode));
            throw new ProtocolException(sb.toString());
        }
        this.readMessage();
        if (opcode == 1) {
            this.frameCallback.onReadMessage(this.messageFrameBuffer.readUtf8());
            return;
        }
        this.frameCallback.onReadMessage(this.messageFrameBuffer.readByteString());
    }
    
    private void readUntilNonControlFrame() throws IOException {
        while (!this.closed) {
            this.readHeader();
            if (!this.isControlFrame) {
                return;
            }
            this.readControlFrame();
        }
    }
    
    void processNextFrame() throws IOException {
        this.readHeader();
        if (this.isControlFrame) {
            this.readControlFrame();
            return;
        }
        this.readMessageFrame();
    }
    
    public interface FrameCallback
    {
        void onReadClose(final int p0, final String p1);
        
        void onReadMessage(final String p0) throws IOException;
        
        void onReadMessage(final ByteString p0) throws IOException;
        
        void onReadPing(final ByteString p0);
        
        void onReadPong(final ByteString p0);
    }
}
