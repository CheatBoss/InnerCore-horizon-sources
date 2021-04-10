package com.google.android.gms.measurement.internal;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;

final class zzfl extends SSLSocketFactory
{
    private final SSLSocketFactory zzaum;
    
    zzfl() {
        this(HttpsURLConnection.getDefaultSSLSocketFactory());
    }
    
    private zzfl(final SSLSocketFactory zzaum) {
        this.zzaum = zzaum;
    }
    
    private final SSLSocket zza(final SSLSocket sslSocket) {
        return new zzfm(this, sslSocket);
    }
    
    @Override
    public final Socket createSocket() throws IOException {
        return this.zza((SSLSocket)this.zzaum.createSocket());
    }
    
    @Override
    public final Socket createSocket(final String s, final int n) throws IOException {
        return this.zza((SSLSocket)this.zzaum.createSocket(s, n));
    }
    
    @Override
    public final Socket createSocket(final String s, final int n, final InetAddress inetAddress, final int n2) throws IOException {
        return this.zza((SSLSocket)this.zzaum.createSocket(s, n, inetAddress, n2));
    }
    
    @Override
    public final Socket createSocket(final InetAddress inetAddress, final int n) throws IOException {
        return this.zza((SSLSocket)this.zzaum.createSocket(inetAddress, n));
    }
    
    @Override
    public final Socket createSocket(final InetAddress inetAddress, final int n, final InetAddress inetAddress2, final int n2) throws IOException {
        return this.zza((SSLSocket)this.zzaum.createSocket(inetAddress, n, inetAddress2, n2));
    }
    
    @Override
    public final Socket createSocket(final Socket socket, final String s, final int n, final boolean b) throws IOException {
        return this.zza((SSLSocket)this.zzaum.createSocket(socket, s, n, b));
    }
    
    @Override
    public final String[] getDefaultCipherSuites() {
        return this.zzaum.getDefaultCipherSuites();
    }
    
    @Override
    public final String[] getSupportedCipherSuites() {
        return this.zzaum.getSupportedCipherSuites();
    }
}
