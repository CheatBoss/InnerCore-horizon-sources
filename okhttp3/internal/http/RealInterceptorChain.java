package okhttp3.internal.http;

import java.util.*;
import okhttp3.internal.connection.*;
import okhttp3.*;
import java.io.*;

public final class RealInterceptorChain implements Chain
{
    private final Call call;
    private int calls;
    private final int connectTimeout;
    private final RealConnection connection;
    private final EventListener eventListener;
    private final HttpCodec httpCodec;
    private final int index;
    private final List<Interceptor> interceptors;
    private final int readTimeout;
    private final Request request;
    private final StreamAllocation streamAllocation;
    private final int writeTimeout;
    
    public RealInterceptorChain(final List<Interceptor> interceptors, final StreamAllocation streamAllocation, final HttpCodec httpCodec, final RealConnection connection, final int index, final Request request, final Call call, final EventListener eventListener, final int connectTimeout, final int readTimeout, final int writeTimeout) {
        this.interceptors = interceptors;
        this.connection = connection;
        this.streamAllocation = streamAllocation;
        this.httpCodec = httpCodec;
        this.index = index;
        this.request = request;
        this.call = call;
        this.eventListener = eventListener;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
    }
    
    public Call call() {
        return this.call;
    }
    
    @Override
    public int connectTimeoutMillis() {
        return this.connectTimeout;
    }
    
    public Connection connection() {
        return this.connection;
    }
    
    public EventListener eventListener() {
        return this.eventListener;
    }
    
    public HttpCodec httpStream() {
        return this.httpCodec;
    }
    
    @Override
    public Response proceed(final Request request) throws IOException {
        return this.proceed(request, this.streamAllocation, this.httpCodec, this.connection);
    }
    
    public Response proceed(final Request request, final StreamAllocation streamAllocation, final HttpCodec httpCodec, final RealConnection realConnection) throws IOException {
        if (this.index >= this.interceptors.size()) {
            throw new AssertionError();
        }
        ++this.calls;
        if (this.httpCodec != null && !this.connection.supportsUrl(request.url())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("network interceptor ");
            sb.append(this.interceptors.get(this.index - 1));
            sb.append(" must retain the same host and port");
            throw new IllegalStateException(sb.toString());
        }
        if (this.httpCodec != null && this.calls > 1) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("network interceptor ");
            sb2.append(this.interceptors.get(this.index - 1));
            sb2.append(" must call proceed() exactly once");
            throw new IllegalStateException(sb2.toString());
        }
        final RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, streamAllocation, httpCodec, realConnection, this.index + 1, request, this.call, this.eventListener, this.connectTimeout, this.readTimeout, this.writeTimeout);
        final Interceptor interceptor = this.interceptors.get(this.index);
        final Response intercept = interceptor.intercept((Interceptor.Chain)realInterceptorChain);
        if (httpCodec != null && this.index + 1 < this.interceptors.size() && realInterceptorChain.calls != 1) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("network interceptor ");
            sb3.append(interceptor);
            sb3.append(" must call proceed() exactly once");
            throw new IllegalStateException(sb3.toString());
        }
        if (intercept == null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("interceptor ");
            sb4.append(interceptor);
            sb4.append(" returned null");
            throw new NullPointerException(sb4.toString());
        }
        if (intercept.body() != null) {
            return intercept;
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("interceptor ");
        sb5.append(interceptor);
        sb5.append(" returned a response with no body");
        throw new IllegalStateException(sb5.toString());
    }
    
    @Override
    public int readTimeoutMillis() {
        return this.readTimeout;
    }
    
    @Override
    public Request request() {
        return this.request;
    }
    
    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }
    
    @Override
    public int writeTimeoutMillis() {
        return this.writeTimeout;
    }
}
