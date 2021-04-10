package org.apache.http.impl.client;

import org.apache.http.client.*;
import org.apache.http.protocol.*;

@Deprecated
public class DefaultUserTokenHandler implements UserTokenHandler
{
    public DefaultUserTokenHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object getUserToken(final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
