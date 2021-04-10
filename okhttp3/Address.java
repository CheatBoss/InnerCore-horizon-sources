package okhttp3;

import javax.annotation.*;
import java.util.*;
import java.net.*;
import javax.net.*;
import javax.net.ssl.*;
import okhttp3.internal.*;

public final class Address
{
    @Nullable
    final CertificatePinner certificatePinner;
    final List<ConnectionSpec> connectionSpecs;
    final Dns dns;
    @Nullable
    final HostnameVerifier hostnameVerifier;
    final List<Protocol> protocols;
    @Nullable
    final Proxy proxy;
    final Authenticator proxyAuthenticator;
    final ProxySelector proxySelector;
    final SocketFactory socketFactory;
    @Nullable
    final SSLSocketFactory sslSocketFactory;
    final HttpUrl url;
    
    public Address(final String s, final int n, final Dns dns, final SocketFactory socketFactory, @Nullable final SSLSocketFactory sslSocketFactory, @Nullable final HostnameVerifier hostnameVerifier, @Nullable final CertificatePinner certificatePinner, final Authenticator proxyAuthenticator, @Nullable final Proxy proxy, final List<Protocol> list, final List<ConnectionSpec> list2, final ProxySelector proxySelector) {
        final HttpUrl.Builder builder = new HttpUrl.Builder();
        String s2;
        if (sslSocketFactory != null) {
            s2 = "https";
        }
        else {
            s2 = "http";
        }
        this.url = builder.scheme(s2).host(s).port(n).build();
        if (dns == null) {
            throw new NullPointerException("dns == null");
        }
        this.dns = dns;
        if (socketFactory == null) {
            throw new NullPointerException("socketFactory == null");
        }
        this.socketFactory = socketFactory;
        if (proxyAuthenticator == null) {
            throw new NullPointerException("proxyAuthenticator == null");
        }
        this.proxyAuthenticator = proxyAuthenticator;
        if (list == null) {
            throw new NullPointerException("protocols == null");
        }
        this.protocols = Util.immutableList(list);
        if (list2 == null) {
            throw new NullPointerException("connectionSpecs == null");
        }
        this.connectionSpecs = Util.immutableList(list2);
        if (proxySelector != null) {
            this.proxySelector = proxySelector;
            this.proxy = proxy;
            this.sslSocketFactory = sslSocketFactory;
            this.hostnameVerifier = hostnameVerifier;
            this.certificatePinner = certificatePinner;
            return;
        }
        throw new NullPointerException("proxySelector == null");
    }
    
    @Nullable
    public CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }
    
    public List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }
    
    public Dns dns() {
        return this.dns;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof Address) {
            final HttpUrl url = this.url;
            final Address address = (Address)o;
            if (url.equals(address.url) && this.equalsNonHost(address)) {
                return true;
            }
        }
        return false;
    }
    
    boolean equalsNonHost(final Address address) {
        return this.dns.equals(address.dns) && this.proxyAuthenticator.equals(address.proxyAuthenticator) && this.protocols.equals(address.protocols) && this.connectionSpecs.equals(address.connectionSpecs) && this.proxySelector.equals(address.proxySelector) && Util.equal(this.proxy, address.proxy) && Util.equal(this.sslSocketFactory, address.sslSocketFactory) && Util.equal(this.hostnameVerifier, address.hostnameVerifier) && Util.equal(this.certificatePinner, address.certificatePinner) && this.url().port() == address.url().port();
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.url.hashCode();
        final int hashCode2 = this.dns.hashCode();
        final int hashCode3 = this.proxyAuthenticator.hashCode();
        final int hashCode4 = this.protocols.hashCode();
        final int hashCode5 = this.connectionSpecs.hashCode();
        final int hashCode6 = this.proxySelector.hashCode();
        final Proxy proxy = this.proxy;
        int hashCode7 = 0;
        int hashCode8;
        if (proxy != null) {
            hashCode8 = proxy.hashCode();
        }
        else {
            hashCode8 = 0;
        }
        final SSLSocketFactory sslSocketFactory = this.sslSocketFactory;
        int hashCode9;
        if (sslSocketFactory != null) {
            hashCode9 = sslSocketFactory.hashCode();
        }
        else {
            hashCode9 = 0;
        }
        final HostnameVerifier hostnameVerifier = this.hostnameVerifier;
        int hashCode10;
        if (hostnameVerifier != null) {
            hashCode10 = hostnameVerifier.hashCode();
        }
        else {
            hashCode10 = 0;
        }
        final CertificatePinner certificatePinner = this.certificatePinner;
        if (certificatePinner != null) {
            hashCode7 = certificatePinner.hashCode();
        }
        return (((((((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode5) * 31 + hashCode6) * 31 + hashCode8) * 31 + hashCode9) * 31 + hashCode10) * 31 + hashCode7;
    }
    
    @Nullable
    public HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }
    
    public List<Protocol> protocols() {
        return this.protocols;
    }
    
    @Nullable
    public Proxy proxy() {
        return this.proxy;
    }
    
    public Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }
    
    public ProxySelector proxySelector() {
        return this.proxySelector;
    }
    
    public SocketFactory socketFactory() {
        return this.socketFactory;
    }
    
    @Nullable
    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Address{");
        sb.append(this.url.host());
        sb.append(":");
        sb.append(this.url.port());
        Object o;
        if (this.proxy != null) {
            sb.append(", proxy=");
            o = this.proxy;
        }
        else {
            sb.append(", proxySelector=");
            o = this.proxySelector;
        }
        sb.append(o);
        sb.append("}");
        return sb.toString();
    }
    
    public HttpUrl url() {
        return this.url;
    }
}
