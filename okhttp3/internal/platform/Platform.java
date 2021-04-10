package okhttp3.internal.platform;

import okhttp3.*;
import java.util.*;
import okio.*;
import java.lang.reflect.*;
import okhttp3.internal.tls.*;
import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import java.security.*;
import javax.annotation.*;
import java.util.logging.*;

public class Platform
{
    private static final Platform PLATFORM;
    private static final Logger logger;
    
    static {
        PLATFORM = findPlatform();
        logger = Logger.getLogger(OkHttpClient.class.getName());
    }
    
    public static List<String> alpnProtocolNames(final List<Protocol> list) {
        final ArrayList<String> list2 = new ArrayList<String>(list.size());
        for (int size = list.size(), i = 0; i < size; ++i) {
            final Protocol protocol = list.get(i);
            if (protocol != Protocol.HTTP_1_0) {
                list2.add(protocol.toString());
            }
        }
        return list2;
    }
    
    static byte[] concatLengthPrefixed(final List<Protocol> list) {
        final Buffer buffer = new Buffer();
        for (int size = list.size(), i = 0; i < size; ++i) {
            final Protocol protocol = list.get(i);
            if (protocol != Protocol.HTTP_1_0) {
                buffer.writeByte(protocol.toString().length());
                buffer.writeUtf8(protocol.toString());
            }
        }
        return buffer.readByteArray();
    }
    
    private static Platform findPlatform() {
        final Platform buildIfSupported = AndroidPlatform.buildIfSupported();
        if (buildIfSupported != null) {
            return buildIfSupported;
        }
        if (isConscryptPreferred()) {
            final Platform buildIfSupported2 = ConscryptPlatform.buildIfSupported();
            if (buildIfSupported2 != null) {
                return buildIfSupported2;
            }
        }
        final Jdk9Platform buildIfSupported3 = Jdk9Platform.buildIfSupported();
        if (buildIfSupported3 != null) {
            return buildIfSupported3;
        }
        final Platform buildIfSupported4 = JdkWithJettyBootPlatform.buildIfSupported();
        if (buildIfSupported4 != null) {
            return buildIfSupported4;
        }
        return new Platform();
    }
    
    public static Platform get() {
        return Platform.PLATFORM;
    }
    
    public static boolean isConscryptPreferred() {
        return "conscrypt".equals(System.getProperty("okhttp.platform")) || "Conscrypt".equals(Security.getProviders()[0].getName());
    }
    
    static <T> T readFieldOrNull(Object fieldOrNull, final Class<T> clazz, final String s) {
        Class<?> clazz2 = fieldOrNull.getClass();
        while (clazz2 != Object.class) {
            try {
                final Field declaredField = clazz2.getDeclaredField(s);
                declaredField.setAccessible(true);
                final Object value = declaredField.get(fieldOrNull);
                if (value == null) {
                    return null;
                }
                if (!clazz.isInstance(value)) {
                    return null;
                }
                return clazz.cast(value);
            }
            catch (IllegalAccessException ex) {
                throw new AssertionError();
            }
            catch (NoSuchFieldException ex2) {
                clazz2 = clazz2.getSuperclass();
                continue;
            }
            break;
        }
        if (!s.equals("delegate")) {
            fieldOrNull = readFieldOrNull(fieldOrNull, Object.class, "delegate");
            if (fieldOrNull != null) {
                return (T)readFieldOrNull(fieldOrNull, (Class<Object>)clazz, s);
            }
        }
        return null;
    }
    
    public void afterHandshake(final SSLSocket sslSocket) {
    }
    
    public CertificateChainCleaner buildCertificateChainCleaner(final SSLSocketFactory sslSocketFactory) {
        final X509TrustManager trustManager = this.trustManager(sslSocketFactory);
        if (trustManager != null) {
            return this.buildCertificateChainCleaner(trustManager);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unable to extract the trust manager on ");
        sb.append(get());
        sb.append(", sslSocketFactory is ");
        sb.append(sslSocketFactory.getClass());
        throw new IllegalStateException(sb.toString());
    }
    
    public CertificateChainCleaner buildCertificateChainCleaner(final X509TrustManager x509TrustManager) {
        return new BasicCertificateChainCleaner(this.buildTrustRootIndex(x509TrustManager));
    }
    
    public TrustRootIndex buildTrustRootIndex(final X509TrustManager x509TrustManager) {
        return new BasicTrustRootIndex(x509TrustManager.getAcceptedIssuers());
    }
    
    public void configureTlsExtensions(final SSLSocket sslSocket, final String s, final List<Protocol> list) {
    }
    
    public void connectSocket(final Socket socket, final InetSocketAddress inetSocketAddress, final int n) throws IOException {
        socket.connect(inetSocketAddress, n);
    }
    
    public SSLContext getSSLContext() {
        try {
            return SSLContext.getInstance("TLS");
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("No TLS provider", ex);
        }
    }
    
    @Nullable
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        return null;
    }
    
    public Object getStackTraceForCloseable(final String s) {
        if (Platform.logger.isLoggable(Level.FINE)) {
            return new Throwable(s);
        }
        return null;
    }
    
    public boolean isCleartextTrafficPermitted(final String s) {
        return true;
    }
    
    public void log(final int n, final String s, final Throwable t) {
        Level level;
        if (n == 5) {
            level = Level.WARNING;
        }
        else {
            level = Level.INFO;
        }
        Platform.logger.log(level, s, t);
    }
    
    public void logCloseableLeak(final String s, final Object o) {
        String string = s;
        if (o == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);");
            string = sb.toString();
        }
        this.log(5, string, (Throwable)o);
    }
    
    protected X509TrustManager trustManager(final SSLSocketFactory sslSocketFactory) {
        try {
            final Object fieldOrNull = readFieldOrNull(sslSocketFactory, Class.forName("sun.security.ssl.SSLContextImpl"), "context");
            if (fieldOrNull == null) {
                return null;
            }
            return readFieldOrNull(fieldOrNull, X509TrustManager.class, "trustManager");
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
}
