package com.bumptech.glide.load.data;

import com.bumptech.glide.load.model.*;
import android.text.*;
import com.bumptech.glide.util.*;
import android.util.*;
import java.io.*;
import java.net.*;
import java.util.*;
import com.bumptech.glide.*;

public class HttpUrlFetcher implements DataFetcher<InputStream>
{
    private static final HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY;
    private static final String DEFAULT_ENCODING = "identity";
    private static final String ENCODING_HEADER = "Accept-Encoding";
    private static final int MAXIMUM_REDIRECTS = 5;
    private static final String TAG = "HttpUrlFetcher";
    private final HttpUrlConnectionFactory connectionFactory;
    private final GlideUrl glideUrl;
    private volatile boolean isCancelled;
    private InputStream stream;
    private HttpURLConnection urlConnection;
    
    static {
        DEFAULT_CONNECTION_FACTORY = (HttpUrlConnectionFactory)new DefaultHttpUrlConnectionFactory();
    }
    
    public HttpUrlFetcher(final GlideUrl glideUrl) {
        this(glideUrl, HttpUrlFetcher.DEFAULT_CONNECTION_FACTORY);
    }
    
    HttpUrlFetcher(final GlideUrl glideUrl, final HttpUrlConnectionFactory connectionFactory) {
        this.glideUrl = glideUrl;
        this.connectionFactory = connectionFactory;
    }
    
    private InputStream getStreamForSuccessfulRequest(final HttpURLConnection httpURLConnection) throws IOException {
        if (TextUtils.isEmpty((CharSequence)httpURLConnection.getContentEncoding())) {
            this.stream = ContentLengthInputStream.obtain(httpURLConnection.getInputStream(), httpURLConnection.getContentLength());
        }
        else {
            if (Log.isLoggable("HttpUrlFetcher", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Got non empty content encoding: ");
                sb.append(httpURLConnection.getContentEncoding());
                Log.d("HttpUrlFetcher", sb.toString());
            }
            this.stream = httpURLConnection.getInputStream();
        }
        return this.stream;
    }
    
    private InputStream loadDataWithRedirects(final URL url, final int n, final URL url2, final Map<String, String> map) throws IOException {
        if (n >= 5) {
            throw new IOException("Too many (> 5) redirects!");
        }
        if (url2 != null) {
            try {
                if (url.toURI().equals(url2.toURI())) {
                    throw new IOException("In re-direct loop");
                }
            }
            catch (URISyntaxException ex) {}
        }
        this.urlConnection = this.connectionFactory.build(url);
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            this.urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
        }
        if (TextUtils.isEmpty((CharSequence)this.urlConnection.getRequestProperty("Accept-Encoding"))) {
            this.urlConnection.setRequestProperty("Accept-Encoding", "identity");
        }
        this.urlConnection.setConnectTimeout(2500);
        this.urlConnection.setReadTimeout(2500);
        this.urlConnection.setUseCaches(false);
        this.urlConnection.setDoInput(true);
        this.urlConnection.connect();
        if (this.isCancelled) {
            return null;
        }
        final int responseCode = this.urlConnection.getResponseCode();
        if (responseCode / 100 == 2) {
            return this.getStreamForSuccessfulRequest(this.urlConnection);
        }
        if (responseCode / 100 == 3) {
            final String headerField = this.urlConnection.getHeaderField("Location");
            if (TextUtils.isEmpty((CharSequence)headerField)) {
                throw new IOException("Received empty or null redirect url");
            }
            return this.loadDataWithRedirects(new URL(url, headerField), n + 1, url, map);
        }
        else {
            if (responseCode == -1) {
                throw new IOException("Unable to retrieve response code from HttpUrlConnection.");
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Request failed ");
            sb.append(responseCode);
            sb.append(": ");
            sb.append(this.urlConnection.getResponseMessage());
            throw new IOException(sb.toString());
        }
    }
    
    @Override
    public void cancel() {
        this.isCancelled = true;
    }
    
    @Override
    public void cleanup() {
        if (this.stream != null) {
            try {
                this.stream.close();
            }
            catch (IOException ex) {}
        }
        if (this.urlConnection != null) {
            this.urlConnection.disconnect();
        }
    }
    
    @Override
    public String getId() {
        return this.glideUrl.getCacheKey();
    }
    
    @Override
    public InputStream loadData(final Priority priority) throws Exception {
        return this.loadDataWithRedirects(this.glideUrl.toURL(), 0, null, this.glideUrl.getHeaders());
    }
    
    private static class DefaultHttpUrlConnectionFactory implements HttpUrlConnectionFactory
    {
        @Override
        public HttpURLConnection build(final URL url) throws IOException {
            return (HttpURLConnection)url.openConnection();
        }
    }
    
    interface HttpUrlConnectionFactory
    {
        HttpURLConnection build(final URL p0) throws IOException;
    }
}
