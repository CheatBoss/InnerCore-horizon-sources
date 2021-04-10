package org.apache.http.conn;

import java.net.*;
import org.apache.http.*;

@Deprecated
public class HttpHostConnectException extends ConnectException
{
    public HttpHostConnectException(final HttpHost httpHost, final ConnectException ex) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpHost getHost() {
        throw new RuntimeException("Stub!");
    }
}
