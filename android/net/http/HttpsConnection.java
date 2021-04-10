package android.net.http;

import android.content.*;
import org.apache.http.*;
import java.io.*;

public class HttpsConnection extends Connection
{
    protected SslCertificate mCertificate;
    protected AndroidHttpClientConnection mHttpClientConnection;
    
    HttpsConnection() {
        super(null, null, null);
        throw new RuntimeException("Stub!");
    }
    
    public static void initializeEngine(final File file) {
        throw new RuntimeException("Stub!");
    }
}
