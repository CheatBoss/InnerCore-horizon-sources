package org.apache.http.client;

import org.apache.http.protocol.*;

@Deprecated
public interface UserTokenHandler
{
    Object getUserToken(final HttpContext p0);
}
