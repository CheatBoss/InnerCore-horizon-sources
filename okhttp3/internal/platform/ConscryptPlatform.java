package okhttp3.internal.platform;

import org.conscrypt.*;
import java.util.*;
import okhttp3.*;
import java.security.*;
import javax.annotation.*;
import javax.net.ssl.*;

public class ConscryptPlatform extends Platform
{
    private ConscryptPlatform() {
    }
    
    public static Platform buildIfSupported() {
        try {
            Class.forName("org.conscrypt.ConscryptEngineSocket");
            if (!Conscrypt.isAvailable()) {
                return null;
            }
            Conscrypt.setUseEngineSocketByDefault(true);
            return new ConscryptPlatform();
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    private Provider getProvider() {
        return (Provider)new OpenSSLProvider();
    }
    
    @Override
    public void configureTlsExtensions(final SSLSocket sslSocket, final String s, final List<Protocol> list) {
        if (Conscrypt.isConscrypt(sslSocket)) {
            if (s != null) {
                Conscrypt.setUseSessionTickets(sslSocket, true);
                Conscrypt.setHostname(sslSocket, s);
            }
            Conscrypt.setApplicationProtocols(sslSocket, (String[])Platform.alpnProtocolNames(list).toArray(new String[0]));
            return;
        }
        super.configureTlsExtensions(sslSocket, s, list);
    }
    
    @Override
    public SSLContext getSSLContext() {
        try {
            return SSLContext.getInstance("TLS", this.getProvider());
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("No TLS provider", ex);
        }
    }
    
    @Nullable
    @Override
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        if (Conscrypt.isConscrypt(sslSocket)) {
            return Conscrypt.getApplicationProtocol(sslSocket);
        }
        return super.getSelectedProtocol(sslSocket);
    }
    
    public X509TrustManager trustManager(final SSLSocketFactory sslSocketFactory) {
        if (!Conscrypt.isConscrypt(sslSocketFactory)) {
            return super.trustManager(sslSocketFactory);
        }
        try {
            final Object fieldOrNull = Platform.readFieldOrNull(sslSocketFactory, Object.class, "sslParameters");
            if (fieldOrNull != null) {
                return Platform.readFieldOrNull(fieldOrNull, X509TrustManager.class, "x509TrustManager");
            }
            return null;
        }
        catch (Exception ex) {
            throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on Conscrypt", ex);
        }
    }
}
