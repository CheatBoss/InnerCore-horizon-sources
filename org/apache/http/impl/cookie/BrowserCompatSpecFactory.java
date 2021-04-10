package org.apache.http.impl.cookie;

import org.apache.http.params.*;
import org.apache.http.cookie.*;

@Deprecated
public class BrowserCompatSpecFactory implements CookieSpecFactory
{
    public BrowserCompatSpecFactory() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CookieSpec newInstance(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
}
