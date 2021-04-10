package bo.app;

import com.appboy.support.*;
import java.net.*;
import javax.net.ssl.*;

public class k
{
    private static final String a;
    private static i b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(k.class);
        try {
            k.b = new i();
        }
        catch (Exception ex) {
            AppboyLogger.e(k.a, "Exception initializing static TLS socket factory.", ex);
        }
    }
    
    public static URLConnection a(final URL url) {
        final URLConnection openConnection = url.openConnection();
        if (url.getProtocol().equals("https")) {
            try {
                ((HttpsURLConnection)openConnection).setSSLSocketFactory(k.b);
                return openConnection;
            }
            catch (Exception ex) {
                AppboyLogger.e(k.a, "Exception setting TLS socket factory on url connection.", ex);
            }
        }
        return openConnection;
    }
}
