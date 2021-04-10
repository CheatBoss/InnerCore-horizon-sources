package okhttp3.internal.http;

import okhttp3.internal.*;
import java.net.*;
import okhttp3.internal.connection.*;
import okhttp3.*;
import java.io.*;
import okio.*;

public final class CallServerInterceptor implements Interceptor
{
    private final boolean forWebSocket;
    
    public CallServerInterceptor(final boolean forWebSocket) {
        this.forWebSocket = forWebSocket;
    }
    
    @Override
    public Response intercept(final Chain chain) throws IOException {
        final RealInterceptorChain realInterceptorChain = (RealInterceptorChain)chain;
        final HttpCodec httpStream = realInterceptorChain.httpStream();
        final StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        final RealConnection realConnection = (RealConnection)realInterceptorChain.connection();
        final Request request = realInterceptorChain.request();
        final long currentTimeMillis = System.currentTimeMillis();
        realInterceptorChain.eventListener().requestHeadersStart(realInterceptorChain.call());
        httpStream.writeRequestHeaders(request);
        realInterceptorChain.eventListener().requestHeadersEnd(realInterceptorChain.call(), request);
        final boolean permitsRequestBody = HttpMethod.permitsRequestBody(request.method());
        final Response.Builder builder = null;
        Response.Builder responseHeaders = null;
        Response.Builder builder2 = builder;
        if (permitsRequestBody) {
            builder2 = builder;
            if (request.body() != null) {
                if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
                    httpStream.flushRequest();
                    realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
                    responseHeaders = httpStream.readResponseHeaders(true);
                }
                if (responseHeaders == null) {
                    realInterceptorChain.eventListener().requestBodyStart(realInterceptorChain.call());
                    final CountingSink countingSink = new CountingSink(httpStream.createRequestBody(request, request.body().contentLength()));
                    final BufferedSink buffer = Okio.buffer(countingSink);
                    request.body().writeTo(buffer);
                    buffer.close();
                    realInterceptorChain.eventListener().requestBodyEnd(realInterceptorChain.call(), countingSink.successfulCount);
                    builder2 = responseHeaders;
                }
                else {
                    builder2 = responseHeaders;
                    if (!realConnection.isMultiplexed()) {
                        streamAllocation.noNewStreams();
                        builder2 = responseHeaders;
                    }
                }
            }
        }
        httpStream.finishRequest();
        Object responseHeaders2;
        if ((responseHeaders2 = builder2) == null) {
            realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
            responseHeaders2 = httpStream.readResponseHeaders(false);
        }
        Response response = ((Response.Builder)responseHeaders2).request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int n;
        if ((n = response.code()) == 100) {
            response = httpStream.readResponseHeaders(false).request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            n = response.code();
        }
        realInterceptorChain.eventListener().responseHeadersEnd(realInterceptorChain.call(), response);
        Response.Builder builder3;
        ResponseBody responseBody;
        if (this.forWebSocket && n == 101) {
            builder3 = response.newBuilder();
            responseBody = Util.EMPTY_RESPONSE;
        }
        else {
            builder3 = response.newBuilder();
            responseBody = httpStream.openResponseBody(response);
        }
        final Response build = builder3.body(responseBody).build();
        if ("close".equalsIgnoreCase(build.request().header("Connection")) || "close".equalsIgnoreCase(build.header("Connection"))) {
            streamAllocation.noNewStreams();
        }
        if ((n != 204 && n != 205) || build.body().contentLength() <= 0L) {
            return build;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("HTTP ");
        sb.append(n);
        sb.append(" had non-zero Content-Length: ");
        sb.append(build.body().contentLength());
        throw new ProtocolException(sb.toString());
    }
    
    static final class CountingSink extends ForwardingSink
    {
        long successfulCount;
        
        CountingSink(final Sink sink) {
            super(sink);
        }
        
        @Override
        public void write(final Buffer buffer, final long n) throws IOException {
            super.write(buffer, n);
            this.successfulCount += n;
        }
    }
}
