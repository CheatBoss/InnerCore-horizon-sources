package org.apache.http.impl.auth;

import org.apache.http.params.*;
import org.apache.http.auth.*;

@Deprecated
public class BasicSchemeFactory implements AuthSchemeFactory
{
    public BasicSchemeFactory() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public AuthScheme newInstance(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
}
