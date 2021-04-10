package okhttp3.internal.cache;

import java.util.concurrent.*;
import okio.*;
import okhttp3.internal.*;
import java.io.*;
import okhttp3.internal.http.*;
import okhttp3.*;

public final class CacheInterceptor implements Interceptor
{
    final InternalCache cache;
    
    public CacheInterceptor(final InternalCache cache) {
        this.cache = cache;
    }
    
    private Response cacheWritingResponse(final CacheRequest cacheRequest, final Response response) throws IOException {
        if (cacheRequest == null) {
            return response;
        }
        final Sink body = cacheRequest.body();
        if (body == null) {
            return response;
        }
        return response.newBuilder().body(new RealResponseBody(response.header("Content-Type"), response.body().contentLength(), Okio.buffer(new Source() {
            boolean cacheRequestClosed;
            final /* synthetic */ BufferedSink val$cacheBody = Okio.buffer(body);
            final /* synthetic */ BufferedSource val$source = response.body().source();
            
            @Override
            public void close() throws IOException {
                if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    this.cacheRequestClosed = true;
                    cacheRequest.abort();
                }
                this.val$source.close();
            }
            
            @Override
            public long read(final Buffer buffer, long read) throws IOException {
                try {
                    read = this.val$source.read(buffer, read);
                    if (read == -1L) {
                        if (!this.cacheRequestClosed) {
                            this.cacheRequestClosed = true;
                            this.val$cacheBody.close();
                        }
                        return -1L;
                    }
                    buffer.copyTo(this.val$cacheBody.buffer(), buffer.size() - read, read);
                    this.val$cacheBody.emitCompleteSegments();
                    return read;
                }
                catch (IOException ex) {
                    if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        cacheRequest.abort();
                    }
                    throw ex;
                }
            }
            
            @Override
            public Timeout timeout() {
                return this.val$source.timeout();
            }
        }))).build();
    }
    
    private static Headers combine(final Headers headers, final Headers headers2) {
        final Headers.Builder builder = new Headers.Builder();
        final int size = headers.size();
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            final String name = headers.name(i);
            final String value = headers.value(i);
            if (!"Warning".equalsIgnoreCase(name) || !value.startsWith("1")) {
                if (isContentSpecificHeader(name) || !isEndToEnd(name) || headers2.get(name) == null) {
                    Internal.instance.addLenient(builder, name, value);
                }
            }
        }
        for (int size2 = headers2.size(), j = n; j < size2; ++j) {
            final String name2 = headers2.name(j);
            if (!isContentSpecificHeader(name2) && isEndToEnd(name2)) {
                Internal.instance.addLenient(builder, name2, headers2.value(j));
            }
        }
        return builder.build();
    }
    
    static boolean isContentSpecificHeader(final String s) {
        return "Content-Length".equalsIgnoreCase(s) || "Content-Encoding".equalsIgnoreCase(s) || "Content-Type".equalsIgnoreCase(s);
    }
    
    static boolean isEndToEnd(final String s) {
        return !"Connection".equalsIgnoreCase(s) && !"Keep-Alive".equalsIgnoreCase(s) && !"Proxy-Authenticate".equalsIgnoreCase(s) && !"Proxy-Authorization".equalsIgnoreCase(s) && !"TE".equalsIgnoreCase(s) && !"Trailers".equalsIgnoreCase(s) && !"Transfer-Encoding".equalsIgnoreCase(s) && !"Upgrade".equalsIgnoreCase(s);
    }
    
    private static Response stripBody(final Response response) {
        Response build = response;
        if (response != null) {
            build = response;
            if (response.body() != null) {
                build = response.newBuilder().body(null).build();
            }
        }
        return build;
    }
    
    @Override
    public Response intercept(Chain build) throws IOException {
        final InternalCache cache = this.cache;
        Response response;
        if (cache != null) {
            response = cache.get(build.request());
        }
        else {
            response = null;
        }
        final CacheStrategy value = new CacheStrategy.Factory(System.currentTimeMillis(), build.request(), response).get();
        final Request networkRequest = value.networkRequest;
        final Response cacheResponse = value.cacheResponse;
        final InternalCache cache2 = this.cache;
        if (cache2 != null) {
            cache2.trackResponse(value);
        }
        if (response != null && cacheResponse == null) {
            Util.closeQuietly(response.body());
        }
        if (networkRequest == null && cacheResponse == null) {
            return new Response.Builder().request(build.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build();
        }
        if (networkRequest == null) {
            return cacheResponse.newBuilder().cacheResponse(stripBody(cacheResponse)).build();
        }
        try {
            final Response proceed = build.proceed(networkRequest);
            if (proceed == null && response != null) {
                Util.closeQuietly(response.body());
            }
            if (cacheResponse != null) {
                if (proceed.code() == 304) {
                    response = cacheResponse.newBuilder().headers(combine(cacheResponse.headers(), proceed.headers())).sentRequestAtMillis(proceed.sentRequestAtMillis()).receivedResponseAtMillis(proceed.receivedResponseAtMillis()).cacheResponse(stripBody(cacheResponse)).networkResponse(stripBody(proceed)).build();
                    proceed.body().close();
                    this.cache.trackConditionalCacheHit();
                    this.cache.update(cacheResponse, response);
                    return response;
                }
                Util.closeQuietly(cacheResponse.body());
            }
            build = (Chain)proceed.newBuilder().cacheResponse(stripBody(cacheResponse)).networkResponse(stripBody(proceed)).build();
            if (this.cache != null) {
                if (HttpHeaders.hasBody((Response)build) && CacheStrategy.isCacheable((Response)build, networkRequest)) {
                    return this.cacheWritingResponse(this.cache.put((Response)build), (Response)build);
                }
                if (HttpMethod.invalidatesCache(networkRequest.method())) {
                    try {
                        this.cache.remove(networkRequest);
                        return (Response)build;
                    }
                    catch (IOException ex) {}
                }
            }
            return (Response)build;
        }
        finally {
            if (response != null) {
                Util.closeQuietly(response.body());
            }
        }
    }
}
