package com.microsoft.xbox.toolkit.network;

import org.apache.http.conn.*;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.*;
import org.apache.http.client.params.*;
import org.apache.http.*;
import org.apache.http.params.*;
import org.apache.http.conn.params.*;
import org.apache.http.impl.conn.tsccm.*;

public class HttpClientFactory
{
    private static final int CONNECTION_PER_ROUTE = 16;
    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 40;
    private static final int MAX_TOTAL_CONNECTIONS = 32;
    public static HttpClientFactory networkOperationsFactory;
    public static HttpClientFactory noRedirectNetworkOperationsFactory;
    public static HttpClientFactory textureFactory;
    private AbstractXLEHttpClient client;
    private AbstractXLEHttpClient clientWithTimeoutOverride;
    private ClientConnectionManager connectionManager;
    private Object httpSyncObject;
    private HttpParams params;
    
    static {
        HttpClientFactory.networkOperationsFactory = new HttpClientFactory();
        HttpClientFactory.noRedirectNetworkOperationsFactory = new HttpClientFactory(false);
        HttpClientFactory.textureFactory = new HttpClientFactory(true);
    }
    
    public HttpClientFactory() {
        this(false);
    }
    
    public HttpClientFactory(final boolean b) {
        this.connectionManager = null;
        this.httpSyncObject = new Object();
        this.client = null;
        this.clientWithTimeoutOverride = null;
        this.params = (HttpParams)new BasicHttpParams();
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", (SocketFactory)PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", (SocketFactory)SSLSocketFactory.getSocketFactory(), 443));
        HttpProtocolParams.setVersion(this.params, (ProtocolVersion)HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(this.params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(this.params, false);
        HttpClientParams.setRedirecting(this.params, b);
        if (XboxLiveEnvironment.Instance().getProxyEnabled()) {
            this.params.setParameter("http.route.default-proxy", (Object)new HttpHost("itgproxy.redmond.corp.microsoft.com", 80));
        }
        HttpConnectionParams.setConnectionTimeout(this.params, 40000);
        HttpConnectionParams.setSoTimeout(this.params, 40000);
        HttpConnectionParams.setSocketBufferSize(this.params, 8192);
        ConnManagerParams.setMaxConnectionsPerRoute(this.params, (ConnPerRoute)new ConnPerRouteBean(16));
        ConnManagerParams.setMaxTotalConnections(this.params, 32);
        this.connectionManager = (ClientConnectionManager)new ThreadSafeClientConnManager(this.params, schemeRegistry);
    }
    
    public ClientConnectionManager getClientConnectionManager() {
        return this.connectionManager;
    }
    
    public AbstractXLEHttpClient getHttpClient(int n) {
        final Object httpSyncObject = this.httpSyncObject;
        // monitorenter(httpSyncObject)
        Label_0070: {
            if (n <= 0) {
                break Label_0070;
            }
            while (true) {
                try {
                    if (this.clientWithTimeoutOverride == null) {
                        final HttpParams copy = this.params.copy();
                        n *= 1000;
                        HttpConnectionParams.setConnectionTimeout(copy, n);
                        HttpConnectionParams.setSoTimeout(copy, n);
                        // monitorexit(httpSyncObject)
                        return new XLEHttpClient(this.connectionManager, copy);
                    }
                    // monitorexit(httpSyncObject)
                    return this.clientWithTimeoutOverride;
                    // monitorexit(httpSyncObject)
                    throw;
                    // iftrue(Label_0096:, this.client != null)
                    // monitorexit(httpSyncObject)
                    while (true) {
                        Block_4: {
                            break Block_4;
                            return this.client;
                        }
                        this.client = new XLEHttpClient(this.connectionManager, this.params);
                        continue;
                    }
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public HttpParams getHttpParams() {
        return this.params;
    }
    
    public void setHttpClient(final AbstractXLEHttpClient abstractXLEHttpClient) {
    }
}
