package okhttp3;

import javax.annotation.*;
import okhttp3.internal.cache.*;
import javax.net.*;
import okhttp3.internal.*;
import java.net.*;
import okhttp3.internal.connection.*;
import okhttp3.internal.platform.*;
import javax.net.ssl.*;
import java.security.*;
import okhttp3.internal.ws.*;
import okhttp3.internal.tls.*;
import java.util.concurrent.*;
import java.util.*;

public class OkHttpClient implements Cloneable, Factory
{
    static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS;
    static final List<Protocol> DEFAULT_PROTOCOLS;
    final Authenticator authenticator;
    @Nullable
    final Cache cache;
    @Nullable
    final CertificateChainCleaner certificateChainCleaner;
    final CertificatePinner certificatePinner;
    final int connectTimeout;
    final ConnectionPool connectionPool;
    final List<ConnectionSpec> connectionSpecs;
    final CookieJar cookieJar;
    final Dispatcher dispatcher;
    final Dns dns;
    final EventListener.Factory eventListenerFactory;
    final boolean followRedirects;
    final boolean followSslRedirects;
    final HostnameVerifier hostnameVerifier;
    final List<Interceptor> interceptors;
    @Nullable
    final InternalCache internalCache;
    final List<Interceptor> networkInterceptors;
    final int pingInterval;
    final List<Protocol> protocols;
    @Nullable
    final Proxy proxy;
    final Authenticator proxyAuthenticator;
    final ProxySelector proxySelector;
    final int readTimeout;
    final boolean retryOnConnectionFailure;
    final SocketFactory socketFactory;
    @Nullable
    final SSLSocketFactory sslSocketFactory;
    final int writeTimeout;
    
    static {
        DEFAULT_PROTOCOLS = Util.immutableList(Protocol.HTTP_2, Protocol.HTTP_1_1);
        DEFAULT_CONNECTION_SPECS = Util.immutableList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT);
        Internal.instance = new Internal() {
            @Override
            public void addLenient(final Headers.Builder builder, final String s) {
                builder.addLenient(s);
            }
            
            @Override
            public void addLenient(final Headers.Builder builder, final String s, final String s2) {
                builder.addLenient(s, s2);
            }
            
            @Override
            public void apply(final ConnectionSpec connectionSpec, final SSLSocket sslSocket, final boolean b) {
                connectionSpec.apply(sslSocket, b);
            }
            
            @Override
            public int code(final Response.Builder builder) {
                return builder.code;
            }
            
            @Override
            public boolean connectionBecameIdle(final ConnectionPool connectionPool, final RealConnection realConnection) {
                return connectionPool.connectionBecameIdle(realConnection);
            }
            
            @Override
            public Socket deduplicate(final ConnectionPool connectionPool, final Address address, final StreamAllocation streamAllocation) {
                return connectionPool.deduplicate(address, streamAllocation);
            }
            
            @Override
            public boolean equalsNonHost(final Address address, final Address address2) {
                return address.equalsNonHost(address2);
            }
            
            @Override
            public RealConnection get(final ConnectionPool connectionPool, final Address address, final StreamAllocation streamAllocation, final Route route) {
                return connectionPool.get(address, streamAllocation, route);
            }
            
            @Override
            public HttpUrl getHttpUrlChecked(final String s) throws MalformedURLException, UnknownHostException {
                return HttpUrl.getChecked(s);
            }
            
            @Override
            public Call newWebSocketCall(final OkHttpClient okHttpClient, final Request request) {
                return RealCall.newRealCall(okHttpClient, request, true);
            }
            
            @Override
            public void put(final ConnectionPool connectionPool, final RealConnection realConnection) {
                connectionPool.put(realConnection);
            }
            
            @Override
            public RouteDatabase routeDatabase(final ConnectionPool connectionPool) {
                return connectionPool.routeDatabase;
            }
            
            @Override
            public void setCache(final Builder builder, final InternalCache internalCache) {
                builder.setInternalCache(internalCache);
            }
            
            @Override
            public StreamAllocation streamAllocation(final Call call) {
                return ((RealCall)call).streamAllocation();
            }
        };
    }
    
    public OkHttpClient() {
        this(new Builder());
    }
    
    OkHttpClient(final Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.proxy = builder.proxy;
        this.protocols = builder.protocols;
        this.connectionSpecs = builder.connectionSpecs;
        this.interceptors = Util.immutableList(builder.interceptors);
        this.networkInterceptors = Util.immutableList(builder.networkInterceptors);
        this.eventListenerFactory = builder.eventListenerFactory;
        this.proxySelector = builder.proxySelector;
        this.cookieJar = builder.cookieJar;
        this.cache = builder.cache;
        this.internalCache = builder.internalCache;
        this.socketFactory = builder.socketFactory;
        final Iterator<ConnectionSpec> iterator = this.connectionSpecs.iterator();
        int n = 0;
    Label_0116:
        while (true) {
            n = 0;
            while (iterator.hasNext()) {
                final ConnectionSpec connectionSpec = iterator.next();
                if (n == 0 && !connectionSpec.isTls()) {
                    continue Label_0116;
                }
                n = 1;
            }
            break;
        }
        CertificateChainCleaner certificateChainCleaner;
        if (builder.sslSocketFactory == null && n != 0) {
            final X509TrustManager systemDefaultTrustManager = this.systemDefaultTrustManager();
            this.sslSocketFactory = this.systemDefaultSslSocketFactory(systemDefaultTrustManager);
            certificateChainCleaner = CertificateChainCleaner.get(systemDefaultTrustManager);
        }
        else {
            this.sslSocketFactory = builder.sslSocketFactory;
            certificateChainCleaner = builder.certificateChainCleaner;
        }
        this.certificateChainCleaner = certificateChainCleaner;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.certificatePinner = builder.certificatePinner.withCertificateChainCleaner(this.certificateChainCleaner);
        this.proxyAuthenticator = builder.proxyAuthenticator;
        this.authenticator = builder.authenticator;
        this.connectionPool = builder.connectionPool;
        this.dns = builder.dns;
        this.followSslRedirects = builder.followSslRedirects;
        this.followRedirects = builder.followRedirects;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
        if (this.interceptors.contains(null)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Null interceptor: ");
            sb.append(this.interceptors);
            throw new IllegalStateException(sb.toString());
        }
        if (!this.networkInterceptors.contains(null)) {
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Null network interceptor: ");
        sb2.append(this.networkInterceptors);
        throw new IllegalStateException(sb2.toString());
    }
    
    private SSLSocketFactory systemDefaultSslSocketFactory(final X509TrustManager x509TrustManager) {
        try {
            final SSLContext sslContext = Platform.get().getSSLContext();
            sslContext.init(null, new TrustManager[] { x509TrustManager }, null);
            return sslContext.getSocketFactory();
        }
        catch (GeneralSecurityException ex) {
            throw Util.assertionError("No System TLS", ex);
        }
    }
    
    private X509TrustManager systemDefaultTrustManager() {
        try {
            final TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance.init((KeyStore)null);
            final TrustManager[] trustManagers = instance.getTrustManagers();
            if (trustManagers.length == 1 && trustManagers[0] instanceof X509TrustManager) {
                return (X509TrustManager)trustManagers[0];
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected default trust managers:");
            sb.append(Arrays.toString(trustManagers));
            throw new IllegalStateException(sb.toString());
        }
        catch (GeneralSecurityException ex) {
            throw Util.assertionError("No System TLS", ex);
        }
    }
    
    public Authenticator authenticator() {
        return this.authenticator;
    }
    
    public Cache cache() {
        return this.cache;
    }
    
    public CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }
    
    public int connectTimeoutMillis() {
        return this.connectTimeout;
    }
    
    public ConnectionPool connectionPool() {
        return this.connectionPool;
    }
    
    public List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }
    
    public CookieJar cookieJar() {
        return this.cookieJar;
    }
    
    public Dispatcher dispatcher() {
        return this.dispatcher;
    }
    
    public Dns dns() {
        return this.dns;
    }
    
    public EventListener.Factory eventListenerFactory() {
        return this.eventListenerFactory;
    }
    
    public boolean followRedirects() {
        return this.followRedirects;
    }
    
    public boolean followSslRedirects() {
        return this.followSslRedirects;
    }
    
    public HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }
    
    public List<Interceptor> interceptors() {
        return this.interceptors;
    }
    
    InternalCache internalCache() {
        final Cache cache = this.cache;
        if (cache != null) {
            return cache.internalCache;
        }
        return this.internalCache;
    }
    
    public List<Interceptor> networkInterceptors() {
        return this.networkInterceptors;
    }
    
    public Builder newBuilder() {
        return new Builder(this);
    }
    
    @Override
    public Call newCall(final Request request) {
        return RealCall.newRealCall(this, request, false);
    }
    
    public WebSocket newWebSocket(final Request request, final WebSocketListener webSocketListener) {
        final RealWebSocket realWebSocket = new RealWebSocket(request, webSocketListener, new Random(), this.pingInterval);
        realWebSocket.connect(this);
        return realWebSocket;
    }
    
    public int pingIntervalMillis() {
        return this.pingInterval;
    }
    
    public List<Protocol> protocols() {
        return this.protocols;
    }
    
    public Proxy proxy() {
        return this.proxy;
    }
    
    public Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }
    
    public ProxySelector proxySelector() {
        return this.proxySelector;
    }
    
    public int readTimeoutMillis() {
        return this.readTimeout;
    }
    
    public boolean retryOnConnectionFailure() {
        return this.retryOnConnectionFailure;
    }
    
    public SocketFactory socketFactory() {
        return this.socketFactory;
    }
    
    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }
    
    public int writeTimeoutMillis() {
        return this.writeTimeout;
    }
    
    public static final class Builder
    {
        Authenticator authenticator;
        @Nullable
        Cache cache;
        @Nullable
        CertificateChainCleaner certificateChainCleaner;
        CertificatePinner certificatePinner;
        int connectTimeout;
        ConnectionPool connectionPool;
        List<ConnectionSpec> connectionSpecs;
        CookieJar cookieJar;
        Dispatcher dispatcher;
        Dns dns;
        EventListener.Factory eventListenerFactory;
        boolean followRedirects;
        boolean followSslRedirects;
        HostnameVerifier hostnameVerifier;
        final List<Interceptor> interceptors;
        @Nullable
        InternalCache internalCache;
        final List<Interceptor> networkInterceptors;
        int pingInterval;
        List<Protocol> protocols;
        @Nullable
        Proxy proxy;
        Authenticator proxyAuthenticator;
        ProxySelector proxySelector;
        int readTimeout;
        boolean retryOnConnectionFailure;
        SocketFactory socketFactory;
        @Nullable
        SSLSocketFactory sslSocketFactory;
        int writeTimeout;
        
        public Builder() {
            this.interceptors = new ArrayList<Interceptor>();
            this.networkInterceptors = new ArrayList<Interceptor>();
            this.dispatcher = new Dispatcher();
            this.protocols = OkHttpClient.DEFAULT_PROTOCOLS;
            this.connectionSpecs = OkHttpClient.DEFAULT_CONNECTION_SPECS;
            this.eventListenerFactory = EventListener.factory(EventListener.NONE);
            this.proxySelector = ProxySelector.getDefault();
            this.cookieJar = CookieJar.NO_COOKIES;
            this.socketFactory = SocketFactory.getDefault();
            this.hostnameVerifier = OkHostnameVerifier.INSTANCE;
            this.certificatePinner = CertificatePinner.DEFAULT;
            this.proxyAuthenticator = Authenticator.NONE;
            this.authenticator = Authenticator.NONE;
            this.connectionPool = new ConnectionPool();
            this.dns = Dns.SYSTEM;
            this.followSslRedirects = true;
            this.followRedirects = true;
            this.retryOnConnectionFailure = true;
            this.connectTimeout = 10000;
            this.readTimeout = 10000;
            this.writeTimeout = 10000;
            this.pingInterval = 0;
        }
        
        Builder(final OkHttpClient okHttpClient) {
            this.interceptors = new ArrayList<Interceptor>();
            this.networkInterceptors = new ArrayList<Interceptor>();
            this.dispatcher = okHttpClient.dispatcher;
            this.proxy = okHttpClient.proxy;
            this.protocols = okHttpClient.protocols;
            this.connectionSpecs = okHttpClient.connectionSpecs;
            this.interceptors.addAll(okHttpClient.interceptors);
            this.networkInterceptors.addAll(okHttpClient.networkInterceptors);
            this.eventListenerFactory = okHttpClient.eventListenerFactory;
            this.proxySelector = okHttpClient.proxySelector;
            this.cookieJar = okHttpClient.cookieJar;
            this.internalCache = okHttpClient.internalCache;
            this.cache = okHttpClient.cache;
            this.socketFactory = okHttpClient.socketFactory;
            this.sslSocketFactory = okHttpClient.sslSocketFactory;
            this.certificateChainCleaner = okHttpClient.certificateChainCleaner;
            this.hostnameVerifier = okHttpClient.hostnameVerifier;
            this.certificatePinner = okHttpClient.certificatePinner;
            this.proxyAuthenticator = okHttpClient.proxyAuthenticator;
            this.authenticator = okHttpClient.authenticator;
            this.connectionPool = okHttpClient.connectionPool;
            this.dns = okHttpClient.dns;
            this.followSslRedirects = okHttpClient.followSslRedirects;
            this.followRedirects = okHttpClient.followRedirects;
            this.retryOnConnectionFailure = okHttpClient.retryOnConnectionFailure;
            this.connectTimeout = okHttpClient.connectTimeout;
            this.readTimeout = okHttpClient.readTimeout;
            this.writeTimeout = okHttpClient.writeTimeout;
            this.pingInterval = okHttpClient.pingInterval;
        }
        
        public Builder addInterceptor(final Interceptor interceptor) {
            if (interceptor != null) {
                this.interceptors.add(interceptor);
                return this;
            }
            throw new IllegalArgumentException("interceptor == null");
        }
        
        public Builder addNetworkInterceptor(final Interceptor interceptor) {
            if (interceptor != null) {
                this.networkInterceptors.add(interceptor);
                return this;
            }
            throw new IllegalArgumentException("interceptor == null");
        }
        
        public Builder authenticator(final Authenticator authenticator) {
            if (authenticator != null) {
                this.authenticator = authenticator;
                return this;
            }
            throw new NullPointerException("authenticator == null");
        }
        
        public OkHttpClient build() {
            return new OkHttpClient(this);
        }
        
        public Builder cache(@Nullable final Cache cache) {
            this.cache = cache;
            this.internalCache = null;
            return this;
        }
        
        public Builder certificatePinner(final CertificatePinner certificatePinner) {
            if (certificatePinner != null) {
                this.certificatePinner = certificatePinner;
                return this;
            }
            throw new NullPointerException("certificatePinner == null");
        }
        
        public Builder connectTimeout(final long n, final TimeUnit timeUnit) {
            this.connectTimeout = Util.checkDuration("timeout", n, timeUnit);
            return this;
        }
        
        public Builder connectionPool(final ConnectionPool connectionPool) {
            if (connectionPool != null) {
                this.connectionPool = connectionPool;
                return this;
            }
            throw new NullPointerException("connectionPool == null");
        }
        
        public Builder connectionSpecs(final List<ConnectionSpec> list) {
            this.connectionSpecs = Util.immutableList(list);
            return this;
        }
        
        public Builder cookieJar(final CookieJar cookieJar) {
            if (cookieJar != null) {
                this.cookieJar = cookieJar;
                return this;
            }
            throw new NullPointerException("cookieJar == null");
        }
        
        public Builder dispatcher(final Dispatcher dispatcher) {
            if (dispatcher != null) {
                this.dispatcher = dispatcher;
                return this;
            }
            throw new IllegalArgumentException("dispatcher == null");
        }
        
        public Builder dns(final Dns dns) {
            if (dns != null) {
                this.dns = dns;
                return this;
            }
            throw new NullPointerException("dns == null");
        }
        
        public Builder eventListener(final EventListener eventListener) {
            if (eventListener != null) {
                this.eventListenerFactory = EventListener.factory(eventListener);
                return this;
            }
            throw new NullPointerException("eventListener == null");
        }
        
        public Builder eventListenerFactory(final EventListener.Factory eventListenerFactory) {
            if (eventListenerFactory != null) {
                this.eventListenerFactory = eventListenerFactory;
                return this;
            }
            throw new NullPointerException("eventListenerFactory == null");
        }
        
        public Builder followRedirects(final boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }
        
        public Builder followSslRedirects(final boolean followSslRedirects) {
            this.followSslRedirects = followSslRedirects;
            return this;
        }
        
        public Builder hostnameVerifier(final HostnameVerifier hostnameVerifier) {
            if (hostnameVerifier != null) {
                this.hostnameVerifier = hostnameVerifier;
                return this;
            }
            throw new NullPointerException("hostnameVerifier == null");
        }
        
        public List<Interceptor> interceptors() {
            return this.interceptors;
        }
        
        public List<Interceptor> networkInterceptors() {
            return this.networkInterceptors;
        }
        
        public Builder pingInterval(final long n, final TimeUnit timeUnit) {
            this.pingInterval = Util.checkDuration("interval", n, timeUnit);
            return this;
        }
        
        public Builder protocols(final List<Protocol> list) {
            final ArrayList list2 = new ArrayList<Protocol>(list);
            if (!list2.contains(Protocol.HTTP_1_1)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("protocols doesn't contain http/1.1: ");
                sb.append(list2);
                throw new IllegalArgumentException(sb.toString());
            }
            if (list2.contains(Protocol.HTTP_1_0)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("protocols must not contain http/1.0: ");
                sb2.append(list2);
                throw new IllegalArgumentException(sb2.toString());
            }
            if (!list2.contains(null)) {
                list2.remove(Protocol.SPDY_3);
                this.protocols = Collections.unmodifiableList((List<? extends Protocol>)list2);
                return this;
            }
            throw new IllegalArgumentException("protocols must not contain null");
        }
        
        public Builder proxy(@Nullable final Proxy proxy) {
            this.proxy = proxy;
            return this;
        }
        
        public Builder proxyAuthenticator(final Authenticator proxyAuthenticator) {
            if (proxyAuthenticator != null) {
                this.proxyAuthenticator = proxyAuthenticator;
                return this;
            }
            throw new NullPointerException("proxyAuthenticator == null");
        }
        
        public Builder proxySelector(final ProxySelector proxySelector) {
            this.proxySelector = proxySelector;
            return this;
        }
        
        public Builder readTimeout(final long n, final TimeUnit timeUnit) {
            this.readTimeout = Util.checkDuration("timeout", n, timeUnit);
            return this;
        }
        
        public Builder retryOnConnectionFailure(final boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }
        
        void setInternalCache(@Nullable final InternalCache internalCache) {
            this.internalCache = internalCache;
            this.cache = null;
        }
        
        public Builder socketFactory(final SocketFactory socketFactory) {
            if (socketFactory != null) {
                this.socketFactory = socketFactory;
                return this;
            }
            throw new NullPointerException("socketFactory == null");
        }
        
        public Builder sslSocketFactory(final SSLSocketFactory sslSocketFactory) {
            if (sslSocketFactory != null) {
                this.sslSocketFactory = sslSocketFactory;
                this.certificateChainCleaner = Platform.get().buildCertificateChainCleaner(sslSocketFactory);
                return this;
            }
            throw new NullPointerException("sslSocketFactory == null");
        }
        
        public Builder sslSocketFactory(final SSLSocketFactory sslSocketFactory, final X509TrustManager x509TrustManager) {
            if (sslSocketFactory == null) {
                throw new NullPointerException("sslSocketFactory == null");
            }
            if (x509TrustManager != null) {
                this.sslSocketFactory = sslSocketFactory;
                this.certificateChainCleaner = CertificateChainCleaner.get(x509TrustManager);
                return this;
            }
            throw new NullPointerException("trustManager == null");
        }
        
        public Builder writeTimeout(final long n, final TimeUnit timeUnit) {
            this.writeTimeout = Util.checkDuration("timeout", n, timeUnit);
            return this;
        }
    }
}
