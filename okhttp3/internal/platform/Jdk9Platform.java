package okhttp3.internal.platform;

import java.util.*;
import okhttp3.*;
import okhttp3.internal.*;
import java.lang.reflect.*;
import javax.annotation.*;
import javax.net.ssl.*;

final class Jdk9Platform extends Platform
{
    final Method getProtocolMethod;
    final Method setProtocolMethod;
    
    Jdk9Platform(final Method setProtocolMethod, final Method getProtocolMethod) {
        this.setProtocolMethod = setProtocolMethod;
        this.getProtocolMethod = getProtocolMethod;
    }
    
    public static Jdk9Platform buildIfSupported() {
        try {
            return new Jdk9Platform(SSLParameters.class.getMethod("setApplicationProtocols", String[].class), SSLSocket.class.getMethod("getApplicationProtocol", (Class<?>[])new Class[0]));
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    @Override
    public void configureTlsExtensions(final SSLSocket sslSocket, final String s, final List<Protocol> list) {
        try {
            final SSLParameters sslParameters = sslSocket.getSSLParameters();
            final List<String> alpnProtocolNames = Platform.alpnProtocolNames(list);
            this.setProtocolMethod.invoke(sslParameters, alpnProtocolNames.toArray(new String[alpnProtocolNames.size()]));
            sslSocket.setSSLParameters(sslParameters);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object o;
            throw Util.assertionError("unable to set ssl parameters", (Exception)o);
        }
    }
    
    @Nullable
    @Override
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        try {
            final String s = (String)this.getProtocolMethod.invoke(sslSocket, new Object[0]);
            if (s != null && !s.equals("")) {
                return s;
            }
            return null;
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object o;
            throw Util.assertionError("unable to get selected protocols", (Exception)o);
        }
    }
    
    public X509TrustManager trustManager(final SSLSocketFactory sslSocketFactory) {
        throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
    }
}
