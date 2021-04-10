package org.apache.http.impl.cookie;

import org.apache.http.cookie.*;

@Deprecated
public class RFC2965DiscardAttributeHandler implements CookieAttributeHandler
{
    public RFC2965DiscardAttributeHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean match(final Cookie cookie, final CookieOrigin cookieOrigin) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void parse(final SetCookie setCookie, final String s) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void validate(final Cookie cookie, final CookieOrigin cookieOrigin) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
}
