package com.microsoft.aad.adal;

import java.util.*;

public class HttpWebResponse
{
    private final String mResponseBody;
    private final Map<String, List<String>> mResponseHeaders;
    private final int mStatusCode;
    
    public HttpWebResponse(final int mStatusCode, final String mResponseBody, final Map<String, List<String>> mResponseHeaders) {
        this.mStatusCode = mStatusCode;
        this.mResponseBody = mResponseBody;
        this.mResponseHeaders = mResponseHeaders;
    }
    
    public String getBody() {
        return this.mResponseBody;
    }
    
    public Map<String, List<String>> getResponseHeaders() {
        return this.mResponseHeaders;
    }
    
    public int getStatusCode() {
        return this.mStatusCode;
    }
}
