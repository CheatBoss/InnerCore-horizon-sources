package com.xbox.httpclient;

import android.content.*;
import android.net.*;
import java.io.*;
import okhttp3.*;

public class HttpClientRequest
{
    private static final byte[] NO_BODY;
    private static OkHttpClient OK_CLIENT;
    private Request okHttpRequest;
    private Request.Builder requestBuilder;
    
    static {
        NO_BODY = new byte[0];
        HttpClientRequest.OK_CLIENT = new OkHttpClient.Builder().retryOnConnectionFailure(false).build();
    }
    
    public HttpClientRequest() {
        this.requestBuilder = new Request.Builder();
    }
    
    private native void OnRequestCompleted(final long p0, final HttpClientResponse p1);
    
    private native void OnRequestFailed(final long p0, final String p1);
    
    public static HttpClientRequest createClientRequest() {
        return new HttpClientRequest();
    }
    
    public static boolean isNetworkAvailable(final Context context) {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    public void doRequestAsync(final long n) {
        HttpClientRequest.OK_CLIENT.newCall(this.requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException ex) {
                HttpClientRequest.this.OnRequestFailed(n, ex.getClass().getCanonicalName());
            }
            
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                HttpClientRequest.this.OnRequestCompleted(n, new HttpClientResponse(response));
            }
        });
    }
    
    public void setHttpHeader(final String s, final String s2) {
        this.requestBuilder = this.requestBuilder.addHeader(s, s2);
    }
    
    public void setHttpMethodAndBody(final String s, final String s2, final byte[] array) {
        Request.Builder requestBuilder;
        if (array != null && array.length != 0) {
            requestBuilder = this.requestBuilder.method(s, RequestBody.create(MediaType.parse(s2), array));
        }
        else {
            final boolean equals = "POST".equals(s);
            RequestBody create = null;
            Request.Builder builder;
            if (!equals && !"PUT".equals(s)) {
                builder = this.requestBuilder;
            }
            else {
                builder = this.requestBuilder;
                create = RequestBody.create(null, HttpClientRequest.NO_BODY);
            }
            requestBuilder = builder.method(s, create);
        }
        this.requestBuilder = requestBuilder;
    }
    
    public void setHttpUrl(final String s) {
        this.requestBuilder = this.requestBuilder.url(s);
    }
}
