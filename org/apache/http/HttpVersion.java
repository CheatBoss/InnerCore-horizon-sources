package org.apache.http;

import java.io.*;

@Deprecated
public final class HttpVersion extends ProtocolVersion implements Serializable
{
    public static final String HTTP = "HTTP";
    public static final HttpVersion HTTP_0_9;
    public static final HttpVersion HTTP_1_0;
    public static final HttpVersion HTTP_1_1;
    
    public HttpVersion(final int n, final int n2) {
        super(null, 0, 0);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public ProtocolVersion forVersion(final int n, final int n2) {
        throw new RuntimeException("Stub!");
    }
}
