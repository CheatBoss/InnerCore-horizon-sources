package bo.app;

import com.appboy.support.*;
import java.security.*;
import javax.net.ssl.*;
import java.util.*;
import java.net.*;

class i extends SSLSocketFactory
{
    private static final String a;
    private SSLSocketFactory b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(i.class);
    }
    
    public i() {
        final SSLContext instance = SSLContext.getInstance("TLS");
        instance.init(null, null, null);
        this.b = instance.getSocketFactory();
    }
    
    private Socket a(final Socket socket) {
        if (socket != null && socket instanceof SSLSocket) {
            final SSLSocket sslSocket = (SSLSocket)socket;
            final ArrayList<String> list = new ArrayList<String>();
            final String[] supportedProtocols = sslSocket.getSupportedProtocols();
            for (int length = supportedProtocols.length, i = 0; i < length; ++i) {
                final String s = supportedProtocols[i];
                if (!s.equals("SSLv3")) {
                    list.add(s);
                }
            }
            final String a = i.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Enabling SSL protocols: ");
            sb.append(list);
            AppboyLogger.v(a, sb.toString());
            sslSocket.setEnabledProtocols(list.toArray(new String[list.size()]));
        }
        return socket;
    }
    
    @Override
    public Socket createSocket() {
        return this.a(this.b.createSocket());
    }
    
    @Override
    public Socket createSocket(final String s, final int n) {
        return this.a(this.b.createSocket(s, n));
    }
    
    @Override
    public Socket createSocket(final String s, final int n, final InetAddress inetAddress, final int n2) {
        return this.a(this.b.createSocket(s, n, inetAddress, n2));
    }
    
    @Override
    public Socket createSocket(final InetAddress inetAddress, final int n) {
        return this.a(this.b.createSocket(inetAddress, n));
    }
    
    @Override
    public Socket createSocket(final InetAddress inetAddress, final int n, final InetAddress inetAddress2, final int n2) {
        return this.a(this.b.createSocket(inetAddress, n, inetAddress2, n2));
    }
    
    @Override
    public Socket createSocket(final Socket socket, final String s, final int n, final boolean b) {
        return this.a(this.b.createSocket(socket, s, n, b));
    }
    
    @Override
    public String[] getDefaultCipherSuites() {
        return this.b.getDefaultCipherSuites();
    }
    
    @Override
    public String[] getSupportedCipherSuites() {
        return this.b.getSupportedCipherSuites();
    }
}
