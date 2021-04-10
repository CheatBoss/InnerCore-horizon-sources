package okhttp3.internal;

import javax.net.ssl.*;
import java.net.*;
import okhttp3.*;
import okhttp3.internal.connection.*;
import okhttp3.internal.cache.*;

public abstract class Internal
{
    public static Internal instance;
    
    public static void initializeInstanceForTests() {
        new OkHttpClient();
    }
    
    public abstract void addLenient(final Headers.Builder p0, final String p1);
    
    public abstract void addLenient(final Headers.Builder p0, final String p1, final String p2);
    
    public abstract void apply(final ConnectionSpec p0, final SSLSocket p1, final boolean p2);
    
    public abstract int code(final Response.Builder p0);
    
    public abstract boolean connectionBecameIdle(final ConnectionPool p0, final RealConnection p1);
    
    public abstract Socket deduplicate(final ConnectionPool p0, final Address p1, final StreamAllocation p2);
    
    public abstract boolean equalsNonHost(final Address p0, final Address p1);
    
    public abstract RealConnection get(final ConnectionPool p0, final Address p1, final StreamAllocation p2, final Route p3);
    
    public abstract HttpUrl getHttpUrlChecked(final String p0) throws MalformedURLException, UnknownHostException;
    
    public abstract Call newWebSocketCall(final OkHttpClient p0, final Request p1);
    
    public abstract void put(final ConnectionPool p0, final RealConnection p1);
    
    public abstract RouteDatabase routeDatabase(final ConnectionPool p0);
    
    public abstract void setCache(final OkHttpClient.Builder p0, final InternalCache p1);
    
    public abstract StreamAllocation streamAllocation(final Call p0);
}
