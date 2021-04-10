package okhttp3.internal.http2;

import java.util.*;
import java.io.*;
import okio.*;
import java.net.*;

public final class Http2Stream
{
    long bytesLeftInWriteWindow;
    final Http2Connection connection;
    ErrorCode errorCode;
    private boolean hasResponseHeaders;
    final int id;
    final StreamTimeout readTimeout;
    private final List<Header> requestHeaders;
    private List<Header> responseHeaders;
    final FramingSink sink;
    private final FramingSource source;
    long unacknowledgedBytesRead;
    final StreamTimeout writeTimeout;
    
    Http2Stream(final int id, final Http2Connection connection, final boolean finished, final boolean finished2, final List<Header> requestHeaders) {
        this.unacknowledgedBytesRead = 0L;
        this.readTimeout = new StreamTimeout();
        this.writeTimeout = new StreamTimeout();
        this.errorCode = null;
        if (connection == null) {
            throw new NullPointerException("connection == null");
        }
        if (requestHeaders != null) {
            this.id = id;
            this.connection = connection;
            this.bytesLeftInWriteWindow = connection.peerSettings.getInitialWindowSize();
            this.source = new FramingSource(connection.okHttpSettings.getInitialWindowSize());
            this.sink = new FramingSink();
            this.source.finished = finished2;
            this.sink.finished = finished;
            this.requestHeaders = requestHeaders;
            return;
        }
        throw new NullPointerException("requestHeaders == null");
    }
    
    private boolean closeInternal(final ErrorCode errorCode) {
        synchronized (this) {
            if (this.errorCode != null) {
                return false;
            }
            if (this.source.finished && this.sink.finished) {
                return false;
            }
            this.errorCode = errorCode;
            this.notifyAll();
            // monitorexit(this)
            this.connection.removeStream(this.id);
            return true;
        }
    }
    
    void addBytesToWriteWindow(final long n) {
        this.bytesLeftInWriteWindow += n;
        if (n > 0L) {
            this.notifyAll();
        }
    }
    
    void cancelStreamIfNecessary() throws IOException {
        while (true) {
            while (true) {
                Label_0091: {
                    Label_0086: {
                        synchronized (this) {
                            if (this.source.finished || !this.source.closed) {
                                break Label_0091;
                            }
                            if (this.sink.finished) {
                                break Label_0086;
                            }
                            if (this.sink.closed) {
                                break Label_0086;
                            }
                            break Label_0091;
                            final boolean open = this.isOpen();
                            // monitorexit(this)
                            // iftrue(Label_0064:, n == 0)
                            // iftrue(Label_0080:, open)
                            Block_7: {
                                break Block_7;
                                while (true) {
                                    this.connection.removeStream(this.id);
                                    Label_0080: {
                                        return;
                                    }
                                    Label_0064:
                                    continue;
                                }
                            }
                            this.close(ErrorCode.CANCEL);
                            return;
                        }
                    }
                    final int n = 1;
                    continue;
                }
                final int n = 0;
                continue;
            }
        }
    }
    
    void checkOutNotClosed() throws IOException {
        if (this.sink.closed) {
            throw new IOException("stream closed");
        }
        if (this.sink.finished) {
            throw new IOException("stream finished");
        }
        if (this.errorCode == null) {
            return;
        }
        throw new StreamResetException(this.errorCode);
    }
    
    public void close(final ErrorCode errorCode) throws IOException {
        if (!this.closeInternal(errorCode)) {
            return;
        }
        this.connection.writeSynReset(this.id, errorCode);
    }
    
    public void closeLater(final ErrorCode errorCode) {
        if (!this.closeInternal(errorCode)) {
            return;
        }
        this.connection.writeSynResetLater(this.id, errorCode);
    }
    
    public int getId() {
        return this.id;
    }
    
    public Sink getSink() {
        synchronized (this) {
            if (!this.hasResponseHeaders && !this.isLocallyInitiated()) {
                throw new IllegalStateException("reply before requesting the sink");
            }
            return this.sink;
        }
    }
    
    public Source getSource() {
        return this.source;
    }
    
    public boolean isLocallyInitiated() {
        return this.connection.client == ((this.id & 0x1) == 0x1);
    }
    
    public boolean isOpen() {
        synchronized (this) {
            return this.errorCode == null && ((!this.source.finished && !this.source.closed) || (!this.sink.finished && !this.sink.closed) || !this.hasResponseHeaders);
        }
    }
    
    public Timeout readTimeout() {
        return this.readTimeout;
    }
    
    void receiveData(final BufferedSource bufferedSource, final int n) throws IOException {
        this.source.receive(bufferedSource, n);
    }
    
    void receiveFin() {
        synchronized (this) {
            this.source.finished = true;
            final boolean open = this.isOpen();
            this.notifyAll();
            // monitorexit(this)
            if (!open) {
                this.connection.removeStream(this.id);
            }
        }
    }
    
    void receiveHeaders(final List<Header> responseHeaders) {
        // monitorenter(this)
        boolean open = true;
        try {
            this.hasResponseHeaders = true;
            if (this.responseHeaders == null) {
                this.responseHeaders = responseHeaders;
                open = this.isOpen();
                this.notifyAll();
            }
            else {
                final ArrayList<Object> responseHeaders2 = new ArrayList<Object>();
                responseHeaders2.addAll(this.responseHeaders);
                responseHeaders2.add(null);
                responseHeaders2.addAll(responseHeaders);
                this.responseHeaders = (List<Header>)responseHeaders2;
            }
            // monitorexit(this)
            if (!open) {
                this.connection.removeStream(this.id);
            }
        }
        finally {
        }
        // monitorexit(this)
    }
    
    void receiveRstStream(final ErrorCode errorCode) {
        synchronized (this) {
            if (this.errorCode == null) {
                this.errorCode = errorCode;
                this.notifyAll();
            }
        }
    }
    
    public List<Header> takeResponseHeaders() throws IOException {
        synchronized (this) {
            if (this.isLocallyInitiated()) {
                this.readTimeout.enter();
                try {
                    while (this.responseHeaders == null && this.errorCode == null) {
                        this.waitForIo();
                    }
                    this.readTimeout.exitAndThrowIfTimedOut();
                    final List<Header> responseHeaders = this.responseHeaders;
                    if (responseHeaders != null) {
                        this.responseHeaders = null;
                        return responseHeaders;
                    }
                    throw new StreamResetException(this.errorCode);
                }
                finally {
                    this.readTimeout.exitAndThrowIfTimedOut();
                }
            }
            throw new IllegalStateException("servers cannot read response headers");
        }
    }
    
    void waitForIo() throws InterruptedIOException {
        try {
            this.wait();
        }
        catch (InterruptedException ex) {
            throw new InterruptedIOException();
        }
    }
    
    public Timeout writeTimeout() {
        return this.writeTimeout;
    }
    
    final class FramingSink implements Sink
    {
        boolean closed;
        boolean finished;
        private final Buffer sendBuffer;
        
        FramingSink() {
            this.sendBuffer = new Buffer();
        }
        
        private void emitFrame(final boolean b) throws IOException {
            while (true) {
                Object o = Http2Stream.this;
                while (true) {
                    synchronized (o) {
                        Http2Stream.this.writeTimeout.enter();
                        try {
                            while (Http2Stream.this.bytesLeftInWriteWindow <= 0L && !this.finished && !this.closed && Http2Stream.this.errorCode == null) {
                                Http2Stream.this.waitForIo();
                            }
                            Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                            Http2Stream.this.checkOutNotClosed();
                            final long min = Math.min(Http2Stream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
                            final Http2Stream this$0 = Http2Stream.this;
                            this$0.bytesLeftInWriteWindow -= min;
                            // monitorexit(o)
                            Http2Stream.this.writeTimeout.enter();
                            try {
                                o = Http2Stream.this.connection;
                                final int id = Http2Stream.this.id;
                                if (b && min == this.sendBuffer.size()) {
                                    final boolean b2 = true;
                                    ((Http2Connection)o).writeData(id, b2, this.sendBuffer, min);
                                    return;
                                }
                            }
                            finally {
                                Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                            }
                        }
                        finally {
                            Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                        }
                    }
                    final boolean b2 = false;
                    continue;
                }
            }
        }
        
        @Override
        public void close() throws IOException {
            synchronized (Http2Stream.this) {
                if (this.closed) {
                    return;
                }
                // monitorexit(this.this$0)
                if (!Http2Stream.this.sink.finished) {
                    if (this.sendBuffer.size() > 0L) {
                        while (this.sendBuffer.size() > 0L) {
                            this.emitFrame(true);
                        }
                    }
                    else {
                        Http2Stream.this.connection.writeData(Http2Stream.this.id, true, null, 0L);
                    }
                }
                final Http2Stream this$0 = Http2Stream.this;
                synchronized (Http2Stream.this) {
                    this.closed = true;
                    // monitorexit(this.this$0)
                    Http2Stream.this.connection.flush();
                    Http2Stream.this.cancelStreamIfNecessary();
                }
            }
        }
        
        @Override
        public void flush() throws IOException {
            synchronized (Http2Stream.this) {
                Http2Stream.this.checkOutNotClosed();
                // monitorexit(this.this$0)
                while (this.sendBuffer.size() > 0L) {
                    this.emitFrame(false);
                    Http2Stream.this.connection.flush();
                }
            }
        }
        
        @Override
        public Timeout timeout() {
            return Http2Stream.this.writeTimeout;
        }
        
        @Override
        public void write(final Buffer buffer, final long n) throws IOException {
            this.sendBuffer.write(buffer, n);
            while (this.sendBuffer.size() >= 16384L) {
                this.emitFrame(false);
            }
        }
    }
    
    private final class FramingSource implements Source
    {
        boolean closed;
        boolean finished;
        private final long maxByteCount;
        private final Buffer readBuffer;
        private final Buffer receiveBuffer;
        
        FramingSource(final long maxByteCount) {
            this.receiveBuffer = new Buffer();
            this.readBuffer = new Buffer();
            this.maxByteCount = maxByteCount;
        }
        
        private void checkNotClosed() throws IOException {
            if (this.closed) {
                throw new IOException("stream closed");
            }
            if (Http2Stream.this.errorCode == null) {
                return;
            }
            throw new StreamResetException(Http2Stream.this.errorCode);
        }
        
        private void waitUntilReadable() throws IOException {
            Http2Stream.this.readTimeout.enter();
            try {
                while (this.readBuffer.size() == 0L && !this.finished && !this.closed && Http2Stream.this.errorCode == null) {
                    Http2Stream.this.waitForIo();
                }
            }
            finally {
                Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
            }
        }
        
        @Override
        public void close() throws IOException {
            synchronized (Http2Stream.this) {
                this.closed = true;
                this.readBuffer.clear();
                Http2Stream.this.notifyAll();
                // monitorexit(this.this$0)
                Http2Stream.this.cancelStreamIfNecessary();
            }
        }
        
        @Override
        public long read(final Buffer buffer, long read) throws IOException {
            if (read >= 0L) {
                synchronized (Http2Stream.this) {
                    this.waitUntilReadable();
                    this.checkNotClosed();
                    if (this.readBuffer.size() == 0L) {
                        return -1L;
                    }
                    read = this.readBuffer.read(buffer, Math.min(read, this.readBuffer.size()));
                    final Http2Stream this$0 = Http2Stream.this;
                    this$0.unacknowledgedBytesRead += read;
                    if (Http2Stream.this.unacknowledgedBytesRead >= Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2) {
                        Http2Stream.this.connection.writeWindowUpdateLater(Http2Stream.this.id, Http2Stream.this.unacknowledgedBytesRead);
                        Http2Stream.this.unacknowledgedBytesRead = 0L;
                    }
                    // monitorexit(this.this$0)
                    synchronized (Http2Stream.this.connection) {
                        final Http2Connection connection = Http2Stream.this.connection;
                        connection.unacknowledgedBytesRead += read;
                        if (Http2Stream.this.connection.unacknowledgedBytesRead >= Http2Stream.this.connection.okHttpSettings.getInitialWindowSize() / 2) {
                            Http2Stream.this.connection.writeWindowUpdateLater(0, Http2Stream.this.connection.unacknowledgedBytesRead);
                            Http2Stream.this.connection.unacknowledgedBytesRead = 0L;
                        }
                        return read;
                    }
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(read);
            throw new IllegalArgumentException(sb.toString());
        }
        
        void receive(final BufferedSource bufferedSource, long n) throws IOException {
        Label_0199:
            while (n > 0L) {
                while (true) {
                    while (true) {
                        Label_0200: {
                            synchronized (Http2Stream.this) {
                                final boolean finished = this.finished;
                                final long size = this.readBuffer.size();
                                final long maxByteCount = this.maxByteCount;
                                final boolean b = false;
                                if (size + n <= maxByteCount) {
                                    break Label_0200;
                                }
                                final int n2 = 1;
                                // monitorexit(this.this$0)
                                if (n2 != 0) {
                                    bufferedSource.skip(n);
                                    Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                                    return;
                                }
                                if (finished) {
                                    bufferedSource.skip(n);
                                    return;
                                }
                                final long read = bufferedSource.read(this.receiveBuffer, n);
                                if (read != -1L) {
                                    final Http2Stream this$0 = Http2Stream.this;
                                    // monitorenter(this.this$0)
                                    boolean b2 = b;
                                    try {
                                        if (this.readBuffer.size() == 0L) {
                                            b2 = true;
                                        }
                                        this.readBuffer.writeAll(this.receiveBuffer);
                                        if (b2) {
                                            Http2Stream.this.notifyAll();
                                        }
                                        // monitorexit(this.this$0)
                                        n -= read;
                                        break;
                                    }
                                    finally {
                                    }
                                    // monitorexit(this.this$0)
                                }
                                throw new EOFException();
                            }
                            break Label_0199;
                        }
                        final int n2 = 0;
                        continue;
                    }
                }
            }
        }
        
        @Override
        public Timeout timeout() {
            return Http2Stream.this.readTimeout;
        }
    }
    
    class StreamTimeout extends AsyncTimeout
    {
        public void exitAndThrowIfTimedOut() throws IOException {
            if (!this.exit()) {
                return;
            }
            throw this.newTimeoutException(null);
        }
        
        @Override
        protected IOException newTimeoutException(final IOException ex) {
            final SocketTimeoutException ex2 = new SocketTimeoutException("timeout");
            if (ex != null) {
                ex2.initCause(ex);
            }
            return ex2;
        }
        
        @Override
        protected void timedOut() {
            Http2Stream.this.closeLater(ErrorCode.CANCEL);
        }
    }
}
