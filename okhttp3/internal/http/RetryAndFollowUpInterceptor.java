package okhttp3.internal.http;

import java.security.cert.*;
import javax.net.ssl.*;
import okhttp3.internal.*;
import java.io.*;
import java.net.*;
import okhttp3.internal.http2.*;
import okhttp3.internal.connection.*;
import okhttp3.*;

public final class RetryAndFollowUpInterceptor implements Interceptor
{
    private Object callStackTrace;
    private volatile boolean canceled;
    private final OkHttpClient client;
    private final boolean forWebSocket;
    private volatile StreamAllocation streamAllocation;
    
    public RetryAndFollowUpInterceptor(final OkHttpClient client, final boolean forWebSocket) {
        this.client = client;
        this.forWebSocket = forWebSocket;
    }
    
    private Address createAddress(final HttpUrl httpUrl) {
        SSLSocketFactory sslSocketFactory;
        Object certificatePinner;
        HostnameVerifier hostnameVerifier2;
        if (httpUrl.isHttps()) {
            sslSocketFactory = this.client.sslSocketFactory();
            final HostnameVerifier hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
            hostnameVerifier2 = hostnameVerifier;
        }
        else {
            final SSLSocketFactory sslSocketFactory2 = null;
            certificatePinner = (hostnameVerifier2 = (HostnameVerifier)sslSocketFactory2);
            sslSocketFactory = sslSocketFactory2;
        }
        return new Address(httpUrl.host(), httpUrl.port(), this.client.dns(), this.client.socketFactory(), sslSocketFactory, hostnameVerifier2, (CertificatePinner)certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }
    
    private Request followUpRequest(final Response response, final Route route) throws IOException {
        if (response == null) {
            throw new IllegalStateException();
        }
        final int code = response.code();
        final String method = response.request().method();
        RequestBody body = null;
        if (code != 307 && code != 308) {
            if (code == 401) {
                return this.client.authenticator().authenticate(route, response);
            }
            if (code != 503) {
                if (code != 407) {
                    if (code != 408) {
                        switch (code) {
                            default: {
                                return null;
                            }
                            case 300:
                            case 301:
                            case 302:
                            case 303: {
                                break;
                            }
                        }
                    }
                    else {
                        if (!this.client.retryOnConnectionFailure()) {
                            return null;
                        }
                        if (response.request().body() instanceof UnrepeatableRequestBody) {
                            return null;
                        }
                        if (response.priorResponse() != null && response.priorResponse().code() == 408) {
                            return null;
                        }
                        if (this.retryAfter(response, 0) > 0) {
                            return null;
                        }
                        return response.request();
                    }
                }
                else {
                    Proxy proxy;
                    if (route != null) {
                        proxy = route.proxy();
                    }
                    else {
                        proxy = this.client.proxy();
                    }
                    if (proxy.type() == Proxy.Type.HTTP) {
                        return this.client.proxyAuthenticator().authenticate(route, response);
                    }
                    throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                }
            }
            else {
                if (response.priorResponse() != null && response.priorResponse().code() == 503) {
                    return null;
                }
                if (this.retryAfter(response, Integer.MAX_VALUE) == 0) {
                    return response.request();
                }
                return null;
            }
        }
        else if (!method.equals("GET") && !method.equals("HEAD")) {
            return null;
        }
        if (!this.client.followRedirects()) {
            return null;
        }
        final String header = response.header("Location");
        if (header == null) {
            return null;
        }
        final HttpUrl resolve = response.request().url().resolve(header);
        if (resolve == null) {
            return null;
        }
        if (!resolve.scheme().equals(response.request().url().scheme()) && !this.client.followSslRedirects()) {
            return null;
        }
        final Request.Builder builder = response.request().newBuilder();
        if (HttpMethod.permitsRequestBody(method)) {
            final boolean redirectsWithBody = HttpMethod.redirectsWithBody(method);
            String s;
            if (HttpMethod.redirectsToGet(method)) {
                s = "GET";
            }
            else {
                s = method;
                if (redirectsWithBody) {
                    body = response.request().body();
                    s = method;
                }
            }
            builder.method(s, body);
            if (!redirectsWithBody) {
                builder.removeHeader("Transfer-Encoding");
                builder.removeHeader("Content-Length");
                builder.removeHeader("Content-Type");
            }
        }
        if (!this.sameConnection(response, resolve)) {
            builder.removeHeader("Authorization");
        }
        return builder.url(resolve).build();
    }
    
    private boolean isRecoverable(final IOException ex, final boolean b) {
        final boolean b2 = ex instanceof ProtocolException;
        final boolean b3 = false;
        if (b2) {
            return false;
        }
        if (ex instanceof InterruptedIOException) {
            boolean b4 = b3;
            if (ex instanceof SocketTimeoutException) {
                b4 = b3;
                if (!b) {
                    b4 = true;
                }
            }
            return b4;
        }
        return (!(ex instanceof SSLHandshakeException) || !(ex.getCause() instanceof CertificateException)) && !(ex instanceof SSLPeerUnverifiedException);
    }
    
    private boolean recover(final IOException ex, final StreamAllocation streamAllocation, final boolean b, final Request request) {
        streamAllocation.streamFailed(ex);
        return this.client.retryOnConnectionFailure() && (!b || !(request.body() instanceof UnrepeatableRequestBody)) && this.isRecoverable(ex, b) && streamAllocation.hasMoreRoutes();
    }
    
    private int retryAfter(final Response response, final int n) {
        final String header = response.header("Retry-After");
        if (header == null) {
            return n;
        }
        if (header.matches("\\d+")) {
            return Integer.valueOf(header);
        }
        return Integer.MAX_VALUE;
    }
    
    private boolean sameConnection(final Response response, final HttpUrl httpUrl) {
        final HttpUrl url = response.request().url();
        return url.host().equals(httpUrl.host()) && url.port() == httpUrl.port() && url.scheme().equals(httpUrl.scheme());
    }
    
    public void cancel() {
        this.canceled = true;
        final StreamAllocation streamAllocation = this.streamAllocation;
        if (streamAllocation != null) {
            streamAllocation.cancel();
        }
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Object request = chain.request();
        final RealInterceptorChain realInterceptorChain = (RealInterceptorChain)chain;
        final Call call = realInterceptorChain.call();
        final EventListener eventListener = realInterceptorChain.eventListener();
        Object o = new StreamAllocation(this.client.connectionPool(), this.createAddress(((Request)request).url()), call, eventListener, this.callStackTrace);
        this.streamAllocation = (StreamAllocation)o;
        Response response = null;
        int n = 0;
        chain = (Chain)request;
    Label_0075:
        while (!this.canceled) {
            while (true) {
                while (true) {
                    Label_0465: {
                        try {
                            try {
                                final Response proceed = realInterceptorChain.proceed((Request)chain, (StreamAllocation)o, null, null);
                                Response build;
                                if (response != null) {
                                    build = proceed.newBuilder().priorResponse(response.newBuilder().body(null).build()).build();
                                }
                                else {
                                    build = proceed;
                                }
                                final Request followUpRequest = this.followUpRequest(build, ((StreamAllocation)o).route());
                                if (followUpRequest == null) {
                                    if (!this.forWebSocket) {
                                        ((StreamAllocation)o).release();
                                    }
                                    return build;
                                }
                                Util.closeQuietly(build.body());
                                ++n;
                                if (n > 20) {
                                    ((StreamAllocation)o).release();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Too many follow-up requests: ");
                                    sb.append(n);
                                    throw new ProtocolException(sb.toString());
                                }
                                if (!(followUpRequest.body() instanceof UnrepeatableRequestBody)) {
                                    if (!this.sameConnection(build, followUpRequest.url())) {
                                        ((StreamAllocation)o).release();
                                        o = new StreamAllocation(this.client.connectionPool(), this.createAddress(followUpRequest.url()), call, eventListener, this.callStackTrace);
                                        this.streamAllocation = (StreamAllocation)o;
                                    }
                                    else if (((StreamAllocation)o).codec() != null) {
                                        o = new StringBuilder();
                                        ((StringBuilder)o).append("Closing the body of ");
                                        ((StringBuilder)o).append(build);
                                        ((StringBuilder)o).append(" didn't close its backing stream. Bad interceptor?");
                                        throw new IllegalStateException(((StringBuilder)o).toString());
                                    }
                                    response = build;
                                    continue Label_0075;
                                }
                                ((StreamAllocation)o).release();
                                throw new HttpRetryException("Cannot retry streamed HTTP body", build.code());
                            }
                            finally {}
                        }
                        catch (IOException ex) {
                            if (ex instanceof ConnectionShutdownException) {
                                break Label_0465;
                            }
                            final boolean b = true;
                            if (this.recover(ex, (StreamAllocation)o, b, (Request)chain)) {
                                continue Label_0075;
                            }
                            throw ex;
                        }
                        catch (RouteException ex2) {
                            if (this.recover(ex2.getLastConnectException(), (StreamAllocation)o, false, (Request)chain)) {
                                continue Label_0075;
                            }
                            throw ex2.getLastConnectException();
                        }
                        break;
                    }
                    final boolean b = false;
                    continue;
                }
            }
            ((StreamAllocation)o).streamFailed(null);
            ((StreamAllocation)o).release();
            throw chain;
        }
        ((StreamAllocation)o).release();
        throw new IOException("Canceled");
    }
    
    public boolean isCanceled() {
        return this.canceled;
    }
    
    public void setCallStackTrace(final Object callStackTrace) {
        this.callStackTrace = callStackTrace;
    }
    
    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }
}
