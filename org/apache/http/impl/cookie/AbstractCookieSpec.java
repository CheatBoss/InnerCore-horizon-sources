package org.apache.http.impl.cookie;

import org.apache.http.cookie.*;
import java.util.*;

@Deprecated
public abstract class AbstractCookieSpec implements CookieSpec
{
    public AbstractCookieSpec() {
        throw new RuntimeException("Stub!");
    }
    
    protected CookieAttributeHandler findAttribHandler(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    protected CookieAttributeHandler getAttribHandler(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    protected Collection<CookieAttributeHandler> getAttribHandlers() {
        throw new RuntimeException("Stub!");
    }
    
    public void registerAttribHandler(final String s, final CookieAttributeHandler cookieAttributeHandler) {
        throw new RuntimeException("Stub!");
    }
}
