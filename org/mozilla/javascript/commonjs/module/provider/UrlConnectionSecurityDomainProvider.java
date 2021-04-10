package org.mozilla.javascript.commonjs.module.provider;

import java.net.*;

public interface UrlConnectionSecurityDomainProvider
{
    Object getSecurityDomain(final URLConnection p0);
}
