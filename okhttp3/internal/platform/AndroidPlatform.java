package okhttp3.internal.platform;

import java.security.*;
import okhttp3.internal.tls.*;
import java.lang.reflect.*;
import java.util.*;
import okhttp3.*;
import java.net.*;
import android.os.*;
import java.io.*;
import okhttp3.internal.*;
import javax.annotation.*;
import android.util.*;
import javax.net.ssl.*;
import java.security.cert.*;

class AndroidPlatform extends Platform
{
    private final CloseGuard closeGuard;
    private final OptionalMethod<Socket> getAlpnSelectedProtocol;
    private final OptionalMethod<Socket> setAlpnProtocols;
    private final OptionalMethod<Socket> setHostname;
    private final OptionalMethod<Socket> setUseSessionTickets;
    private final Class<?> sslParametersClass;
    
    AndroidPlatform(final Class<?> sslParametersClass, final OptionalMethod<Socket> setUseSessionTickets, final OptionalMethod<Socket> setHostname, final OptionalMethod<Socket> getAlpnSelectedProtocol, final OptionalMethod<Socket> setAlpnProtocols) {
        this.closeGuard = CloseGuard.get();
        this.sslParametersClass = sslParametersClass;
        this.setUseSessionTickets = setUseSessionTickets;
        this.setHostname = setHostname;
        this.getAlpnSelectedProtocol = getAlpnSelectedProtocol;
        this.setAlpnProtocols = setAlpnProtocols;
    }
    
    private boolean api23IsCleartextTrafficPermitted(final String s, final Class<?> clazz, final Object o) throws InvocationTargetException, IllegalAccessException {
        try {
            return (boolean)clazz.getMethod("isCleartextTrafficPermitted", (Class<?>[])new Class[0]).invoke(o, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            return super.isCleartextTrafficPermitted(s);
        }
    }
    
    private boolean api24IsCleartextTrafficPermitted(final String s, final Class<?> clazz, final Object o) throws InvocationTargetException, IllegalAccessException {
        try {
            return (boolean)clazz.getMethod("isCleartextTrafficPermitted", String.class).invoke(o, s);
        }
        catch (NoSuchMethodException ex) {
            return this.api23IsCleartextTrafficPermitted(s, clazz, o);
        }
    }
    
    public static Platform buildIfSupported() {
    Label_0106_Outer:
        while (true) {
            while (true) {
                while (true) {
                    Label_0019: {
                        try {
                            final Class<?> clazz = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
                            break Label_0019;
                        }
                        catch (ClassNotFoundException ex) {
                            final String s = "org.apache.harmony.xnet.provider.jsse.SSLParametersImpl";
                            final Class<?> clazz = Class.forName(s);
                            continue Label_0106_Outer;
                        }
                        try {
                            final String s = "org.apache.harmony.xnet.provider.jsse.SSLParametersImpl";
                            final Class<?> clazz = Class.forName(s);
                            continue Label_0106_Outer;
                            final OptionalMethod<Socket> optionalMethod = new OptionalMethod<Socket>(null, "setUseSessionTickets", new Class[] { Boolean.TYPE });
                            final OptionalMethod<Socket> optionalMethod2 = new OptionalMethod<Socket>(null, "setHostname", new Class[] { String.class });
                            // iftrue(Label_0125:, !supportsAlpn())
                            Block_5: {
                                break Block_5;
                                final OptionalMethod<Socket> optionalMethod3;
                                final OptionalMethod<Socket> optionalMethod4;
                                return new AndroidPlatform(clazz, optionalMethod, optionalMethod2, optionalMethod3, optionalMethod4);
                            }
                            final OptionalMethod<Socket> optionalMethod3 = new OptionalMethod<Socket>(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
                            final OptionalMethod<Socket> optionalMethod4 = new OptionalMethod<Socket>(null, "setAlpnProtocols", new Class[] { byte[].class });
                            return new AndroidPlatform(clazz, optionalMethod, optionalMethod2, optionalMethod3, optionalMethod4);
                        }
                        catch (ClassNotFoundException ex2) {
                            return null;
                        }
                    }
                    break;
                }
                Label_0125: {
                    final OptionalMethod<Socket> optionalMethod4;
                    final OptionalMethod<Socket> optionalMethod3 = optionalMethod4 = null;
                }
                continue;
            }
        }
    }
    
    private static boolean supportsAlpn() {
        if (Security.getProvider("GMSCore_OpenSSL") != null) {
            return true;
        }
        try {
            Class.forName("android.net.Network");
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    @Override
    public CertificateChainCleaner buildCertificateChainCleaner(final X509TrustManager x509TrustManager) {
        try {
            final Class<?> forName = Class.forName("android.net.http.X509TrustManagerExtensions");
            return new AndroidCertificateChainCleaner(forName.getConstructor(X509TrustManager.class).newInstance(x509TrustManager), forName.getMethod("checkServerTrusted", X509Certificate[].class, String.class, String.class));
        }
        catch (Exception ex) {
            return super.buildCertificateChainCleaner(x509TrustManager);
        }
    }
    
    @Override
    public TrustRootIndex buildTrustRootIndex(final X509TrustManager x509TrustManager) {
        try {
            final Method declaredMethod = x509TrustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            declaredMethod.setAccessible(true);
            return new AndroidTrustRootIndex(x509TrustManager, declaredMethod);
        }
        catch (NoSuchMethodException ex) {
            return super.buildTrustRootIndex(x509TrustManager);
        }
    }
    
    @Override
    public void configureTlsExtensions(final SSLSocket sslSocket, final String s, final List<Protocol> list) {
        if (s != null) {
            this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sslSocket, true);
            this.setHostname.invokeOptionalWithoutCheckedException(sslSocket, s);
        }
        final OptionalMethod<Socket> setAlpnProtocols = this.setAlpnProtocols;
        if (setAlpnProtocols != null && setAlpnProtocols.isSupported(sslSocket)) {
            this.setAlpnProtocols.invokeWithoutCheckedException(sslSocket, Platform.concatLengthPrefixed(list));
        }
    }
    
    @Override
    public void connectSocket(final Socket socket, final InetSocketAddress inetSocketAddress, final int n) throws IOException {
        try {
            socket.connect(inetSocketAddress, n);
        }
        catch (ClassCastException ex2) {
            if (Build$VERSION.SDK_INT == 26) {
                final IOException ex = new IOException("Exception in connect");
                ex.initCause(ex2);
                throw ex;
            }
            throw ex2;
        }
        catch (SecurityException ex4) {
            final IOException ex3 = new IOException("Exception in connect");
            ex3.initCause(ex4);
            throw ex3;
        }
        catch (AssertionError assertionError) {
            if (Util.isAndroidGetsocknameError(assertionError)) {
                throw new IOException(assertionError);
            }
            throw assertionError;
        }
    }
    
    @Nullable
    @Override
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        final OptionalMethod<Socket> getAlpnSelectedProtocol = this.getAlpnSelectedProtocol;
        final String s = null;
        if (getAlpnSelectedProtocol == null) {
            return null;
        }
        if (!getAlpnSelectedProtocol.isSupported(sslSocket)) {
            return null;
        }
        final byte[] array = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException(sslSocket, new Object[0]);
        String s2 = s;
        if (array != null) {
            s2 = new String(array, Util.UTF_8);
        }
        return s2;
    }
    
    @Override
    public Object getStackTraceForCloseable(final String s) {
        return this.closeGuard.createAndOpen(s);
    }
    
    @Override
    public boolean isCleartextTrafficPermitted(final String s) {
        try {
            final Class<?> forName = Class.forName("android.security.NetworkSecurityPolicy");
            return this.api24IsCleartextTrafficPermitted(s, forName, forName.getMethod("getInstance", (Class<?>[])new Class[0]).invoke(null, new Object[0]));
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            final Object o;
            throw Util.assertionError("unable to determine cleartext support", (Exception)o);
        }
        catch (ClassNotFoundException | NoSuchMethodException ex2) {
            return super.isCleartextTrafficPermitted(s);
        }
    }
    
    @Override
    public void log(int i, final String s, final Throwable t) {
        int n = 5;
        if (i != 5) {
            n = 3;
        }
        String string = s;
        if (t != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append('\n');
            sb.append(Log.getStackTraceString(t));
            string = sb.toString();
        }
        i = 0;
        int min;
        for (int length = string.length(); i < length; i = min + 1) {
            int index = string.indexOf(10, i);
            if (index == -1) {
                index = length;
            }
            while (true) {
                min = Math.min(index, i + 4000);
                Log.println(n, "OkHttp", string.substring(i, min));
                if (min >= index) {
                    break;
                }
                i = min;
            }
        }
    }
    
    @Override
    public void logCloseableLeak(final String s, final Object o) {
        if (!this.closeGuard.warnIfOpen(o)) {
            this.log(5, s, null);
        }
    }
    
    @Override
    protected X509TrustManager trustManager(final SSLSocketFactory sslSocketFactory) {
        Object o;
        if ((o = Platform.readFieldOrNull(sslSocketFactory, this.sslParametersClass, "sslParameters")) == null) {
            try {
                o = Platform.readFieldOrNull(sslSocketFactory, Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, sslSocketFactory.getClass().getClassLoader()), "sslParameters");
            }
            catch (ClassNotFoundException ex) {
                return super.trustManager(sslSocketFactory);
            }
        }
        final X509TrustManager x509TrustManager = Platform.readFieldOrNull(o, X509TrustManager.class, "x509TrustManager");
        if (x509TrustManager != null) {
            return x509TrustManager;
        }
        return Platform.readFieldOrNull(o, X509TrustManager.class, "trustManager");
    }
    
    static final class AndroidCertificateChainCleaner extends CertificateChainCleaner
    {
        private final Method checkServerTrusted;
        private final Object x509TrustManagerExtensions;
        
        AndroidCertificateChainCleaner(final Object x509TrustManagerExtensions, final Method checkServerTrusted) {
            this.x509TrustManagerExtensions = x509TrustManagerExtensions;
            this.checkServerTrusted = checkServerTrusted;
        }
        
        @Override
        public List<Certificate> clean(final List<Certificate> list, final String s) throws SSLPeerUnverifiedException {
            try {
                return (List<Certificate>)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, list.toArray(new X509Certificate[list.size()]), "RSA", s);
            }
            catch (IllegalAccessException ex) {
                throw new AssertionError((Object)ex);
            }
            catch (InvocationTargetException ex3) {
                final SSLPeerUnverifiedException ex2 = new SSLPeerUnverifiedException(ex3.getMessage());
                ex2.initCause(ex3);
                throw ex2;
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AndroidCertificateChainCleaner;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
    }
    
    static final class AndroidTrustRootIndex implements TrustRootIndex
    {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;
        
        AndroidTrustRootIndex(final X509TrustManager trustManager, final Method findByIssuerAndSignatureMethod) {
            this.findByIssuerAndSignatureMethod = findByIssuerAndSignatureMethod;
            this.trustManager = trustManager;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof AndroidTrustRootIndex)) {
                return false;
            }
            final AndroidTrustRootIndex androidTrustRootIndex = (AndroidTrustRootIndex)o;
            return this.trustManager.equals(androidTrustRootIndex.trustManager) && this.findByIssuerAndSignatureMethod.equals(androidTrustRootIndex.findByIssuerAndSignatureMethod);
        }
        
        @Override
        public X509Certificate findByIssuerAndSignature(X509Certificate trustedCert) {
            try {
                final TrustAnchor trustAnchor = (TrustAnchor)this.findByIssuerAndSignatureMethod.invoke(this.trustManager, trustedCert);
                if (trustAnchor != null) {
                    trustedCert = trustAnchor.getTrustedCert();
                    return trustedCert;
                }
                return null;
            }
            catch (InvocationTargetException ex2) {
                return null;
            }
            catch (IllegalAccessException ex) {
                throw Util.assertionError("unable to get issues and signature", ex);
            }
        }
        
        @Override
        public int hashCode() {
            return this.trustManager.hashCode() + this.findByIssuerAndSignatureMethod.hashCode() * 31;
        }
    }
    
    static final class CloseGuard
    {
        private final Method getMethod;
        private final Method openMethod;
        private final Method warnIfOpenMethod;
        
        CloseGuard(final Method getMethod, final Method openMethod, final Method warnIfOpenMethod) {
            this.getMethod = getMethod;
            this.openMethod = openMethod;
            this.warnIfOpenMethod = warnIfOpenMethod;
        }
        
        static CloseGuard get() {
            Method method = null;
            Method method3;
            Method method4;
            try {
                final Class<?> forName = Class.forName("dalvik.system.CloseGuard");
                final Method method2 = forName.getMethod("get", (Class<?>[])new Class[0]);
                method3 = forName.getMethod("open", String.class);
                method4 = forName.getMethod("warnIfOpen", (Class<?>[])new Class[0]);
                method = method2;
            }
            catch (Exception ex) {
                method4 = (method3 = null);
            }
            return new CloseGuard(method, method3, method4);
        }
        
        Object createAndOpen(final String s) {
            final Method getMethod = this.getMethod;
            if (getMethod != null) {
                try {
                    final Object invoke = getMethod.invoke(null, new Object[0]);
                    this.openMethod.invoke(invoke, s);
                    return invoke;
                }
                catch (Exception ex) {}
            }
            return null;
        }
        
        boolean warnIfOpen(final Object o) {
            if (o != null) {
                try {
                    this.warnIfOpenMethod.invoke(o, new Object[0]);
                    return true;
                }
                catch (Exception ex) {}
            }
            return false;
        }
    }
}
