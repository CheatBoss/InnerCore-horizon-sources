package okhttp3.internal.cache;

import javax.annotation.*;
import java.util.*;
import okhttp3.internal.http.*;
import java.util.concurrent.*;
import okhttp3.*;
import okhttp3.internal.*;

public final class CacheStrategy
{
    @Nullable
    public final Response cacheResponse;
    @Nullable
    public final Request networkRequest;
    
    CacheStrategy(final Request networkRequest, final Response cacheResponse) {
        this.networkRequest = networkRequest;
        this.cacheResponse = cacheResponse;
    }
    
    public static boolean isCacheable(final Response response, final Request request) {
        final int code = response.code();
        final boolean b = false;
        Label_0151: {
            if (code != 200 && code != 410 && code != 414 && code != 501 && code != 203 && code != 204) {
                if (code != 307) {
                    if (code == 308 || code == 404 || code == 405) {
                        break Label_0151;
                    }
                    switch (code) {
                        default: {
                            return false;
                        }
                        case 302: {
                            break;
                        }
                        case 300:
                        case 301: {
                            break Label_0151;
                        }
                    }
                }
                if (response.header("Expires") == null && response.cacheControl().maxAgeSeconds() == -1 && !response.cacheControl().isPublic()) {
                    if (!response.cacheControl().isPrivate()) {
                        return false;
                    }
                }
            }
        }
        boolean b2 = b;
        if (!response.cacheControl().noStore()) {
            b2 = b;
            if (!request.cacheControl().noStore()) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public static class Factory
    {
        private int ageSeconds;
        final Response cacheResponse;
        private String etag;
        private Date expires;
        private Date lastModified;
        private String lastModifiedString;
        final long nowMillis;
        private long receivedResponseMillis;
        final Request request;
        private long sentRequestMillis;
        private Date servedDate;
        private String servedDateString;
        
        public Factory(final long nowMillis, final Request request, final Response cacheResponse) {
            this.ageSeconds = -1;
            this.nowMillis = nowMillis;
            this.request = request;
            this.cacheResponse = cacheResponse;
            if (cacheResponse != null) {
                this.sentRequestMillis = cacheResponse.sentRequestAtMillis();
                this.receivedResponseMillis = cacheResponse.receivedResponseAtMillis();
                final Headers headers = cacheResponse.headers();
                for (int i = 0; i < headers.size(); ++i) {
                    final String name = headers.name(i);
                    final String value = headers.value(i);
                    if ("Date".equalsIgnoreCase(name)) {
                        this.servedDate = HttpDate.parse(value);
                        this.servedDateString = value;
                    }
                    else if ("Expires".equalsIgnoreCase(name)) {
                        this.expires = HttpDate.parse(value);
                    }
                    else if ("Last-Modified".equalsIgnoreCase(name)) {
                        this.lastModified = HttpDate.parse(value);
                        this.lastModifiedString = value;
                    }
                    else if ("ETag".equalsIgnoreCase(name)) {
                        this.etag = value;
                    }
                    else if ("Age".equalsIgnoreCase(name)) {
                        this.ageSeconds = HttpHeaders.parseSeconds(value, -1);
                    }
                }
            }
        }
        
        private long cacheResponseAge() {
            final Date servedDate = this.servedDate;
            long max = 0L;
            if (servedDate != null) {
                max = Math.max(0L, this.receivedResponseMillis - servedDate.getTime());
            }
            long max2 = max;
            if (this.ageSeconds != -1) {
                max2 = Math.max(max, TimeUnit.SECONDS.toMillis(this.ageSeconds));
            }
            final long receivedResponseMillis = this.receivedResponseMillis;
            return max2 + (receivedResponseMillis - this.sentRequestMillis) + (this.nowMillis - receivedResponseMillis);
        }
        
        private long computeFreshnessLifetime() {
            final CacheControl cacheControl = this.cacheResponse.cacheControl();
            if (cacheControl.maxAgeSeconds() != -1) {
                return TimeUnit.SECONDS.toMillis(cacheControl.maxAgeSeconds());
            }
            final Date expires = this.expires;
            long n = 0L;
            if (expires != null) {
                final Date servedDate = this.servedDate;
                long n2;
                if (servedDate != null) {
                    n2 = servedDate.getTime();
                }
                else {
                    n2 = this.receivedResponseMillis;
                }
                final long n3 = this.expires.getTime() - n2;
                if (n3 > 0L) {
                    n = n3;
                }
                return n;
            }
            long n4 = n;
            if (this.lastModified != null) {
                n4 = n;
                if (this.cacheResponse.request().url().query() == null) {
                    final Date servedDate2 = this.servedDate;
                    long n5;
                    if (servedDate2 != null) {
                        n5 = servedDate2.getTime();
                    }
                    else {
                        n5 = this.sentRequestMillis;
                    }
                    final long n6 = n5 - this.lastModified.getTime();
                    n4 = n;
                    if (n6 > 0L) {
                        n4 = n6 / 10L;
                    }
                }
            }
            return n4;
        }
        
        private CacheStrategy getCandidate() {
            if (this.cacheResponse == null) {
                return new CacheStrategy(this.request, null);
            }
            if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
                return new CacheStrategy(this.request, null);
            }
            if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
                return new CacheStrategy(this.request, null);
            }
            final CacheControl cacheControl = this.request.cacheControl();
            if (cacheControl.noCache() || hasConditions(this.request)) {
                return new CacheStrategy(this.request, null);
            }
            final CacheControl cacheControl2 = this.cacheResponse.cacheControl();
            if (cacheControl2.immutable()) {
                return new CacheStrategy(null, this.cacheResponse);
            }
            final long cacheResponseAge = this.cacheResponseAge();
            long n2;
            final long n = n2 = this.computeFreshnessLifetime();
            if (cacheControl.maxAgeSeconds() != -1) {
                n2 = Math.min(n, TimeUnit.SECONDS.toMillis(cacheControl.maxAgeSeconds()));
            }
            final int minFreshSeconds = cacheControl.minFreshSeconds();
            final long n3 = 0L;
            long millis;
            if (minFreshSeconds != -1) {
                millis = TimeUnit.SECONDS.toMillis(cacheControl.minFreshSeconds());
            }
            else {
                millis = 0L;
            }
            long millis2 = n3;
            if (!cacheControl2.mustRevalidate()) {
                millis2 = n3;
                if (cacheControl.maxStaleSeconds() != -1) {
                    millis2 = TimeUnit.SECONDS.toMillis(cacheControl.maxStaleSeconds());
                }
            }
            if (!cacheControl2.noCache()) {
                final long n4 = millis + cacheResponseAge;
                if (n4 < millis2 + n2) {
                    final Response.Builder builder = this.cacheResponse.newBuilder();
                    if (n4 >= n2) {
                        builder.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
                    }
                    if (cacheResponseAge > 86400000L && this.isFreshnessLifetimeHeuristic()) {
                        builder.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                    }
                    return new CacheStrategy(null, builder.build());
                }
            }
            String s = this.etag;
            String s2 = "If-Modified-Since";
            if (s != null) {
                s2 = "If-None-Match";
            }
            else if (this.lastModified != null) {
                s = this.lastModifiedString;
            }
            else {
                if (this.servedDate == null) {
                    return new CacheStrategy(this.request, null);
                }
                s = this.servedDateString;
            }
            final Headers.Builder builder2 = this.request.headers().newBuilder();
            Internal.instance.addLenient(builder2, s2, s);
            return new CacheStrategy(this.request.newBuilder().headers(builder2.build()).build(), this.cacheResponse);
        }
        
        private static boolean hasConditions(final Request request) {
            return request.header("If-Modified-Since") != null || request.header("If-None-Match") != null;
        }
        
        private boolean isFreshnessLifetimeHeuristic() {
            return this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
        }
        
        public CacheStrategy get() {
            CacheStrategy candidate;
            final CacheStrategy cacheStrategy = candidate = this.getCandidate();
            if (cacheStrategy.networkRequest != null) {
                candidate = cacheStrategy;
                if (this.request.cacheControl().onlyIfCached()) {
                    candidate = new CacheStrategy(null, null);
                }
            }
            return candidate;
        }
    }
}
