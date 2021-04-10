package org.apache.http.impl.cookie;

import org.apache.http.cookie.*;

@Deprecated
public class NetscapeDomainHandler extends BasicDomainHandler
{
    public NetscapeDomainHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean match(final Cookie cookie, final CookieOrigin cookieOrigin) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void validate(final Cookie cookie, final CookieOrigin cookieOrigin) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
}
