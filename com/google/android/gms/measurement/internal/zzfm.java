package com.google.android.gms.measurement.internal;

import java.nio.channels.*;
import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import java.util.*;

final class zzfm extends SSLSocket
{
    private final SSLSocket zzaun;
    
    zzfm(final zzfl zzfl, final SSLSocket zzaun) {
        this.zzaun = zzaun;
    }
    
    @Override
    public final void addHandshakeCompletedListener(final HandshakeCompletedListener handshakeCompletedListener) {
        this.zzaun.addHandshakeCompletedListener(handshakeCompletedListener);
    }
    
    @Override
    public final void bind(final SocketAddress socketAddress) throws IOException {
        this.zzaun.bind(socketAddress);
    }
    
    @Override
    public final void close() throws IOException {
        synchronized (this) {
            this.zzaun.close();
        }
    }
    
    @Override
    public final void connect(final SocketAddress socketAddress) throws IOException {
        this.zzaun.connect(socketAddress);
    }
    
    @Override
    public final void connect(final SocketAddress socketAddress, final int n) throws IOException {
        this.zzaun.connect(socketAddress, n);
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this.zzaun.equals(o);
    }
    
    @Override
    public final SocketChannel getChannel() {
        return this.zzaun.getChannel();
    }
    
    @Override
    public final boolean getEnableSessionCreation() {
        return this.zzaun.getEnableSessionCreation();
    }
    
    @Override
    public final String[] getEnabledCipherSuites() {
        return this.zzaun.getEnabledCipherSuites();
    }
    
    @Override
    public final String[] getEnabledProtocols() {
        return this.zzaun.getEnabledProtocols();
    }
    
    @Override
    public final InetAddress getInetAddress() {
        return this.zzaun.getInetAddress();
    }
    
    @Override
    public final InputStream getInputStream() throws IOException {
        return this.zzaun.getInputStream();
    }
    
    @Override
    public final boolean getKeepAlive() throws SocketException {
        return this.zzaun.getKeepAlive();
    }
    
    @Override
    public final InetAddress getLocalAddress() {
        return this.zzaun.getLocalAddress();
    }
    
    @Override
    public final int getLocalPort() {
        return this.zzaun.getLocalPort();
    }
    
    @Override
    public final SocketAddress getLocalSocketAddress() {
        return this.zzaun.getLocalSocketAddress();
    }
    
    @Override
    public final boolean getNeedClientAuth() {
        return this.zzaun.getNeedClientAuth();
    }
    
    @Override
    public final boolean getOOBInline() throws SocketException {
        return this.zzaun.getOOBInline();
    }
    
    @Override
    public final OutputStream getOutputStream() throws IOException {
        return this.zzaun.getOutputStream();
    }
    
    @Override
    public final int getPort() {
        return this.zzaun.getPort();
    }
    
    @Override
    public final int getReceiveBufferSize() throws SocketException {
        synchronized (this) {
            return this.zzaun.getReceiveBufferSize();
        }
    }
    
    @Override
    public final SocketAddress getRemoteSocketAddress() {
        return this.zzaun.getRemoteSocketAddress();
    }
    
    @Override
    public final boolean getReuseAddress() throws SocketException {
        return this.zzaun.getReuseAddress();
    }
    
    @Override
    public final int getSendBufferSize() throws SocketException {
        synchronized (this) {
            return this.zzaun.getSendBufferSize();
        }
    }
    
    @Override
    public final SSLSession getSession() {
        return this.zzaun.getSession();
    }
    
    @Override
    public final int getSoLinger() throws SocketException {
        return this.zzaun.getSoLinger();
    }
    
    @Override
    public final int getSoTimeout() throws SocketException {
        synchronized (this) {
            return this.zzaun.getSoTimeout();
        }
    }
    
    @Override
    public final String[] getSupportedCipherSuites() {
        return this.zzaun.getSupportedCipherSuites();
    }
    
    @Override
    public final String[] getSupportedProtocols() {
        return this.zzaun.getSupportedProtocols();
    }
    
    @Override
    public final boolean getTcpNoDelay() throws SocketException {
        return this.zzaun.getTcpNoDelay();
    }
    
    @Override
    public final int getTrafficClass() throws SocketException {
        return this.zzaun.getTrafficClass();
    }
    
    @Override
    public final boolean getUseClientMode() {
        return this.zzaun.getUseClientMode();
    }
    
    @Override
    public final boolean getWantClientAuth() {
        return this.zzaun.getWantClientAuth();
    }
    
    @Override
    public final boolean isBound() {
        return this.zzaun.isBound();
    }
    
    @Override
    public final boolean isClosed() {
        return this.zzaun.isClosed();
    }
    
    @Override
    public final boolean isConnected() {
        return this.zzaun.isConnected();
    }
    
    @Override
    public final boolean isInputShutdown() {
        return this.zzaun.isInputShutdown();
    }
    
    @Override
    public final boolean isOutputShutdown() {
        return this.zzaun.isOutputShutdown();
    }
    
    @Override
    public final void removeHandshakeCompletedListener(final HandshakeCompletedListener handshakeCompletedListener) {
        this.zzaun.removeHandshakeCompletedListener(handshakeCompletedListener);
    }
    
    @Override
    public final void sendUrgentData(final int n) throws IOException {
        this.zzaun.sendUrgentData(n);
    }
    
    @Override
    public final void setEnableSessionCreation(final boolean enableSessionCreation) {
        this.zzaun.setEnableSessionCreation(enableSessionCreation);
    }
    
    @Override
    public final void setEnabledCipherSuites(final String[] enabledCipherSuites) {
        this.zzaun.setEnabledCipherSuites(enabledCipherSuites);
    }
    
    @Override
    public final void setEnabledProtocols(final String[] array) {
        String[] enabledProtocols = array;
        if (array != null) {
            enabledProtocols = array;
            if (Arrays.asList(array).contains("SSLv3")) {
                final ArrayList list = new ArrayList((Collection<? extends E>)Arrays.asList(this.zzaun.getEnabledProtocols()));
                if (list.size() > 1) {
                    list.remove("SSLv3");
                }
                enabledProtocols = (String[])list.toArray(new String[list.size()]);
            }
        }
        this.zzaun.setEnabledProtocols(enabledProtocols);
    }
    
    @Override
    public final void setKeepAlive(final boolean keepAlive) throws SocketException {
        this.zzaun.setKeepAlive(keepAlive);
    }
    
    @Override
    public final void setNeedClientAuth(final boolean needClientAuth) {
        this.zzaun.setNeedClientAuth(needClientAuth);
    }
    
    @Override
    public final void setOOBInline(final boolean oobInline) throws SocketException {
        this.zzaun.setOOBInline(oobInline);
    }
    
    @Override
    public final void setPerformancePreferences(final int n, final int n2, final int n3) {
        this.zzaun.setPerformancePreferences(n, n2, n3);
    }
    
    @Override
    public final void setReceiveBufferSize(final int receiveBufferSize) throws SocketException {
        synchronized (this) {
            this.zzaun.setReceiveBufferSize(receiveBufferSize);
        }
    }
    
    @Override
    public final void setReuseAddress(final boolean reuseAddress) throws SocketException {
        this.zzaun.setReuseAddress(reuseAddress);
    }
    
    @Override
    public final void setSendBufferSize(final int sendBufferSize) throws SocketException {
        synchronized (this) {
            this.zzaun.setSendBufferSize(sendBufferSize);
        }
    }
    
    @Override
    public final void setSoLinger(final boolean b, final int n) throws SocketException {
        this.zzaun.setSoLinger(b, n);
    }
    
    @Override
    public final void setSoTimeout(final int soTimeout) throws SocketException {
        synchronized (this) {
            this.zzaun.setSoTimeout(soTimeout);
        }
    }
    
    @Override
    public final void setTcpNoDelay(final boolean tcpNoDelay) throws SocketException {
        this.zzaun.setTcpNoDelay(tcpNoDelay);
    }
    
    @Override
    public final void setTrafficClass(final int trafficClass) throws SocketException {
        this.zzaun.setTrafficClass(trafficClass);
    }
    
    @Override
    public final void setUseClientMode(final boolean useClientMode) {
        this.zzaun.setUseClientMode(useClientMode);
    }
    
    @Override
    public final void setWantClientAuth(final boolean wantClientAuth) {
        this.zzaun.setWantClientAuth(wantClientAuth);
    }
    
    @Override
    public final void shutdownInput() throws IOException {
        this.zzaun.shutdownInput();
    }
    
    @Override
    public final void shutdownOutput() throws IOException {
        this.zzaun.shutdownOutput();
    }
    
    @Override
    public final void startHandshake() throws IOException {
        this.zzaun.startHandshake();
    }
    
    @Override
    public final String toString() {
        return this.zzaun.toString();
    }
}
