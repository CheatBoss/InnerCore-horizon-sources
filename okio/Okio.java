package okio;

import java.io.*;
import javax.annotation.*;
import java.net.*;
import java.util.logging.*;

public final class Okio
{
    static final Logger logger;
    
    static {
        logger = Logger.getLogger(Okio.class.getName());
    }
    
    private Okio() {
    }
    
    public static BufferedSink buffer(final Sink sink) {
        return new RealBufferedSink(sink);
    }
    
    public static BufferedSource buffer(final Source source) {
        return new RealBufferedSource(source);
    }
    
    static boolean isAndroidGetsocknameError(final AssertionError assertionError) {
        return assertionError.getCause() != null && assertionError.getMessage() != null && assertionError.getMessage().contains("getsockname failed");
    }
    
    private static Sink sink(final OutputStream outputStream, final Timeout timeout) {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        if (timeout != null) {
            return new Sink() {
                @Override
                public void close() throws IOException {
                    outputStream.close();
                }
                
                @Override
                public void flush() throws IOException {
                    outputStream.flush();
                }
                
                @Override
                public Timeout timeout() {
                    return timeout;
                }
                
                @Override
                public String toString() {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("sink(");
                    sb.append(outputStream);
                    sb.append(")");
                    return sb.toString();
                }
                
                @Override
                public void write(final Buffer buffer, long n) throws IOException {
                    Util.checkOffsetAndCount(buffer.size, 0L, n);
                    while (n > 0L) {
                        timeout.throwIfReached();
                        final Segment head = buffer.head;
                        final int n2 = (int)Math.min(n, head.limit - head.pos);
                        outputStream.write(head.data, head.pos, n2);
                        head.pos += n2;
                        final long n3 = n2;
                        buffer.size -= n3;
                        if (head.pos == head.limit) {
                            buffer.head = head.pop();
                            SegmentPool.recycle(head);
                        }
                        n -= n3;
                    }
                }
            };
        }
        throw new IllegalArgumentException("timeout == null");
    }
    
    public static Sink sink(final Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        if (socket.getOutputStream() != null) {
            final AsyncTimeout timeout = timeout(socket);
            return timeout.sink(sink(socket.getOutputStream(), timeout));
        }
        throw new IOException("socket's output stream == null");
    }
    
    public static Source source(final File file) throws FileNotFoundException {
        if (file != null) {
            return source(new FileInputStream(file));
        }
        throw new IllegalArgumentException("file == null");
    }
    
    public static Source source(final InputStream inputStream) {
        return source(inputStream, new Timeout());
    }
    
    private static Source source(final InputStream inputStream, final Timeout timeout) {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (timeout != null) {
            return new Source() {
                @Override
                public void close() throws IOException {
                    inputStream.close();
                }
                
                @Override
                public long read(final Buffer buffer, long size) throws IOException {
                    if (size >= 0L) {
                        if (size == 0L) {
                            return 0L;
                        }
                        try {
                            timeout.throwIfReached();
                            final Segment writableSegment = buffer.writableSegment(1);
                            final int read = inputStream.read(writableSegment.data, writableSegment.limit, (int)Math.min(size, 8192 - writableSegment.limit));
                            if (read == -1) {
                                return -1L;
                            }
                            writableSegment.limit += read;
                            size = buffer.size;
                            final long n = read;
                            buffer.size = size + n;
                            return n;
                        }
                        catch (AssertionError assertionError) {
                            if (Okio.isAndroidGetsocknameError(assertionError)) {
                                throw new IOException(assertionError);
                            }
                            throw assertionError;
                        }
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("byteCount < 0: ");
                    sb.append(size);
                    throw new IllegalArgumentException(sb.toString());
                }
                
                @Override
                public Timeout timeout() {
                    return timeout;
                }
                
                @Override
                public String toString() {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("source(");
                    sb.append(inputStream);
                    sb.append(")");
                    return sb.toString();
                }
            };
        }
        throw new IllegalArgumentException("timeout == null");
    }
    
    public static Source source(final Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        if (socket.getInputStream() != null) {
            final AsyncTimeout timeout = timeout(socket);
            return timeout.source(source(socket.getInputStream(), timeout));
        }
        throw new IOException("socket's input stream == null");
    }
    
    private static AsyncTimeout timeout(final Socket socket) {
        return new AsyncTimeout() {
            @Override
            protected IOException newTimeoutException(@Nullable final IOException ex) {
                final SocketTimeoutException ex2 = new SocketTimeoutException("timeout");
                if (ex != null) {
                    ex2.initCause(ex);
                }
                return ex2;
            }
            
            @Override
            protected void timedOut() {
                try {
                    socket.close();
                }
                catch (AssertionError assertionError) {
                    if (!Okio.isAndroidGetsocknameError(assertionError)) {
                        goto Label_0063;
                    }
                    final Logger logger = Okio.logger;
                    final Level level = Level.WARNING;
                    final StringBuilder sb = new StringBuilder();
                }
                catch (Exception assertionError) {
                    final Logger logger = Okio.logger;
                    final Level level = Level.WARNING;
                    final StringBuilder sb = new StringBuilder();
                    goto Label_0033;
                }
            }
        };
    }
}
