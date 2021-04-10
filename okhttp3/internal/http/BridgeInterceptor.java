package okhttp3.internal.http;

import java.util.*;
import okhttp3.internal.*;
import okio.*;
import okhttp3.*;
import java.io.*;

public final class BridgeInterceptor implements Interceptor
{
    private final CookieJar cookieJar;
    
    public BridgeInterceptor(final CookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }
    
    private String cookieHeader(final List<Cookie> list) {
        final StringBuilder sb = new StringBuilder();
        for (int size = list.size(), i = 0; i < size; ++i) {
            if (i > 0) {
                sb.append("; ");
            }
            final Cookie cookie = list.get(i);
            sb.append(cookie.name());
            sb.append('=');
            sb.append(cookie.value());
        }
        return sb.toString();
    }
    
    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Request request = chain.request();
        final Request.Builder builder = request.newBuilder();
        final RequestBody body = request.body();
        if (body != null) {
            final MediaType contentType = body.contentType();
            if (contentType != null) {
                builder.header("Content-Type", contentType.toString());
            }
            final long contentLength = body.contentLength();
            String s;
            if (contentLength != -1L) {
                builder.header("Content-Length", Long.toString(contentLength));
                s = "Transfer-Encoding";
            }
            else {
                builder.header("Transfer-Encoding", "chunked");
                s = "Content-Length";
            }
            builder.removeHeader(s);
        }
        final String header = request.header("Host");
        final boolean b = false;
        if (header == null) {
            builder.header("Host", Util.hostHeader(request.url(), false));
        }
        if (request.header("Connection") == null) {
            builder.header("Connection", "Keep-Alive");
        }
        boolean b2 = b;
        if (request.header("Accept-Encoding") == null) {
            b2 = b;
            if (request.header("Range") == null) {
                b2 = true;
                builder.header("Accept-Encoding", "gzip");
            }
        }
        final List<Cookie> loadForRequest = this.cookieJar.loadForRequest(request.url());
        if (!loadForRequest.isEmpty()) {
            builder.header("Cookie", this.cookieHeader(loadForRequest));
        }
        if (request.header("User-Agent") == null) {
            builder.header("User-Agent", Version.userAgent());
        }
        final Response proceed = chain.proceed(builder.build());
        HttpHeaders.receiveHeaders(this.cookieJar, request.url(), proceed.headers());
        final Response.Builder request2 = proceed.newBuilder().request(request);
        if (b2 && "gzip".equalsIgnoreCase(proceed.header("Content-Encoding")) && HttpHeaders.hasBody(proceed)) {
            final GzipSource gzipSource = new GzipSource(proceed.body().source());
            request2.headers(proceed.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build());
            request2.body(new RealResponseBody(proceed.header("Content-Type"), -1L, Okio.buffer(gzipSource)));
        }
        return request2.build();
    }
}
