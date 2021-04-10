package com.bumptech.glide.load.model;

import android.text.*;
import android.net.*;
import java.net.*;
import java.util.*;

public class GlideUrl
{
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private final Headers headers;
    private String safeStringUrl;
    private URL safeUrl;
    private final String stringUrl;
    private final URL url;
    
    public GlideUrl(final String s) {
        this(s, Headers.NONE);
    }
    
    public GlideUrl(final String stringUrl, final Headers headers) {
        if (TextUtils.isEmpty((CharSequence)stringUrl)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("String url must not be empty or null: ");
            sb.append(stringUrl);
            throw new IllegalArgumentException(sb.toString());
        }
        if (headers == null) {
            throw new IllegalArgumentException("Headers must not be null");
        }
        this.stringUrl = stringUrl;
        this.url = null;
        this.headers = headers;
    }
    
    public GlideUrl(final URL url) {
        this(url, Headers.NONE);
    }
    
    public GlideUrl(final URL url, final Headers headers) {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null!");
        }
        if (headers == null) {
            throw new IllegalArgumentException("Headers must not be null");
        }
        this.url = url;
        this.stringUrl = null;
        this.headers = headers;
    }
    
    private String getSafeStringUrl() {
        if (TextUtils.isEmpty((CharSequence)this.safeStringUrl)) {
            String s;
            if (TextUtils.isEmpty((CharSequence)(s = this.stringUrl))) {
                s = this.url.toString();
            }
            this.safeStringUrl = Uri.encode(s, "@#&=*+-_.,:!?()/~'%");
        }
        return this.safeStringUrl;
    }
    
    private URL getSafeUrl() throws MalformedURLException {
        if (this.safeUrl == null) {
            this.safeUrl = new URL(this.getSafeStringUrl());
        }
        return this.safeUrl;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof GlideUrl;
        final boolean b2 = false;
        if (b) {
            final GlideUrl glideUrl = (GlideUrl)o;
            boolean b3 = b2;
            if (this.getCacheKey().equals(glideUrl.getCacheKey())) {
                b3 = b2;
                if (this.headers.equals(glideUrl.headers)) {
                    b3 = true;
                }
            }
            return b3;
        }
        return false;
    }
    
    public String getCacheKey() {
        if (this.stringUrl != null) {
            return this.stringUrl;
        }
        return this.url.toString();
    }
    
    public Map<String, String> getHeaders() {
        return this.headers.getHeaders();
    }
    
    @Override
    public int hashCode() {
        return this.getCacheKey().hashCode() * 31 + this.headers.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getCacheKey());
        sb.append('\n');
        sb.append(this.headers.toString());
        return sb.toString();
    }
    
    public String toStringUrl() {
        return this.getSafeStringUrl();
    }
    
    public URL toURL() throws MalformedURLException {
        return this.getSafeUrl();
    }
}
