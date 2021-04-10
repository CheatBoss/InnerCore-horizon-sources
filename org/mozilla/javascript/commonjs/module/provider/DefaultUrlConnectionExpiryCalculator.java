package org.mozilla.javascript.commonjs.module.provider;

import java.io.*;
import java.net.*;

public class DefaultUrlConnectionExpiryCalculator implements UrlConnectionExpiryCalculator, Serializable
{
    private static final long serialVersionUID = 1L;
    private final long relativeExpiry;
    
    public DefaultUrlConnectionExpiryCalculator() {
        this(60000L);
    }
    
    public DefaultUrlConnectionExpiryCalculator(final long relativeExpiry) {
        if (relativeExpiry < 0L) {
            throw new IllegalArgumentException("relativeExpiry < 0");
        }
        this.relativeExpiry = relativeExpiry;
    }
    
    @Override
    public long calculateExpiry(final URLConnection urlConnection) {
        return System.currentTimeMillis() + this.relativeExpiry;
    }
}
