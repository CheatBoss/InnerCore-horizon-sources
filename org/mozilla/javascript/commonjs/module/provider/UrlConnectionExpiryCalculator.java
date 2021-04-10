package org.mozilla.javascript.commonjs.module.provider;

import java.net.*;

public interface UrlConnectionExpiryCalculator
{
    long calculateExpiry(final URLConnection p0);
}
