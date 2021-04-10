package okhttp3.internal.http2;

import okio.*;
import java.io.*;
import java.util.logging.*;
import okhttp3.internal.*;
import java.util.*;

final class Http2Writer implements Closeable
{
    private static final Logger logger;
    private final boolean client;
    private boolean closed;
    private final Buffer hpackBuffer;
    final Hpack.Writer hpackWriter;
    private int maxFrameSize;
    private final BufferedSink sink;
    
    static {
        logger = Logger.getLogger(Http2.class.getName());
    }
    
    Http2Writer(final BufferedSink sink, final boolean client) {
        this.sink = sink;
        this.client = client;
        this.hpackBuffer = new Buffer();
        this.hpackWriter = new Hpack.Writer(this.hpackBuffer);
        this.maxFrameSize = 16384;
    }
    
    private void writeContinuationFrames(final int n, long n2) throws IOException {
        while (n2 > 0L) {
            final int n3 = (int)Math.min(this.maxFrameSize, n2);
            final long n4 = n3;
            n2 -= n4;
            byte b;
            if (n2 == 0L) {
                b = 4;
            }
            else {
                b = 0;
            }
            this.frameHeader(n, n3, (byte)9, b);
            this.sink.write(this.hpackBuffer, n4);
        }
    }
    
    private static void writeMedium(final BufferedSink bufferedSink, final int n) throws IOException {
        bufferedSink.writeByte(n >>> 16 & 0xFF);
        bufferedSink.writeByte(n >>> 8 & 0xFF);
        bufferedSink.writeByte(n & 0xFF);
    }
    
    public void applyAndAckSettings(final Settings settings) throws IOException {
        synchronized (this) {
            if (!this.closed) {
                this.maxFrameSize = settings.getMaxFrameSize(this.maxFrameSize);
                if (settings.getHeaderTableSize() != -1) {
                    this.hpackWriter.setHeaderTableSizeSetting(settings.getHeaderTableSize());
                }
                this.frameHeader(0, 0, (byte)4, (byte)1);
                this.sink.flush();
                return;
            }
            throw new IOException("closed");
        }
    }
    
    @Override
    public void close() throws IOException {
        synchronized (this) {
            this.closed = true;
            this.sink.close();
        }
    }
    
    public void connectionPreface() throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
            if (!this.client) {
                return;
            }
            if (Http2Writer.logger.isLoggable(Level.FINE)) {
                Http2Writer.logger.fine(Util.format(">> CONNECTION %s", Http2.CONNECTION_PREFACE.hex()));
            }
            this.sink.write(Http2.CONNECTION_PREFACE.toByteArray());
            this.sink.flush();
        }
    }
    
    public void data(final boolean b, final int n, final Buffer buffer, final int n2) throws IOException {
        synchronized (this) {
            if (!this.closed) {
                byte b2 = 0;
                if (b) {
                    b2 = 1;
                }
                this.dataFrame(n, b2, buffer, n2);
                return;
            }
            throw new IOException("closed");
        }
    }
    
    void dataFrame(final int n, final byte b, final Buffer buffer, final int n2) throws IOException {
        this.frameHeader(n, n2, (byte)0, b);
        if (n2 > 0) {
            this.sink.write(buffer, n2);
        }
    }
    
    public void flush() throws IOException {
        synchronized (this) {
            if (!this.closed) {
                this.sink.flush();
                return;
            }
            throw new IOException("closed");
        }
    }
    
    public void frameHeader(final int n, final int n2, final byte b, final byte b2) throws IOException {
        if (Http2Writer.logger.isLoggable(Level.FINE)) {
            Http2Writer.logger.fine(Http2.frameLog(false, n, n2, b, b2));
        }
        final int maxFrameSize = this.maxFrameSize;
        if (n2 > maxFrameSize) {
            throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", maxFrameSize, n2);
        }
        if ((Integer.MIN_VALUE & n) == 0x0) {
            writeMedium(this.sink, n2);
            this.sink.writeByte(b & 0xFF);
            this.sink.writeByte(b2 & 0xFF);
            this.sink.writeInt(n & Integer.MAX_VALUE);
            return;
        }
        throw Http2.illegalArgument("reserved bit set: %s", n);
    }
    
    public void goAway(final int n, final ErrorCode errorCode, final byte[] array) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
            if (errorCode.httpCode != -1) {
                this.frameHeader(0, array.length + 8, (byte)7, (byte)0);
                this.sink.writeInt(n);
                this.sink.writeInt(errorCode.httpCode);
                if (array.length > 0) {
                    this.sink.write(array);
                }
                this.sink.flush();
                return;
            }
            throw Http2.illegalArgument("errorCode.httpCode == -1", new Object[0]);
        }
    }
    
    void headers(final boolean b, final int n, final List<Header> list) throws IOException {
        if (!this.closed) {
            this.hpackWriter.writeHeaders(list);
            final long size = this.hpackBuffer.size();
            final int n2 = (int)Math.min(this.maxFrameSize, size);
            final long n3 = n2;
            byte b2;
            if (size == n3) {
                b2 = 4;
            }
            else {
                b2 = 0;
            }
            byte b3 = b2;
            if (b) {
                b3 = (byte)(b2 | 0x1);
            }
            this.frameHeader(n, n2, (byte)1, b3);
            this.sink.write(this.hpackBuffer, n3);
            if (size > n3) {
                this.writeContinuationFrames(n, size - n3);
            }
            return;
        }
        throw new IOException("closed");
    }
    
    public int maxDataLength() {
        return this.maxFrameSize;
    }
    
    public void ping(final boolean b, final int n, final int n2) throws IOException {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public void pushPromise(final int n, final int n2, final List<Header> list) throws IOException {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (this.closed) {
                        throw new IOException("closed");
                    }
                    this.hpackWriter.writeHeaders(list);
                    final long size = this.hpackBuffer.size();
                    final int n3 = (int)Math.min(this.maxFrameSize - 4, size);
                    final long n4 = n3;
                    if (size == n4) {
                        final byte b = 4;
                        this.frameHeader(n, n3 + 4, (byte)5, b);
                        this.sink.writeInt(n2 & Integer.MAX_VALUE);
                        this.sink.write(this.hpackBuffer, n4);
                        if (size > n4) {
                            this.writeContinuationFrames(n, size - n4);
                        }
                        return;
                    }
                }
                final byte b = 0;
                continue;
            }
        }
    }
    
    public void rstStream(final int n, final ErrorCode errorCode) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
            if (errorCode.httpCode != -1) {
                this.frameHeader(n, 4, (byte)3, (byte)0);
                this.sink.writeInt(errorCode.httpCode);
                this.sink.flush();
                return;
            }
            throw new IllegalArgumentException();
        }
    }
    
    public void settings(final Settings settings) throws IOException {
        while (true) {
            while (true) {
                int n = 0;
                Label_0126: {
                    while (true) {
                        Label_0100: {
                            synchronized (this) {
                                if (this.closed) {
                                    throw new IOException("closed");
                                }
                                final int size = settings.size();
                                n = 0;
                                this.frameHeader(0, size * 6, (byte)4, (byte)0);
                                if (n >= 10) {
                                    this.sink.flush();
                                    return;
                                }
                                if (!settings.isSet(n)) {
                                    break Label_0126;
                                }
                                break Label_0100;
                                final int n2;
                                this.sink.writeShort(n2);
                                this.sink.writeInt(settings.get(n));
                                break Label_0126;
                            }
                        }
                        if (n == 4) {
                            final int n2 = 3;
                            continue;
                        }
                        if (n == 7) {
                            final int n2 = 4;
                            continue;
                        }
                        final int n2 = n;
                        continue;
                    }
                }
                ++n;
                continue;
            }
        }
    }
    
    public void synStream(final boolean b, final int n, final int n2, final List<Header> list) throws IOException {
        synchronized (this) {
            if (!this.closed) {
                this.headers(b, n, list);
                return;
            }
            throw new IOException("closed");
        }
    }
    
    public void windowUpdate(final int n, final long n2) throws IOException {
        synchronized (this) {
            if (this.closed) {
                throw new IOException("closed");
            }
            if (n2 != 0L && n2 <= 2147483647L) {
                this.frameHeader(n, 4, (byte)8, (byte)0);
                this.sink.writeInt((int)n2);
                this.sink.flush();
                return;
            }
            throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", n2);
        }
    }
}
