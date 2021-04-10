package org.apache.http.impl.cookie;

import org.apache.http.util.*;
import org.apache.http.*;
import java.util.*;
import org.apache.http.cookie.*;

@Deprecated
public class RFC2965Spec extends RFC2109Spec
{
    public RFC2965Spec() {
        throw new RuntimeException("Stub!");
    }
    
    public RFC2965Spec(final String[] array, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void formatCookieAsVer(final CharArrayBuffer charArrayBuffer, final Cookie cookie, final int n) {
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
