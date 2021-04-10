package org.apache.http.impl.cookie;

import org.apache.http.util.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.cookie.*;

@Deprecated
public class RFC2109Spec extends CookieSpecBase
{
    public RFC2109Spec() {
        throw new RuntimeException("Stub!");
    }
    
    public RFC2109Spec(final String[] array, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    protected void formatCookieAsVer(final CharArrayBuffer charArrayBuffer, final Cookie cookie, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public List<Header> formatCookies(final List<Cookie> list) {
        throw new RuntimeException("Stub!");
    }
    
    protected void formatParamAsVer(final CharArrayBuffer charArrayBuffer, final String s, final String s2, final int n) {
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
    
    @Override
    public void validate(final Cookie cookie, final CookieOrigin cookieOrigin) throws MalformedCookieException {
        throw new RuntimeException("Stub!");
    }
}
