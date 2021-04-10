package com.microsoft.xbox.toolkit.network;

import org.apache.http.impl.client.*;
import org.apache.http.conn.*;
import org.apache.http.params.*;
import org.apache.http.client.methods.*;
import org.apache.http.*;
import org.apache.http.protocol.*;
import org.apache.http.client.*;
import java.io.*;

public class XLEHttpClient extends AbstractXLEHttpClient
{
    DefaultHttpClient client;
    
    public XLEHttpClient(final ClientConnectionManager clientConnectionManager, final HttpParams httpParams) {
        this.client = new DefaultHttpClient(clientConnectionManager, httpParams);
    }
    
    @Override
    protected HttpResponse execute(final HttpUriRequest httpUriRequest) throws ClientProtocolException, IOException {
        return this.client.execute(httpUriRequest, (HttpContext)new BasicHttpContext());
    }
}
