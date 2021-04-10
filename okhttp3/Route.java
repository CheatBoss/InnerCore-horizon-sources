package okhttp3;

import java.net.*;
import javax.annotation.*;

public final class Route
{
    final Address address;
    final InetSocketAddress inetSocketAddress;
    final Proxy proxy;
    
    public Route(final Address address, final Proxy proxy, final InetSocketAddress inetSocketAddress) {
        if (address == null) {
            throw new NullPointerException("address == null");
        }
        if (proxy == null) {
            throw new NullPointerException("proxy == null");
        }
        if (inetSocketAddress != null) {
            this.address = address;
            this.proxy = proxy;
            this.inetSocketAddress = inetSocketAddress;
            return;
        }
        throw new NullPointerException("inetSocketAddress == null");
    }
    
    public Address address() {
        return this.address;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof Route) {
            final Route route = (Route)o;
            if (route.address.equals(this.address) && route.proxy.equals(this.proxy) && route.inetSocketAddress.equals(this.inetSocketAddress)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return ((this.address.hashCode() + 527) * 31 + this.proxy.hashCode()) * 31 + this.inetSocketAddress.hashCode();
    }
    
    public Proxy proxy() {
        return this.proxy;
    }
    
    public boolean requiresTunnel() {
        return this.address.sslSocketFactory != null && this.proxy.type() == Proxy.Type.HTTP;
    }
    
    public InetSocketAddress socketAddress() {
        return this.inetSocketAddress;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Route{");
        sb.append(this.inetSocketAddress);
        sb.append("}");
        return sb.toString();
    }
}
