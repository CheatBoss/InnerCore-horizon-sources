package okhttp3;

import java.security.cert.*;
import javax.net.ssl.*;
import okhttp3.internal.*;
import java.util.*;
import javax.annotation.*;

public final class Handshake
{
    private final CipherSuite cipherSuite;
    private final List<Certificate> localCertificates;
    private final List<Certificate> peerCertificates;
    private final TlsVersion tlsVersion;
    
    private Handshake(final TlsVersion tlsVersion, final CipherSuite cipherSuite, final List<Certificate> peerCertificates, final List<Certificate> localCertificates) {
        this.tlsVersion = tlsVersion;
        this.cipherSuite = cipherSuite;
        this.peerCertificates = peerCertificates;
        this.localCertificates = localCertificates;
    }
    
    public static Handshake get(final SSLSession sslSession) {
        final String cipherSuite = sslSession.getCipherSuite();
        if (cipherSuite == null) {
            throw new IllegalStateException("cipherSuite == null");
        }
        final CipherSuite forJavaName = CipherSuite.forJavaName(cipherSuite);
        final String protocol = sslSession.getProtocol();
        if (protocol != null) {
            final TlsVersion forJavaName2 = TlsVersion.forJavaName(protocol);
            Certificate[] peerCertificates;
            try {
                peerCertificates = sslSession.getPeerCertificates();
            }
            catch (SSLPeerUnverifiedException ex) {
                peerCertificates = null;
            }
            List<Certificate> list;
            if (peerCertificates != null) {
                list = Util.immutableList(peerCertificates);
            }
            else {
                list = Collections.emptyList();
            }
            final Certificate[] localCertificates = sslSession.getLocalCertificates();
            List<Certificate> list2;
            if (localCertificates != null) {
                list2 = Util.immutableList(localCertificates);
            }
            else {
                list2 = Collections.emptyList();
            }
            return new Handshake(forJavaName2, forJavaName, list, list2);
        }
        throw new IllegalStateException("tlsVersion == null");
    }
    
    public CipherSuite cipherSuite() {
        return this.cipherSuite;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        final boolean b = o instanceof Handshake;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final Handshake handshake = (Handshake)o;
        boolean b3 = b2;
        if (this.tlsVersion.equals(handshake.tlsVersion)) {
            b3 = b2;
            if (this.cipherSuite.equals(handshake.cipherSuite)) {
                b3 = b2;
                if (this.peerCertificates.equals(handshake.peerCertificates)) {
                    b3 = b2;
                    if (this.localCertificates.equals(handshake.localCertificates)) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        return (((this.tlsVersion.hashCode() + 527) * 31 + this.cipherSuite.hashCode()) * 31 + this.peerCertificates.hashCode()) * 31 + this.localCertificates.hashCode();
    }
    
    public List<Certificate> peerCertificates() {
        return this.peerCertificates;
    }
}
