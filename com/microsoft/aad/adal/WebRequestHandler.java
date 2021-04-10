package com.microsoft.aad.adal;

import java.util.*;
import java.net.*;
import android.os.*;
import java.io.*;

public class WebRequestHandler implements IWebRequestHandler
{
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_JSON = "application/json";
    private static final String TAG = "WebRequestHandler";
    private UUID mRequestCorrelationId;
    
    public WebRequestHandler() {
        this.mRequestCorrelationId = null;
    }
    
    private Map<String, String> updateHeaders(final Map<String, String> map) {
        final UUID mRequestCorrelationId = this.mRequestCorrelationId;
        if (mRequestCorrelationId != null) {
            map.put("client-request-id", mRequestCorrelationId.toString());
        }
        map.put("x-client-SKU", "Android");
        map.put("x-client-Ver", AuthenticationContext.getVersionName());
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(Build$VERSION.SDK_INT);
        map.put("x-client-OS", sb.toString());
        map.put("x-client-DM", Build.MODEL);
        return map;
    }
    
    @Override
    public HttpWebResponse sendGet(final URL url, final Map<String, String> map) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("WebRequestHandler thread");
        sb.append(Process.myTid());
        Logger.v("WebRequestHandler", sb.toString());
        return new HttpWebRequest(url, "GET", this.updateHeaders(map)).send();
    }
    
    @Override
    public HttpWebResponse sendPost(final URL url, final Map<String, String> map, final byte[] array, final String s) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("WebRequestHandler thread");
        sb.append(Process.myTid());
        Logger.v("WebRequestHandler", sb.toString());
        return new HttpWebRequest(url, "POST", this.updateHeaders(map), array, s).send();
    }
    
    @Override
    public void setRequestCorrelationId(final UUID mRequestCorrelationId) {
        this.mRequestCorrelationId = mRequestCorrelationId;
    }
}
