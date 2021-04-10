package org.apache.http.impl.cookie;

import org.apache.http.*;
import java.util.*;
import org.apache.http.cookie.*;

@Deprecated
public abstract class CookieSpecBase extends AbstractCookieSpec
{
    public CookieSpecBase() {
        throw new RuntimeException("Stub!");
    }
    
    protected static String getDefaultDomain(final CookieOrigin cookieOrigin) {
        throw new RuntimeException("Stub!");
    }
    
    protected static String getDefaultPath(final CookieOrigin cookieOrigin) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean match(final Cookie cookie, final CookieOrigin cookieOrigin) {
        throw new RuntimeException("Stub!");
    }
    
    protected List<Cookie> parse(final HeaderElement[] array, final CookieOrigin cookieOrigin) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void validate(final Cookie cookie, final CookieOrigin cookieOrigin) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
}
