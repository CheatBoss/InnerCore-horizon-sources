package android.net.http;

import java.security.cert.*;
import java.security.*;
import javax.security.cert.*;
import javax.net.ssl.*;

public class DelegatingSSLSession implements SSLSession
{
    protected DelegatingSSLSession() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getApplicationBufferSize() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getCipherSuite() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getCreationTime() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public byte[] getId() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getLastAccessedTime() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Certificate[] getLocalCertificates() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Principal getLocalPrincipal() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getPacketBufferSize() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getPeerHost() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getPeerPort() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getProtocol() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public SSLSessionContext getSessionContext() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object getValue(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String[] getValueNames() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void invalidate() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isValid() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void putValue(final String s, final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void removeValue(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public static class CertificateWrap extends DelegatingSSLSession
    {
        public CertificateWrap(final Certificate certificate) {
            throw new RuntimeException("Stub!");
        }
        
        @Override
        public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
            throw new RuntimeException("Stub!");
        }
    }
}
