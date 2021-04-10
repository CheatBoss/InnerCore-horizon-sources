package okhttp3;

import javax.annotation.*;
import java.util.*;
import okhttp3.internal.*;
import okhttp3.internal.http.*;
import java.net.*;

public final class Request
{
    @Nullable
    final RequestBody body;
    private volatile CacheControl cacheControl;
    final Headers headers;
    final String method;
    final Object tag;
    final HttpUrl url;
    
    Request(final Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers.build();
        this.body = builder.body;
        Object tag;
        if (builder.tag != null) {
            tag = builder.tag;
        }
        else {
            tag = this;
        }
        this.tag = tag;
    }
    
    @Nullable
    public RequestBody body() {
        return this.body;
    }
    
    public CacheControl cacheControl() {
        final CacheControl cacheControl = this.cacheControl;
        if (cacheControl != null) {
            return cacheControl;
        }
        return this.cacheControl = CacheControl.parse(this.headers);
    }
    
    @Nullable
    public String header(final String s) {
        return this.headers.get(s);
    }
    
    public List<String> headers(final String s) {
        return this.headers.values(s);
    }
    
    public Headers headers() {
        return this.headers;
    }
    
    public boolean isHttps() {
        return this.url.isHttps();
    }
    
    public String method() {
        return this.method;
    }
    
    public Builder newBuilder() {
        return new Builder(this);
    }
    
    public Object tag() {
        return this.tag;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Request{method=");
        sb.append(this.method);
        sb.append(", url=");
        sb.append(this.url);
        sb.append(", tag=");
        Object tag = this.tag;
        if (tag == this) {
            tag = null;
        }
        sb.append(tag);
        sb.append('}');
        return sb.toString();
    }
    
    public HttpUrl url() {
        return this.url;
    }
    
    public static class Builder
    {
        RequestBody body;
        Headers.Builder headers;
        String method;
        Object tag;
        HttpUrl url;
        
        public Builder() {
            this.method = "GET";
            this.headers = new Headers.Builder();
        }
        
        Builder(final Request request) {
            this.url = request.url;
            this.method = request.method;
            this.body = request.body;
            this.tag = request.tag;
            this.headers = request.headers.newBuilder();
        }
        
        public Builder addHeader(final String s, final String s2) {
            this.headers.add(s, s2);
            return this;
        }
        
        public Request build() {
            if (this.url != null) {
                return new Request(this);
            }
            throw new IllegalStateException("url == null");
        }
        
        public Builder cacheControl(final CacheControl cacheControl) {
            final String string = cacheControl.toString();
            if (string.isEmpty()) {
                return this.removeHeader("Cache-Control");
            }
            return this.header("Cache-Control", string);
        }
        
        public Builder delete() {
            return this.delete(Util.EMPTY_REQUEST);
        }
        
        public Builder delete(@Nullable final RequestBody requestBody) {
            return this.method("DELETE", requestBody);
        }
        
        public Builder get() {
            return this.method("GET", null);
        }
        
        public Builder head() {
            return this.method("HEAD", null);
        }
        
        public Builder header(final String s, final String s2) {
            this.headers.set(s, s2);
            return this;
        }
        
        public Builder headers(final Headers headers) {
            this.headers = headers.newBuilder();
            return this;
        }
        
        public Builder method(final String method, @Nullable final RequestBody body) {
            if (method == null) {
                throw new NullPointerException("method == null");
            }
            if (method.length() == 0) {
                throw new IllegalArgumentException("method.length() == 0");
            }
            if (body != null && !HttpMethod.permitsRequestBody(method)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("method ");
                sb.append(method);
                sb.append(" must not have a request body.");
                throw new IllegalArgumentException(sb.toString());
            }
            if (body == null && HttpMethod.requiresRequestBody(method)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("method ");
                sb2.append(method);
                sb2.append(" must have a request body.");
                throw new IllegalArgumentException(sb2.toString());
            }
            this.method = method;
            this.body = body;
            return this;
        }
        
        public Builder patch(final RequestBody requestBody) {
            return this.method("PATCH", requestBody);
        }
        
        public Builder post(final RequestBody requestBody) {
            return this.method("POST", requestBody);
        }
        
        public Builder put(final RequestBody requestBody) {
            return this.method("PUT", requestBody);
        }
        
        public Builder removeHeader(final String s) {
            this.headers.removeAll(s);
            return this;
        }
        
        public Builder tag(final Object tag) {
            this.tag = tag;
            return this;
        }
        
        public Builder url(final String s) {
            if (s == null) {
                throw new NullPointerException("url == null");
            }
            String string = null;
            Label_0087: {
                StringBuilder sb;
                int n;
                if (s.regionMatches(true, 0, "ws:", 0, 3)) {
                    sb = new StringBuilder();
                    sb.append("http:");
                    n = 3;
                }
                else {
                    string = s;
                    if (!s.regionMatches(true, 0, "wss:", 0, 4)) {
                        break Label_0087;
                    }
                    sb = new StringBuilder();
                    sb.append("https:");
                    n = 4;
                }
                sb.append(s.substring(n));
                string = sb.toString();
            }
            final HttpUrl parse = HttpUrl.parse(string);
            if (parse != null) {
                return this.url(parse);
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("unexpected url: ");
            sb2.append(string);
            throw new IllegalArgumentException(sb2.toString());
        }
        
        public Builder url(final URL url) {
            if (url == null) {
                throw new NullPointerException("url == null");
            }
            final HttpUrl value = HttpUrl.get(url);
            if (value != null) {
                return this.url(value);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected url: ");
            sb.append(url);
            throw new IllegalArgumentException(sb.toString());
        }
        
        public Builder url(final HttpUrl url) {
            if (url != null) {
                this.url = url;
                return this;
            }
            throw new NullPointerException("url == null");
        }
    }
}
