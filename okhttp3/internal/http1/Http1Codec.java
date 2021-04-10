package okhttp3.internal.http1;

import okhttp3.internal.connection.*;
import okhttp3.*;
import java.io.*;
import okhttp3.internal.http.*;
import okio.*;
import java.net.*;
import java.util.concurrent.*;
import okhttp3.internal.*;

public final class Http1Codec implements HttpCodec
{
    final OkHttpClient client;
    private long headerLimit;
    final BufferedSink sink;
    final BufferedSource source;
    int state;
    final StreamAllocation streamAllocation;
    
    public Http1Codec(final OkHttpClient client, final StreamAllocation streamAllocation, final BufferedSource source, final BufferedSink sink) {
        this.state = 0;
        this.headerLimit = 262144L;
        this.client = client;
        this.streamAllocation = streamAllocation;
        this.source = source;
        this.sink = sink;
    }
    
    private String readHeaderLine() throws IOException {
        final String utf8LineStrict = this.source.readUtf8LineStrict(this.headerLimit);
        this.headerLimit -= utf8LineStrict.length();
        return utf8LineStrict;
    }
    
    @Override
    public void cancel() {
        final RealConnection connection = this.streamAllocation.connection();
        if (connection != null) {
            connection.cancel();
        }
    }
    
    @Override
    public Sink createRequestBody(final Request request, final long n) {
        if ("chunked".equalsIgnoreCase(request.header("Transfer-Encoding"))) {
            return this.newChunkedSink();
        }
        if (n != -1L) {
            return this.newFixedLengthSink(n);
        }
        throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
    }
    
    void detachTimeout(final ForwardingTimeout forwardingTimeout) {
        final Timeout delegate = forwardingTimeout.delegate();
        forwardingTimeout.setDelegate(Timeout.NONE);
        delegate.clearDeadline();
        delegate.clearTimeout();
    }
    
    @Override
    public void finishRequest() throws IOException {
        this.sink.flush();
    }
    
    @Override
    public void flushRequest() throws IOException {
        this.sink.flush();
    }
    
    public Sink newChunkedSink() {
        if (this.state == 1) {
            this.state = 2;
            return new ChunkedSink();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("state: ");
        sb.append(this.state);
        throw new IllegalStateException(sb.toString());
    }
    
    public Source newChunkedSource(final HttpUrl httpUrl) throws IOException {
        if (this.state == 4) {
            this.state = 5;
            return new ChunkedSource(httpUrl);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("state: ");
        sb.append(this.state);
        throw new IllegalStateException(sb.toString());
    }
    
    public Sink newFixedLengthSink(final long n) {
        if (this.state == 1) {
            this.state = 2;
            return new FixedLengthSink(n);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("state: ");
        sb.append(this.state);
        throw new IllegalStateException(sb.toString());
    }
    
    public Source newFixedLengthSource(final long n) throws IOException {
        if (this.state == 4) {
            this.state = 5;
            return new FixedLengthSource(n);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("state: ");
        sb.append(this.state);
        throw new IllegalStateException(sb.toString());
    }
    
    public Source newUnknownLengthSource() throws IOException {
        if (this.state != 4) {
            final StringBuilder sb = new StringBuilder();
            sb.append("state: ");
            sb.append(this.state);
            throw new IllegalStateException(sb.toString());
        }
        final StreamAllocation streamAllocation = this.streamAllocation;
        if (streamAllocation != null) {
            this.state = 5;
            streamAllocation.noNewStreams();
            return new UnknownLengthSource();
        }
        throw new IllegalStateException("streamAllocation == null");
    }
    
    @Override
    public ResponseBody openResponseBody(final Response response) throws IOException {
        this.streamAllocation.eventListener.responseBodyStart(this.streamAllocation.call);
        final String header = response.header("Content-Type");
        if (!HttpHeaders.hasBody(response)) {
            return new RealResponseBody(header, 0L, Okio.buffer(this.newFixedLengthSource(0L)));
        }
        if ("chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return new RealResponseBody(header, -1L, Okio.buffer(this.newChunkedSource(response.request().url())));
        }
        final long contentLength = HttpHeaders.contentLength(response);
        if (contentLength != -1L) {
            return new RealResponseBody(header, contentLength, Okio.buffer(this.newFixedLengthSource(contentLength)));
        }
        return new RealResponseBody(header, -1L, Okio.buffer(this.newUnknownLengthSource()));
    }
    
    public Headers readHeaders() throws IOException {
        final Headers.Builder builder = new Headers.Builder();
        while (true) {
            final String headerLine = this.readHeaderLine();
            if (headerLine.length() == 0) {
                break;
            }
            Internal.instance.addLenient(builder, headerLine);
        }
        return builder.build();
    }
    
    @Override
    public Response.Builder readResponseHeaders(final boolean b) throws IOException {
        final int state = this.state;
        if (state != 1) {
            if (state != 3) {
                final StringBuilder sb = new StringBuilder();
                sb.append("state: ");
                sb.append(this.state);
                throw new IllegalStateException(sb.toString());
            }
        }
        try {
            final StatusLine parse = StatusLine.parse(this.readHeaderLine());
            final Response.Builder headers = new Response.Builder().protocol(parse.protocol).code(parse.code).message(parse.message).headers(this.readHeaders());
            if (b && parse.code == 100) {
                return null;
            }
            if (parse.code == 100) {
                this.state = 3;
                return headers;
            }
            this.state = 4;
            return headers;
        }
        catch (EOFException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("unexpected end of stream on ");
            sb2.append(this.streamAllocation);
            final IOException ex = new IOException(sb2.toString());
            ex.initCause(ex2);
            throw ex;
        }
    }
    
    public void writeRequest(final Headers headers, final String s) throws IOException {
        if (this.state == 0) {
            this.sink.writeUtf8(s).writeUtf8("\r\n");
            for (int i = 0; i < headers.size(); ++i) {
                this.sink.writeUtf8(headers.name(i)).writeUtf8(": ").writeUtf8(headers.value(i)).writeUtf8("\r\n");
            }
            this.sink.writeUtf8("\r\n");
            this.state = 1;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("state: ");
        sb.append(this.state);
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public void writeRequestHeaders(final Request request) throws IOException {
        this.writeRequest(request.headers(), RequestLine.get(request, this.streamAllocation.connection().route().proxy().type()));
    }
    
    private abstract class AbstractSource implements Source
    {
        protected long bytesRead;
        protected boolean closed;
        protected final ForwardingTimeout timeout;
        
        private AbstractSource() {
            this.timeout = new ForwardingTimeout(Http1Codec.this.source.timeout());
            this.bytesRead = 0L;
        }
        
        protected final void endOfInput(final boolean b, final IOException ex) throws IOException {
            if (Http1Codec.this.state == 6) {
                return;
            }
            if (Http1Codec.this.state == 5) {
                Http1Codec.this.detachTimeout(this.timeout);
                Http1Codec.this.state = 6;
                if (Http1Codec.this.streamAllocation != null) {
                    Http1Codec.this.streamAllocation.streamFinished(b ^ true, Http1Codec.this, this.bytesRead, ex);
                }
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("state: ");
            sb.append(Http1Codec.this.state);
            throw new IllegalStateException(sb.toString());
        }
        
        @Override
        public long read(final Buffer buffer, long read) throws IOException {
            try {
                read = Http1Codec.this.source.read(buffer, read);
                if (read > 0L) {
                    this.bytesRead += read;
                }
                return read;
            }
            catch (IOException ex) {
                this.endOfInput(false, ex);
                throw ex;
            }
        }
        
        @Override
        public Timeout timeout() {
            return this.timeout;
        }
    }
    
    private final class ChunkedSink implements Sink
    {
        private boolean closed;
        private final ForwardingTimeout timeout;
        
        ChunkedSink() {
            this.timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
        }
        
        @Override
        public void close() throws IOException {
            synchronized (this) {
                if (this.closed) {
                    return;
                }
                this.closed = true;
                Http1Codec.this.sink.writeUtf8("0\r\n\r\n");
                Http1Codec.this.detachTimeout(this.timeout);
                Http1Codec.this.state = 3;
            }
        }
        
        @Override
        public void flush() throws IOException {
            synchronized (this) {
                if (this.closed) {
                    return;
                }
                Http1Codec.this.sink.flush();
            }
        }
        
        @Override
        public Timeout timeout() {
            return this.timeout;
        }
        
        @Override
        public void write(final Buffer buffer, final long n) throws IOException {
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (n == 0L) {
                return;
            }
            Http1Codec.this.sink.writeHexadecimalUnsignedLong(n);
            Http1Codec.this.sink.writeUtf8("\r\n");
            Http1Codec.this.sink.write(buffer, n);
            Http1Codec.this.sink.writeUtf8("\r\n");
        }
    }
    
    private class ChunkedSource extends AbstractSource
    {
        private long bytesRemainingInChunk;
        private boolean hasMoreChunks;
        private final HttpUrl url;
        
        ChunkedSource(final HttpUrl url) {
            this.bytesRemainingInChunk = -1L;
            this.hasMoreChunks = true;
            this.url = url;
        }
        
        private void readChunkSize() throws IOException {
            if (this.bytesRemainingInChunk != -1L) {
                Http1Codec.this.source.readUtf8LineStrict();
            }
            try {
                this.bytesRemainingInChunk = Http1Codec.this.source.readHexadecimalUnsignedLong();
                final String trim = Http1Codec.this.source.readUtf8LineStrict().trim();
                if (this.bytesRemainingInChunk >= 0L && (trim.isEmpty() || trim.startsWith(";"))) {
                    if (this.bytesRemainingInChunk == 0L) {
                        this.hasMoreChunks = false;
                        HttpHeaders.receiveHeaders(Http1Codec.this.client.cookieJar(), this.url, Http1Codec.this.readHeaders());
                        ((AbstractSource)this).endOfInput(true, null);
                    }
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("expected chunk size and optional extensions but was \"");
                sb.append(this.bytesRemainingInChunk);
                sb.append(trim);
                sb.append("\"");
                throw new ProtocolException(sb.toString());
            }
            catch (NumberFormatException ex) {
                throw new ProtocolException(ex.getMessage());
            }
        }
        
        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                ((AbstractSource)this).endOfInput(false, null);
            }
            this.closed = true;
        }
        
        @Override
        public long read(final Buffer buffer, long read) throws IOException {
            if (read < 0L) {
                final StringBuilder sb = new StringBuilder();
                sb.append("byteCount < 0: ");
                sb.append(read);
                throw new IllegalArgumentException(sb.toString());
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (!this.hasMoreChunks) {
                return -1L;
            }
            final long bytesRemainingInChunk = this.bytesRemainingInChunk;
            if (bytesRemainingInChunk == 0L || bytesRemainingInChunk == -1L) {
                this.readChunkSize();
                if (!this.hasMoreChunks) {
                    return -1L;
                }
            }
            read = super.read(buffer, Math.min(read, this.bytesRemainingInChunk));
            if (read != -1L) {
                this.bytesRemainingInChunk -= read;
                return read;
            }
            final ProtocolException ex = new ProtocolException("unexpected end of stream");
            ((AbstractSource)this).endOfInput(false, ex);
            throw ex;
        }
    }
    
    private final class FixedLengthSink implements Sink
    {
        private long bytesRemaining;
        private boolean closed;
        private final ForwardingTimeout timeout;
        
        FixedLengthSink(final long bytesRemaining) {
            this.timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
            this.bytesRemaining = bytesRemaining;
        }
        
        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.bytesRemaining <= 0L) {
                Http1Codec.this.detachTimeout(this.timeout);
                Http1Codec.this.state = 3;
                return;
            }
            throw new ProtocolException("unexpected end of stream");
        }
        
        @Override
        public void flush() throws IOException {
            if (this.closed) {
                return;
            }
            Http1Codec.this.sink.flush();
        }
        
        @Override
        public Timeout timeout() {
            return this.timeout;
        }
        
        @Override
        public void write(final Buffer buffer, final long n) throws IOException {
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            Util.checkOffsetAndCount(buffer.size(), 0L, n);
            if (n <= this.bytesRemaining) {
                Http1Codec.this.sink.write(buffer, n);
                this.bytesRemaining -= n;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("expected ");
            sb.append(this.bytesRemaining);
            sb.append(" bytes but received ");
            sb.append(n);
            throw new ProtocolException(sb.toString());
        }
    }
    
    private class FixedLengthSource extends AbstractSource
    {
        private long bytesRemaining;
        
        FixedLengthSource(final long bytesRemaining) throws IOException {
            this.bytesRemaining = bytesRemaining;
            if (bytesRemaining == 0L) {
                ((AbstractSource)this).endOfInput(true, null);
            }
        }
        
        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            if (this.bytesRemaining != 0L && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                ((AbstractSource)this).endOfInput(false, null);
            }
            this.closed = true;
        }
        
        @Override
        public long read(final Buffer buffer, long read) throws IOException {
            if (read < 0L) {
                final StringBuilder sb = new StringBuilder();
                sb.append("byteCount < 0: ");
                sb.append(read);
                throw new IllegalArgumentException(sb.toString());
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            final long bytesRemaining = this.bytesRemaining;
            if (bytesRemaining == 0L) {
                return -1L;
            }
            read = super.read(buffer, Math.min(bytesRemaining, read));
            if (read != -1L) {
                final long bytesRemaining2 = this.bytesRemaining - read;
                this.bytesRemaining = bytesRemaining2;
                if (bytesRemaining2 == 0L) {
                    ((AbstractSource)this).endOfInput(true, null);
                }
                return read;
            }
            final ProtocolException ex = new ProtocolException("unexpected end of stream");
            ((AbstractSource)this).endOfInput(false, ex);
            throw ex;
        }
    }
    
    private class UnknownLengthSource extends AbstractSource
    {
        private boolean inputExhausted;
        
        UnknownLengthSource() {
        }
        
        @Override
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            if (!this.inputExhausted) {
                ((AbstractSource)this).endOfInput(false, null);
            }
            this.closed = true;
        }
        
        @Override
        public long read(final Buffer buffer, long read) throws IOException {
            if (read < 0L) {
                final StringBuilder sb = new StringBuilder();
                sb.append("byteCount < 0: ");
                sb.append(read);
                throw new IllegalArgumentException(sb.toString());
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (this.inputExhausted) {
                return -1L;
            }
            read = super.read(buffer, read);
            if (read == -1L) {
                ((AbstractSource)this).endOfInput(this.inputExhausted = true, null);
                return -1L;
            }
            return read;
        }
    }
}
