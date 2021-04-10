package okhttp3.internal.http2;

import okhttp3.internal.connection.*;
import java.util.*;
import okhttp3.internal.*;
import java.net.*;
import java.io.*;
import okhttp3.*;
import okhttp3.internal.http.*;
import java.util.concurrent.*;
import okio.*;

public final class Http2Codec implements HttpCodec
{
    private static final ByteString CONNECTION;
    private static final ByteString ENCODING;
    private static final ByteString HOST;
    private static final List<ByteString> HTTP_2_SKIPPED_REQUEST_HEADERS;
    private static final List<ByteString> HTTP_2_SKIPPED_RESPONSE_HEADERS;
    private static final ByteString KEEP_ALIVE;
    private static final ByteString PROXY_CONNECTION;
    private static final ByteString TE;
    private static final ByteString TRANSFER_ENCODING;
    private static final ByteString UPGRADE;
    private final Interceptor.Chain chain;
    private final OkHttpClient client;
    private final Http2Connection connection;
    private Http2Stream stream;
    final StreamAllocation streamAllocation;
    
    static {
        CONNECTION = ByteString.encodeUtf8("connection");
        HOST = ByteString.encodeUtf8("host");
        KEEP_ALIVE = ByteString.encodeUtf8("keep-alive");
        PROXY_CONNECTION = ByteString.encodeUtf8("proxy-connection");
        TRANSFER_ENCODING = ByteString.encodeUtf8("transfer-encoding");
        TE = ByteString.encodeUtf8("te");
        ENCODING = ByteString.encodeUtf8("encoding");
        HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList(Http2Codec.CONNECTION, Http2Codec.HOST, Http2Codec.KEEP_ALIVE, Http2Codec.PROXY_CONNECTION, Http2Codec.TE, Http2Codec.TRANSFER_ENCODING, Http2Codec.ENCODING, UPGRADE = ByteString.encodeUtf8("upgrade"), Header.TARGET_METHOD, Header.TARGET_PATH, Header.TARGET_SCHEME, Header.TARGET_AUTHORITY);
        HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList(Http2Codec.CONNECTION, Http2Codec.HOST, Http2Codec.KEEP_ALIVE, Http2Codec.PROXY_CONNECTION, Http2Codec.TE, Http2Codec.TRANSFER_ENCODING, Http2Codec.ENCODING, Http2Codec.UPGRADE);
    }
    
    public Http2Codec(final OkHttpClient client, final Interceptor.Chain chain, final StreamAllocation streamAllocation, final Http2Connection connection) {
        this.client = client;
        this.chain = chain;
        this.streamAllocation = streamAllocation;
        this.connection = connection;
    }
    
    public static List<Header> http2HeadersList(final Request request) {
        final Headers headers = request.headers();
        final ArrayList list = new ArrayList<Header>(headers.size() + 4);
        list.add(new Header(Header.TARGET_METHOD, request.method()));
        list.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(request.url())));
        final String header = request.header("Host");
        if (header != null) {
            list.add(new Header(Header.TARGET_AUTHORITY, header));
        }
        list.add(new Header(Header.TARGET_SCHEME, request.url().scheme()));
        for (int i = 0; i < headers.size(); ++i) {
            final ByteString encodeUtf8 = ByteString.encodeUtf8(headers.name(i).toLowerCase(Locale.US));
            if (!Http2Codec.HTTP_2_SKIPPED_REQUEST_HEADERS.contains(encodeUtf8)) {
                list.add(new Header(encodeUtf8, headers.value(i)));
            }
        }
        return (List<Header>)list;
    }
    
    public static Response.Builder readHttp2HeadersList(final List<Header> list) throws IOException {
        Headers.Builder builder = new Headers.Builder();
        final int size = list.size();
        int i = 0;
        StatusLine statusLine = null;
        while (i < size) {
            final Header header = list.get(i);
            StatusLine parse;
            Headers.Builder builder2;
            if (header == null) {
                parse = statusLine;
                builder2 = builder;
                if (statusLine != null) {
                    parse = statusLine;
                    builder2 = builder;
                    if (statusLine.code == 100) {
                        builder2 = new Headers.Builder();
                        parse = null;
                    }
                }
            }
            else {
                final ByteString name = header.name;
                final String utf8 = header.value.utf8();
                if (name.equals(Header.RESPONSE_STATUS)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("HTTP/1.1 ");
                    sb.append(utf8);
                    parse = StatusLine.parse(sb.toString());
                    builder2 = builder;
                }
                else {
                    parse = statusLine;
                    builder2 = builder;
                    if (!Http2Codec.HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(name)) {
                        Internal.instance.addLenient(builder, name.utf8(), utf8);
                        builder2 = builder;
                        parse = statusLine;
                    }
                }
            }
            ++i;
            statusLine = parse;
            builder = builder2;
        }
        if (statusLine != null) {
            return new Response.Builder().protocol(Protocol.HTTP_2).code(statusLine.code).message(statusLine.message).headers(builder.build());
        }
        throw new ProtocolException("Expected ':status' header not present");
    }
    
    @Override
    public void cancel() {
        final Http2Stream stream = this.stream;
        if (stream != null) {
            stream.closeLater(ErrorCode.CANCEL);
        }
    }
    
    @Override
    public Sink createRequestBody(final Request request, final long n) {
        return this.stream.getSink();
    }
    
    @Override
    public void finishRequest() throws IOException {
        this.stream.getSink().close();
    }
    
    @Override
    public void flushRequest() throws IOException {
        this.connection.flush();
    }
    
    @Override
    public ResponseBody openResponseBody(final Response response) throws IOException {
        this.streamAllocation.eventListener.responseBodyStart(this.streamAllocation.call);
        return new RealResponseBody(response.header("Content-Type"), HttpHeaders.contentLength(response), Okio.buffer(new StreamFinishingSource(this.stream.getSource())));
    }
    
    @Override
    public Response.Builder readResponseHeaders(final boolean b) throws IOException {
        final Response.Builder http2HeadersList = readHttp2HeadersList(this.stream.takeResponseHeaders());
        if (b && Internal.instance.code(http2HeadersList) == 100) {
            return null;
        }
        return http2HeadersList;
    }
    
    @Override
    public void writeRequestHeaders(final Request request) throws IOException {
        if (this.stream != null) {
            return;
        }
        final Http2Stream stream = this.connection.newStream(http2HeadersList(request), request.body() != null);
        this.stream = stream;
        stream.readTimeout().timeout(this.chain.readTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.stream.writeTimeout().timeout(this.chain.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
    }
    
    class StreamFinishingSource extends ForwardingSource
    {
        long bytesRead;
        boolean completed;
        
        StreamFinishingSource(final Source source) {
            super(source);
            this.completed = false;
            this.bytesRead = 0L;
        }
        
        private void endOfInput(final IOException ex) {
            if (this.completed) {
                return;
            }
            this.completed = true;
            Http2Codec.this.streamAllocation.streamFinished(false, Http2Codec.this, this.bytesRead, ex);
        }
        
        @Override
        public void close() throws IOException {
            super.close();
            this.endOfInput(null);
        }
        
        @Override
        public long read(final Buffer buffer, long read) throws IOException {
            try {
                read = this.delegate().read(buffer, read);
                if (read > 0L) {
                    this.bytesRead += read;
                }
                return read;
            }
            catch (IOException ex) {
                this.endOfInput(ex);
                throw ex;
            }
        }
    }
}
