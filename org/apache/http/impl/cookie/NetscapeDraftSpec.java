package org.apache.http.impl.cookie;

import java.util.*;
import org.apache.http.*;
import org.apache.http.cookie.*;

@Deprecated
public class NetscapeDraftSpec extends CookieSpecBase
{
    protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss z";
    
    public NetscapeDraftSpec() {
        throw new RuntimeException("Stub!");
    }
    
    public NetscapeDraftSpec(final String[] array) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public List<Header> formatCookies(final List<Cookie> list) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getVersion() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header getVersionHeader() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public List<Cookie> parse(final Header header, final CookieOrigin cookieOrigin) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
}
