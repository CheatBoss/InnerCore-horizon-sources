package org.apache.http.cookie;

@Deprecated
public interface CookieAttributeHandler
{
    boolean match(final Cookie p0, final CookieOrigin p1);
    
    void parse(final SetCookie p0, final String p1) throws MalformedCookieException;
    
    void validate(final Cookie p0, final CookieOrigin p1) throws MalformedCookieException;
}
