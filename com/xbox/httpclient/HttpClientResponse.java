package com.xbox.httpclient;

import okhttp3.*;
import java.io.*;

class HttpClientResponse
{
    private Response response;
    
    public HttpClientResponse(final Response response) {
        this.response = response;
    }
    
    public String getHeaderNameAtIndex(final int n) {
        if (n >= 0 && n < this.response.headers().size()) {
            return this.response.headers().name(n);
        }
        return null;
    }
    
    public String getHeaderValueAtIndex(final int n) {
        if (n >= 0 && n < this.response.headers().size()) {
            return this.response.headers().value(n);
        }
        return null;
    }
    
    public int getNumHeaders() {
        return this.response.headers().size();
    }
    
    public byte[] getResponseBodyBytes() {
        try {
            return this.response.body().bytes();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    public int getResponseCode() {
        return this.response.code();
    }
}
