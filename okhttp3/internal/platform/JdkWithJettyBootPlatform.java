package okhttp3.internal.platform;

import javax.net.ssl.*;
import okhttp3.internal.*;
import java.util.*;
import okhttp3.*;
import java.lang.reflect.*;
import javax.annotation.*;

class JdkWithJettyBootPlatform extends Platform
{
    private final Class<?> clientProviderClass;
    private final Method getMethod;
    private final Method putMethod;
    private final Method removeMethod;
    private final Class<?> serverProviderClass;
    
    JdkWithJettyBootPlatform(final Method putMethod, final Method getMethod, final Method removeMethod, final Class<?> clientProviderClass, final Class<?> serverProviderClass) {
        this.putMethod = putMethod;
        this.getMethod = getMethod;
        this.removeMethod = removeMethod;
        this.clientProviderClass = clientProviderClass;
        this.serverProviderClass = serverProviderClass;
    }
    
    public static Platform buildIfSupported() {
        try {
            final Class<?> forName = Class.forName("org.eclipse.jetty.alpn.ALPN");
            final StringBuilder sb = new StringBuilder();
            sb.append("org.eclipse.jetty.alpn.ALPN");
            sb.append("$Provider");
            final Class<?> forName2 = Class.forName(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("org.eclipse.jetty.alpn.ALPN");
            sb2.append("$ClientProvider");
            final Class<?> forName3 = Class.forName(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("org.eclipse.jetty.alpn.ALPN");
            sb3.append("$ServerProvider");
            return new JdkWithJettyBootPlatform(forName.getMethod("put", SSLSocket.class, forName2), forName.getMethod("get", SSLSocket.class), forName.getMethod("remove", SSLSocket.class), forName3, Class.forName(sb3.toString()));
        }
        catch (ClassNotFoundException | NoSuchMethodException ex) {
            return null;
        }
    }
    
    @Override
    public void afterHandshake(final SSLSocket sslSocket) {
        try {
            this.removeMethod.invoke(null, sslSocket);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object o;
            throw Util.assertionError("unable to remove alpn", (Exception)o);
        }
    }
    
    @Override
    public void configureTlsExtensions(final SSLSocket sslSocket, final String s, final List<Protocol> list) {
        final List<String> alpnProtocolNames = Platform.alpnProtocolNames(list);
        try {
            this.putMethod.invoke(null, sslSocket, Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class[] { this.clientProviderClass, this.serverProviderClass }, new JettyNegoProvider(alpnProtocolNames)));
        }
        catch (InvocationTargetException | IllegalAccessException ex) {
            final Object o;
            throw Util.assertionError("unable to set alpn", (Exception)o);
        }
    }
    
    @Nullable
    @Override
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        try {
            final JettyNegoProvider jettyNegoProvider = (JettyNegoProvider)Proxy.getInvocationHandler(this.getMethod.invoke(null, sslSocket));
            if (!jettyNegoProvider.unsupported && jettyNegoProvider.selected == null) {
                Platform.get().log(4, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", null);
                return null;
            }
            if (jettyNegoProvider.unsupported) {
                return null;
            }
            return jettyNegoProvider.selected;
        }
        catch (InvocationTargetException | IllegalAccessException ex) {
            final Object o;
            throw Util.assertionError("unable to get selected protocol", (Exception)o);
        }
    }
    
    private static class JettyNegoProvider implements InvocationHandler
    {
        private final List<String> protocols;
        String selected;
        boolean unsupported;
        
        JettyNegoProvider(final List<String> protocols) {
            this.protocols = protocols;
        }
        
        @Override
        public Object invoke(Object o, final Method method, final Object[] array) throws Throwable {
            final String name = method.getName();
            final Class<?> returnType = method.getReturnType();
            Object[] empty_STRING_ARRAY = array;
            if (array == null) {
                empty_STRING_ARRAY = Util.EMPTY_STRING_ARRAY;
            }
            if (name.equals("supports") && Boolean.TYPE == returnType) {
                return true;
            }
            if (name.equals("unsupported") && Void.TYPE == returnType) {
                this.unsupported = true;
                return null;
            }
            if (name.equals("protocols") && empty_STRING_ARRAY.length == 0) {
                return this.protocols;
            }
            if ((name.equals("selectProtocol") || name.equals("select")) && String.class == returnType && empty_STRING_ARRAY.length == 1 && empty_STRING_ARRAY[0] instanceof List) {
                final List list = (List)empty_STRING_ARRAY[0];
                for (int size = list.size(), i = 0; i < size; ++i) {
                    if (this.protocols.contains(list.get(i))) {
                        o = list.get(i);
                        return this.selected = (String)o;
                    }
                }
                o = this.protocols.get(0);
                return this.selected = (String)o;
            }
            if ((name.equals("protocolSelected") || name.equals("selected")) && empty_STRING_ARRAY.length == 1) {
                this.selected = (String)empty_STRING_ARRAY[0];
                return null;
            }
            return method.invoke(this, empty_STRING_ARRAY);
        }
    }
}
