package org.apache.http.client;

@Deprecated
public class HttpResponseException extends ClientProtocolException
{
    public HttpResponseException(final int n, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public int getStatusCode() {
        throw new RuntimeException("Stub!");
    }
}
