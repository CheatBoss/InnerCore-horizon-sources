package org.apache.http.cookie;

import java.util.*;
import org.apache.http.*;

@Deprecated
public interface CookieSpec
{
    List<Header> formatCookies(final List<Cookie> p0);
    
    int getVersion();
    
    Header getVersionHeader();
    
    boolean match(final Cookie p0, final CookieOrigin p1);
    
    List<Cookie> parse(final Header p0, final CookieOrigin p1) throws MalformedCookieException;
    
    void validate(final Cookie p0, final CookieOrigin p1) throws MalformedCookieException;
}
