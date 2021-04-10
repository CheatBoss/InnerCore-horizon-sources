package okhttp3.internal.connection;

import java.lang.ref.*;
import java.util.*;
import okhttp3.internal.platform.*;
import java.io.*;
import java.security.cert.*;
import okhttp3.internal.tls.*;
import javax.net.ssl.*;
import okhttp3.internal.http1.*;
import java.util.concurrent.*;
import okio.*;
import javax.annotation.*;
import okhttp3.internal.*;
import okhttp3.*;
import okhttp3.internal.http.*;
import java.net.*;
import okhttp3.internal.ws.*;
import okhttp3.internal.http2.*;

public final class RealConnection extends Listener implements Connection
{
    public int allocationLimit;
    public final List<Reference<StreamAllocation>> allocations;
    private final ConnectionPool connectionPool;
    private Handshake handshake;
    private Http2Connection http2Connection;
    public long idleAtNanos;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    private BufferedSink sink;
    private Socket socket;
    private BufferedSource source;
    public int successCount;
    
    public RealConnection(final ConnectionPool connectionPool, final Route route) {
        this.allocationLimit = 1;
        this.allocations = new ArrayList<Reference<StreamAllocation>>();
        this.idleAtNanos = Long.MAX_VALUE;
        this.connectionPool = connectionPool;
        this.route = route;
    }
    
    private void connectSocket(final int n, final int soTimeout, final Call call, final EventListener eventListener) throws IOException {
        final Proxy proxy = this.route.proxy();
        final Address address = this.route.address();
        Socket socket;
        if (proxy.type() != Proxy.Type.DIRECT && proxy.type() != Proxy.Type.HTTP) {
            socket = new Socket(proxy);
        }
        else {
            socket = address.socketFactory().createSocket();
        }
        this.rawSocket = socket;
        eventListener.connectStart(call, this.route.socketAddress(), proxy);
        this.rawSocket.setSoTimeout(soTimeout);
        try {
            Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), n);
            try {
                this.source = Okio.buffer(Okio.source(this.rawSocket));
                this.sink = Okio.buffer(Okio.sink(this.rawSocket));
            }
            catch (NullPointerException ex) {
                if (!"throw with null exception".equals(ex.getMessage())) {
                    return;
                }
                throw new IOException(ex);
            }
        }
        catch (ConnectException ex3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to connect to ");
            sb.append(this.route.socketAddress());
            final ConnectException ex2 = new ConnectException(sb.toString());
            ex2.initCause(ex3);
            throw ex2;
        }
    }
    
    private void connectTls(final ConnectionSpecSelector connectionSpecSelector) throws IOException {
        final Address address = this.route.address();
        final SSLSocketFactory sslSocketFactory = address.sslSocketFactory();
        Object value = null;
        ConnectionSpec configureSecureSocket = null;
        final String s = null;
        StringBuilder sb = null;
        try {
            try {
                final SSLSocket socket = (SSLSocket)sslSocketFactory.createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
                try {
                    configureSecureSocket = connectionSpecSelector.configureSecureSocket(socket);
                    if (configureSecureSocket.supportsTlsExtensions()) {
                        Platform.get().configureTlsExtensions(socket, address.url().host(), address.protocols());
                    }
                    socket.startHandshake();
                    final SSLSession session = socket.getSession();
                    if (!this.isValid(session)) {
                        throw new IOException("a valid ssl session was not established");
                    }
                    value = Handshake.get(session);
                    if (address.hostnameVerifier().verify(address.url().host(), session)) {
                        address.certificatePinner().check(address.url().host(), ((Handshake)value).peerCertificates());
                        String selectedProtocol = s;
                        if (configureSecureSocket.supportsTlsExtensions()) {
                            selectedProtocol = Platform.get().getSelectedProtocol(socket);
                        }
                        this.socket = socket;
                        this.source = Okio.buffer(Okio.source(socket));
                        this.sink = Okio.buffer(Okio.sink(this.socket));
                        this.handshake = (Handshake)value;
                        Protocol protocol;
                        if (selectedProtocol != null) {
                            protocol = Protocol.get(selectedProtocol);
                        }
                        else {
                            protocol = Protocol.HTTP_1_1;
                        }
                        this.protocol = protocol;
                        if (socket != null) {
                            Platform.get().afterHandshake(socket);
                        }
                        return;
                    }
                    final X509Certificate x509Certificate = ((Handshake)value).peerCertificates().get(0);
                    value = new StringBuilder();
                    ((StringBuilder)value).append("Hostname ");
                    ((StringBuilder)value).append(address.url().host());
                    ((StringBuilder)value).append(" not verified:\n    certificate: ");
                    ((StringBuilder)value).append(CertificatePinner.pin(x509Certificate));
                    ((StringBuilder)value).append("\n    DN: ");
                    ((StringBuilder)value).append(x509Certificate.getSubjectDN().getName());
                    ((StringBuilder)value).append("\n    subjectAltNames: ");
                    ((StringBuilder)value).append(OkHostnameVerifier.allSubjectAltNames(x509Certificate));
                    throw new SSLPeerUnverifiedException(((StringBuilder)value).toString());
                }
                catch (AssertionError value) {}
            }
            finally {
                sb = (StringBuilder)value;
            }
        }
        catch (AssertionError sb) {}
        if (Util.isAndroidGetsocknameError((AssertionError)sb)) {
            throw new IOException((Throwable)sb);
        }
        throw sb;
        if (sb != null) {
            Platform.get().afterHandshake((SSLSocket)sb);
        }
        Util.closeQuietly((Socket)sb);
    }
    
    private void connectTunnel(final int n, final int n2, final int n3, final Call call, final EventListener eventListener) throws IOException {
        Request request = this.createTunnelRequest();
        final HttpUrl url = request.url();
        for (int i = 0; i < 21; ++i) {
            this.connectSocket(n, n2, call, eventListener);
            request = this.createTunnel(n2, n3, request, url);
            if (request == null) {
                return;
            }
            Util.closeQuietly(this.rawSocket);
            this.rawSocket = null;
            this.sink = null;
            this.source = null;
            eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), null);
        }
    }
    
    private Request createTunnel(final int n, final int n2, Request authenticate, final HttpUrl httpUrl) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("CONNECT ");
        sb.append(Util.hostHeader(httpUrl, true));
        sb.append(" HTTP/1.1");
        final String string = sb.toString();
        while (true) {
            final Http1Codec http1Codec = new Http1Codec(null, null, this.source, this.sink);
            this.source.timeout().timeout(n, TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout(n2, TimeUnit.MILLISECONDS);
            http1Codec.writeRequest(authenticate.headers(), string);
            http1Codec.finishRequest();
            final Response build = http1Codec.readResponseHeaders(false).request(authenticate).build();
            long contentLength;
            if ((contentLength = HttpHeaders.contentLength(build)) == -1L) {
                contentLength = 0L;
            }
            final Source fixedLengthSource = http1Codec.newFixedLengthSource(contentLength);
            Util.skipAll(fixedLengthSource, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            fixedLengthSource.close();
            final int code = build.code();
            if (code != 200) {
                if (code != 407) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unexpected response code for CONNECT: ");
                    sb2.append(build.code());
                    throw new IOException(sb2.toString());
                }
                authenticate = this.route.address().proxyAuthenticator().authenticate(this.route, build);
                if (authenticate == null) {
                    throw new IOException("Failed to authenticate with proxy");
                }
                if ("close".equalsIgnoreCase(build.header("Connection"))) {
                    return authenticate;
                }
                continue;
            }
            else {
                if (this.source.buffer().exhausted() && this.sink.buffer().exhausted()) {
                    return null;
                }
                throw new IOException("TLS tunnel buffered too many bytes!");
            }
        }
    }
    
    private Request createTunnelRequest() {
        return new Request.Builder().url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
    }
    
    private void establishProtocol(final ConnectionSpecSelector connectionSpecSelector, final int n, final Call call, final EventListener eventListener) throws IOException {
        if (this.route.address().sslSocketFactory() == null) {
            this.protocol = Protocol.HTTP_1_1;
            this.socket = this.rawSocket;
            return;
        }
        eventListener.secureConnectStart(call);
        this.connectTls(connectionSpecSelector);
        eventListener.secureConnectEnd(call, this.handshake);
        if (this.protocol == Protocol.HTTP_2) {
            this.socket.setSoTimeout(0);
            (this.http2Connection = new Http2Connection.Builder(true).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).pingIntervalMillis(n).build()).start();
        }
    }
    
    private boolean isValid(final SSLSession sslSession) {
        return !"NONE".equals(sslSession.getProtocol()) && !"SSL_NULL_WITH_NULL_NULL".equals(sslSession.getCipherSuite());
    }
    
    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }
    
    public void connect(final int n, final int n2, final int n3, final int n4, final boolean b, final Call call, final EventListener eventListener) {
        if (this.protocol == null) {
            Object o = this.route.address().connectionSpecs();
            final ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector((List<ConnectionSpec>)o);
            if (this.route.address().sslSocketFactory() == null) {
                if (!((List)o).contains(ConnectionSpec.CLEARTEXT)) {
                    throw new RouteException(new UnknownServiceException("CLEARTEXT communication not enabled for client"));
                }
                o = this.route.address().url().host();
                if (!Platform.get().isCleartextTrafficPermitted((String)o)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("CLEARTEXT communication to ");
                    sb.append((String)o);
                    sb.append(" not permitted by network security policy");
                    throw new RouteException(new UnknownServiceException(sb.toString()));
                }
            }
            RouteException ex = null;
            do {
                try {
                    Label_0209: {
                        if (this.route.requiresTunnel()) {
                            this.connectTunnel(n, n2, n3, call, eventListener);
                            o = this.rawSocket;
                            if (o == null) {
                                break Label_0209;
                            }
                            break Label_0209;
                        }
                        try {
                            this.connectSocket(n, n2, call, eventListener);
                            try {
                                this.establishProtocol(connectionSpecSelector, n4, call, eventListener);
                                eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), this.protocol);
                                if (this.route.requiresTunnel() && this.rawSocket == null) {
                                    throw new RouteException(new ProtocolException("Too many tunnel connections attempted: 21"));
                                }
                                if (this.http2Connection != null) {
                                    synchronized (this.connectionPool) {
                                        this.allocationLimit = this.http2Connection.maxConcurrentStreams();
                                    }
                                }
                                return;
                            }
                            catch (IOException o) {}
                        }
                        catch (IOException o) {}
                    }
                }
                catch (IOException ex2) {}
                Util.closeQuietly(this.socket);
                Util.closeQuietly(this.rawSocket);
                this.socket = null;
                this.rawSocket = null;
                this.source = null;
                this.sink = null;
                this.handshake = null;
                this.protocol = null;
                this.http2Connection = null;
                eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, (IOException)o);
                if (ex == null) {
                    ex = new RouteException((IOException)o);
                }
                else {
                    ex.addConnectException((IOException)o);
                }
            } while (b && connectionSpecSelector.connectionFailed((IOException)o));
            throw ex;
        }
        throw new IllegalStateException("already connected");
    }
    
    public Handshake handshake() {
        return this.handshake;
    }
    
    public boolean isEligible(final Address address, @Nullable final Route route) {
        if (this.allocations.size() < this.allocationLimit) {
            if (this.noNewStreams) {
                return false;
            }
            if (!Internal.instance.equalsNonHost(this.route.address(), address)) {
                return false;
            }
            if (address.url().host().equals(this.route().address().url().host())) {
                return true;
            }
            if (this.http2Connection == null) {
                return false;
            }
            if (route == null) {
                return false;
            }
            if (route.proxy().type() != Proxy.Type.DIRECT) {
                return false;
            }
            if (this.route.proxy().type() != Proxy.Type.DIRECT) {
                return false;
            }
            if (!this.route.socketAddress().equals(route.socketAddress())) {
                return false;
            }
            if (route.address().hostnameVerifier() != OkHostnameVerifier.INSTANCE) {
                return false;
            }
            if (!this.supportsUrl(address.url())) {
                return false;
            }
            try {
                address.certificatePinner().check(address.url().host(), this.handshake().peerCertificates());
                return true;
            }
            catch (SSLPeerUnverifiedException ex) {}
        }
        return false;
    }
    
    public boolean isHealthy(final boolean b) {
        if (this.socket.isClosed() || this.socket.isInputShutdown()) {
            return false;
        }
        if (this.socket.isOutputShutdown()) {
            return false;
        }
        final Http2Connection http2Connection = this.http2Connection;
        if (http2Connection != null) {
            return http2Connection.isShutdown() ^ true;
        }
        if (b) {
            try {
                final int soTimeout = this.socket.getSoTimeout();
                try {
                    this.socket.setSoTimeout(1);
                    return !this.source.exhausted();
                }
                finally {
                    this.socket.setSoTimeout(soTimeout);
                }
            }
            catch (IOException ex) {
                return false;
            }
            catch (SocketTimeoutException ex2) {}
        }
        return true;
    }
    
    public boolean isMultiplexed() {
        return this.http2Connection != null;
    }
    
    public HttpCodec newCodec(final OkHttpClient okHttpClient, final Interceptor.Chain chain, final StreamAllocation streamAllocation) throws SocketException {
        if (this.http2Connection != null) {
            return new Http2Codec(okHttpClient, chain, streamAllocation, this.http2Connection);
        }
        this.socket.setSoTimeout(chain.readTimeoutMillis());
        this.source.timeout().timeout(chain.readTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.sink.timeout().timeout(chain.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
        return new Http1Codec(okHttpClient, streamAllocation, this.source, this.sink);
    }
    
    public RealWebSocket.Streams newWebSocketStreams(final StreamAllocation streamAllocation) {
        return new RealWebSocket.Streams(true, this.source, this.sink) {
            @Override
            public void close() throws IOException {
                final StreamAllocation val$streamAllocation = streamAllocation;
                val$streamAllocation.streamFinished(true, val$streamAllocation.codec(), -1L, null);
            }
        };
    }
    
    @Override
    public void onSettings(final Http2Connection http2Connection) {
        synchronized (this.connectionPool) {
            this.allocationLimit = http2Connection.maxConcurrentStreams();
        }
    }
    
    @Override
    public void onStream(final Http2Stream http2Stream) throws IOException {
        http2Stream.close(ErrorCode.REFUSED_STREAM);
    }
    
    public Route route() {
        return this.route;
    }
    
    public Socket socket() {
        return this.socket;
    }
    
    public boolean supportsUrl(final HttpUrl httpUrl) {
        if (httpUrl.port() != this.route.address().url().port()) {
            return false;
        }
        final boolean equals = httpUrl.host().equals(this.route.address().url().host());
        boolean b = true;
        if (!equals) {
            if (this.handshake != null && OkHostnameVerifier.INSTANCE.verify(httpUrl.host(), this.handshake.peerCertificates().get(0))) {
                return true;
            }
            b = false;
        }
        return b;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Connection{");
        sb.append(this.route.address().url().host());
        sb.append(":");
        sb.append(this.route.address().url().port());
        sb.append(", proxy=");
        sb.append(this.route.proxy());
        sb.append(" hostAddress=");
        sb.append(this.route.socketAddress());
        sb.append(" cipherSuite=");
        final Handshake handshake = this.handshake;
        Object cipherSuite;
        if (handshake != null) {
            cipherSuite = handshake.cipherSuite();
        }
        else {
            cipherSuite = "none";
        }
        sb.append(cipherSuite);
        sb.append(" protocol=");
        sb.append(this.protocol);
        sb.append('}');
        return sb.toString();
    }
}
