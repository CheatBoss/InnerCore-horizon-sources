package okhttp3.internal.connection;

import java.io.*;
import java.net.*;
import okhttp3.*;
import okhttp3.internal.*;
import java.util.*;

public final class RouteSelector
{
    private final Address address;
    private final Call call;
    private final EventListener eventListener;
    private List<InetSocketAddress> inetSocketAddresses;
    private int nextProxyIndex;
    private final List<Route> postponedRoutes;
    private List<Proxy> proxies;
    private final RouteDatabase routeDatabase;
    
    public RouteSelector(final Address address, final RouteDatabase routeDatabase, final Call call, final EventListener eventListener) {
        this.proxies = Collections.emptyList();
        this.inetSocketAddresses = Collections.emptyList();
        this.postponedRoutes = new ArrayList<Route>();
        this.address = address;
        this.routeDatabase = routeDatabase;
        this.call = call;
        this.eventListener = eventListener;
        this.resetNextProxy(address.url(), address.proxy());
    }
    
    static String getHostString(final InetSocketAddress inetSocketAddress) {
        final InetAddress address = inetSocketAddress.getAddress();
        if (address == null) {
            return inetSocketAddress.getHostName();
        }
        return address.getHostAddress();
    }
    
    private boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }
    
    private Proxy nextProxy() throws IOException {
        if (this.hasNextProxy()) {
            final Proxy proxy = this.proxies.get(this.nextProxyIndex++);
            this.resetNextInetSocketAddress(proxy);
            return proxy;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("No route to ");
        sb.append(this.address.url().host());
        sb.append("; exhausted proxy configurations: ");
        sb.append(this.proxies);
        throw new SocketException(sb.toString());
    }
    
    private void resetNextInetSocketAddress(final Proxy proxy) throws IOException {
        this.inetSocketAddresses = new ArrayList<InetSocketAddress>();
        String s;
        int n;
        if (proxy.type() != Proxy.Type.DIRECT && proxy.type() != Proxy.Type.SOCKS) {
            final SocketAddress address = proxy.address();
            if (!(address instanceof InetSocketAddress)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Proxy.address() is not an InetSocketAddress: ");
                sb.append(((InetSocketAddress)address).getClass());
                throw new IllegalArgumentException(sb.toString());
            }
            final InetSocketAddress inetSocketAddress = (InetSocketAddress)address;
            s = getHostString(inetSocketAddress);
            n = inetSocketAddress.getPort();
        }
        else {
            s = this.address.url().host();
            n = this.address.url().port();
        }
        if (n < 1 || n > 65535) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("No route to ");
            sb2.append(s);
            sb2.append(":");
            sb2.append(n);
            sb2.append("; port is out of range");
            throw new SocketException(sb2.toString());
        }
        if (proxy.type() == Proxy.Type.SOCKS) {
            this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(s, n));
            return;
        }
        this.eventListener.dnsStart(this.call, s);
        final List<InetAddress> lookup = this.address.dns().lookup(s);
        if (!lookup.isEmpty()) {
            this.eventListener.dnsEnd(this.call, s, lookup);
            for (int i = 0; i < lookup.size(); ++i) {
                this.inetSocketAddresses.add(new InetSocketAddress(lookup.get(i), n));
            }
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(this.address.dns());
        sb3.append(" returned no addresses for ");
        sb3.append(s);
        throw new UnknownHostException(sb3.toString());
    }
    
    private void resetNextProxy(final HttpUrl httpUrl, final Proxy proxy) {
        List<Proxy> proxies;
        if (proxy != null) {
            proxies = Collections.singletonList(proxy);
        }
        else {
            final List<Proxy> select = this.address.proxySelector().select(httpUrl.uri());
            if (select != null && !select.isEmpty()) {
                proxies = Util.immutableList(select);
            }
            else {
                proxies = Util.immutableList(Proxy.NO_PROXY);
            }
        }
        this.proxies = proxies;
        this.nextProxyIndex = 0;
    }
    
    public void connectFailed(final Route route, final IOException ex) {
        if (route.proxy().type() != Proxy.Type.DIRECT && this.address.proxySelector() != null) {
            this.address.proxySelector().connectFailed(this.address.url().uri(), route.proxy().address(), ex);
        }
        this.routeDatabase.failed(route);
    }
    
    public boolean hasNext() {
        return this.hasNextProxy() || !this.postponedRoutes.isEmpty();
    }
    
    public Selection next() throws IOException {
        if (this.hasNext()) {
            final ArrayList list = new ArrayList<Object>();
            while (this.hasNextProxy()) {
                final Proxy nextProxy = this.nextProxy();
                for (int i = 0; i < this.inetSocketAddresses.size(); ++i) {
                    final Route route = new Route(this.address, nextProxy, this.inetSocketAddresses.get(i));
                    if (this.routeDatabase.shouldPostpone(route)) {
                        this.postponedRoutes.add(route);
                    }
                    else {
                        list.add(route);
                    }
                }
                if (!list.isEmpty()) {
                    break;
                }
            }
            if (list.isEmpty()) {
                list.addAll(this.postponedRoutes);
                this.postponedRoutes.clear();
            }
            return new Selection(list);
        }
        throw new NoSuchElementException();
    }
    
    public static final class Selection
    {
        private int nextRouteIndex;
        private final List<Route> routes;
        
        Selection(final List<Route> routes) {
            this.nextRouteIndex = 0;
            this.routes = routes;
        }
        
        public List<Route> getAll() {
            return new ArrayList<Route>(this.routes);
        }
        
        public boolean hasNext() {
            return this.nextRouteIndex < this.routes.size();
        }
        
        public Route next() {
            if (this.hasNext()) {
                return this.routes.get(this.nextRouteIndex++);
            }
            throw new NoSuchElementException();
        }
    }
}
