package org.apache.http.impl.cookie;

import java.util.*;
import org.apache.http.*;
import org.apache.http.cookie.*;

@Deprecated
public class BestMatchSpec implements CookieSpec
{
    public BestMatchSpec() {
        throw new RuntimeException("Stub!");
    }
    
    public BestMatchSpec(final String[] array, final boolean b) {
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
    public boolean match(final Cookie cookie, final CookieOrigin cookieOrigin) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public List<Cookie> parse(final Header header, final CookieOrigin cookieOrigin) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void validate(final Cookie cookie, final CookieOrigin cookieOrigin) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
}
