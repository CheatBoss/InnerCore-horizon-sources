package android.net.http;

import android.content.*;
import org.apache.http.*;

abstract class Connection
{
    protected SslCertificate mCertificate;
    protected AndroidHttpClientConnection mHttpClientConnection;
    
    protected Connection(final Context context, final HttpHost httpHost, final RequestFeeder requestFeeder) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
}
