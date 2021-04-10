package okhttp3.internal.connection;

import okhttp3.*;
import okhttp3.internal.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.security.cert.*;
import javax.net.ssl.*;

public final class ConnectionSpecSelector
{
    private final List<ConnectionSpec> connectionSpecs;
    private boolean isFallback;
    private boolean isFallbackPossible;
    private int nextModeIndex;
    
    public ConnectionSpecSelector(final List<ConnectionSpec> connectionSpecs) {
        this.nextModeIndex = 0;
        this.connectionSpecs = connectionSpecs;
    }
    
    private boolean isFallbackPossible(final SSLSocket sslSocket) {
        for (int i = this.nextModeIndex; i < this.connectionSpecs.size(); ++i) {
            if (this.connectionSpecs.get(i).isCompatible(sslSocket)) {
                return true;
            }
        }
        return false;
    }
    
    public ConnectionSpec configureSecureSocket(final SSLSocket sslSocket) throws IOException {
        int i = this.nextModeIndex;
        while (true) {
            while (i < this.connectionSpecs.size()) {
                final ConnectionSpec connectionSpec = this.connectionSpecs.get(i);
                if (connectionSpec.isCompatible(sslSocket)) {
                    this.nextModeIndex = i + 1;
                    if (connectionSpec != null) {
                        this.isFallbackPossible = this.isFallbackPossible(sslSocket);
                        Internal.instance.apply(connectionSpec, sslSocket, this.isFallback);
                        return connectionSpec;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unable to find acceptable protocols. isFallback=");
                    sb.append(this.isFallback);
                    sb.append(", modes=");
                    sb.append(this.connectionSpecs);
                    sb.append(", supported protocols=");
                    sb.append(Arrays.toString(sslSocket.getEnabledProtocols()));
                    throw new UnknownServiceException(sb.toString());
                }
                else {
                    ++i;
                }
            }
            final ConnectionSpec connectionSpec = null;
            continue;
        }
    }
    
    public boolean connectionFailed(final IOException ex) {
        boolean b = true;
        this.isFallback = true;
        if (!this.isFallbackPossible) {
            return false;
        }
        if (ex instanceof ProtocolException) {
            return false;
        }
        if (ex instanceof InterruptedIOException) {
            return false;
        }
        final boolean b2 = ex instanceof SSLHandshakeException;
        if (b2 && ex.getCause() instanceof CertificateException) {
            return false;
        }
        if (ex instanceof SSLPeerUnverifiedException) {
            return false;
        }
        if (!b2) {
            if (ex instanceof SSLProtocolException) {
                return true;
            }
            b = false;
        }
        return b;
    }
}
