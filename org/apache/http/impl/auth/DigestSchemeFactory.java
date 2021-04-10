package org.apache.http.impl.auth;

import org.apache.http.params.*;
import org.apache.http.auth.*;

@Deprecated
public class DigestSchemeFactory implements AuthSchemeFactory
{
    public DigestSchemeFactory() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public AuthScheme newInstance(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
}
