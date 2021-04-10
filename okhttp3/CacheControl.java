package okhttp3;

import javax.annotation.*;
import java.util.concurrent.*;
import okhttp3.internal.http.*;

public final class CacheControl
{
    public static final CacheControl FORCE_CACHE;
    public static final CacheControl FORCE_NETWORK;
    @Nullable
    String headerValue;
    private final boolean immutable;
    private final boolean isPrivate;
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;
    
    static {
        FORCE_NETWORK = new Builder().noCache().build();
        FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    }
    
    CacheControl(final Builder builder) {
        this.noCache = builder.noCache;
        this.noStore = builder.noStore;
        this.maxAgeSeconds = builder.maxAgeSeconds;
        this.sMaxAgeSeconds = -1;
        this.isPrivate = false;
        this.isPublic = false;
        this.mustRevalidate = false;
        this.maxStaleSeconds = builder.maxStaleSeconds;
        this.minFreshSeconds = builder.minFreshSeconds;
        this.onlyIfCached = builder.onlyIfCached;
        this.noTransform = builder.noTransform;
        this.immutable = builder.immutable;
    }
    
    private CacheControl(final boolean noCache, final boolean noStore, final int maxAgeSeconds, final int sMaxAgeSeconds, final boolean isPrivate, final boolean isPublic, final boolean mustRevalidate, final int maxStaleSeconds, final int minFreshSeconds, final boolean onlyIfCached, final boolean noTransform, final boolean immutable, @Nullable final String headerValue) {
        this.noCache = noCache;
        this.noStore = noStore;
        this.maxAgeSeconds = maxAgeSeconds;
        this.sMaxAgeSeconds = sMaxAgeSeconds;
        this.isPrivate = isPrivate;
        this.isPublic = isPublic;
        this.mustRevalidate = mustRevalidate;
        this.maxStaleSeconds = maxStaleSeconds;
        this.minFreshSeconds = minFreshSeconds;
        this.onlyIfCached = onlyIfCached;
        this.noTransform = noTransform;
        this.immutable = immutable;
        this.headerValue = headerValue;
    }
    
    private String headerValue() {
        final StringBuilder sb = new StringBuilder();
        if (this.noCache) {
            sb.append("no-cache, ");
        }
        if (this.noStore) {
            sb.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            sb.append("max-age=");
            sb.append(this.maxAgeSeconds);
            sb.append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            sb.append("s-maxage=");
            sb.append(this.sMaxAgeSeconds);
            sb.append(", ");
        }
        if (this.isPrivate) {
            sb.append("private, ");
        }
        if (this.isPublic) {
            sb.append("public, ");
        }
        if (this.mustRevalidate) {
            sb.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            sb.append("max-stale=");
            sb.append(this.maxStaleSeconds);
            sb.append(", ");
        }
        if (this.minFreshSeconds != -1) {
            sb.append("min-fresh=");
            sb.append(this.minFreshSeconds);
            sb.append(", ");
        }
        if (this.onlyIfCached) {
            sb.append("only-if-cached, ");
        }
        if (this.noTransform) {
            sb.append("no-transform, ");
        }
        if (this.immutable) {
            sb.append("immutable, ");
        }
        if (sb.length() == 0) {
            return "";
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
    
    public static CacheControl parse(final Headers headers) {
        final int size = headers.size();
        int i = 0;
        int n = 1;
        String s = null;
        boolean b = false;
        boolean b2 = false;
        int n2 = -1;
        int n3 = -1;
        boolean b3 = false;
        boolean b4 = false;
        boolean b5 = false;
        int n4 = -1;
        int n5 = -1;
        boolean b6 = false;
        boolean b7 = false;
        boolean b8 = false;
        while (i < size) {
            final String name = headers.name(i);
            final String value = headers.value(i);
            int n6 = 0;
            String s2 = null;
            boolean b9 = false;
            boolean b10 = false;
            int n7 = 0;
            int n8 = 0;
            boolean b11 = false;
            boolean b12 = false;
            boolean b13 = false;
            int n9 = 0;
            int n10 = 0;
            boolean b14 = false;
            boolean b15 = false;
            boolean b16 = false;
            Label_1158: {
                Label_0162: {
                    if (name.equalsIgnoreCase("Cache-Control")) {
                        if (s == null) {
                            s = value;
                            break Label_0162;
                        }
                    }
                    else {
                        n6 = n;
                        s2 = s;
                        b9 = b;
                        b10 = b2;
                        n7 = n2;
                        n8 = n3;
                        b11 = b3;
                        b12 = b4;
                        b13 = b5;
                        n9 = n4;
                        n10 = n5;
                        b14 = b6;
                        b15 = b7;
                        b16 = b8;
                        if (!name.equalsIgnoreCase("Pragma")) {
                            break Label_1158;
                        }
                    }
                    n = 0;
                }
                int n11 = 0;
                while (true) {
                    n6 = n;
                    s2 = s;
                    b9 = b;
                    b10 = b2;
                    n7 = n2;
                    n8 = n3;
                    b11 = b3;
                    b12 = b4;
                    b13 = b5;
                    n9 = n4;
                    n10 = n5;
                    b14 = b6;
                    b15 = b7;
                    b16 = b8;
                    if (n11 >= value.length()) {
                        break;
                    }
                    final int skipUntil = HttpHeaders.skipUntil(value, n11, "=,;");
                    final String trim = value.substring(n11, skipUntil).trim();
                    String s3;
                    int skipUntil3;
                    if (skipUntil != value.length() && value.charAt(skipUntil) != ',' && value.charAt(skipUntil) != ';') {
                        final int skipWhitespace = HttpHeaders.skipWhitespace(value, skipUntil + 1);
                        if (skipWhitespace < value.length() && value.charAt(skipWhitespace) == '\"') {
                            final int n12 = skipWhitespace + 1;
                            final int skipUntil2 = HttpHeaders.skipUntil(value, n12, "\"");
                            s3 = value.substring(n12, skipUntil2);
                            skipUntil3 = skipUntil2 + 1;
                        }
                        else {
                            skipUntil3 = HttpHeaders.skipUntil(value, skipWhitespace, ",;");
                            s3 = value.substring(skipWhitespace, skipUntil3).trim();
                        }
                    }
                    else {
                        skipUntil3 = skipUntil + 1;
                        s3 = null;
                    }
                    boolean b17;
                    boolean b18;
                    int seconds;
                    int seconds2;
                    boolean b19;
                    boolean b20;
                    boolean b21;
                    int seconds3;
                    int seconds4;
                    boolean b22;
                    boolean b23;
                    if ("no-cache".equalsIgnoreCase(trim)) {
                        b17 = true;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("no-store".equalsIgnoreCase(trim)) {
                        b18 = true;
                        b17 = b;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("max-age".equalsIgnoreCase(trim)) {
                        seconds = HttpHeaders.parseSeconds(s3, -1);
                        b17 = b;
                        b18 = b2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("s-maxage".equalsIgnoreCase(trim)) {
                        seconds2 = HttpHeaders.parseSeconds(s3, -1);
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("private".equalsIgnoreCase(trim)) {
                        b19 = true;
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("public".equalsIgnoreCase(trim)) {
                        b20 = true;
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("must-revalidate".equalsIgnoreCase(trim)) {
                        b21 = true;
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("max-stale".equalsIgnoreCase(trim)) {
                        seconds3 = HttpHeaders.parseSeconds(s3, Integer.MAX_VALUE);
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("min-fresh".equalsIgnoreCase(trim)) {
                        seconds4 = HttpHeaders.parseSeconds(s3, -1);
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        b22 = b6;
                        b23 = b7;
                    }
                    else if ("only-if-cached".equalsIgnoreCase(trim)) {
                        b22 = true;
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b23 = b7;
                    }
                    else if ("no-transform".equalsIgnoreCase(trim)) {
                        b23 = true;
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                    }
                    else {
                        b17 = b;
                        b18 = b2;
                        seconds = n2;
                        seconds2 = n3;
                        b19 = b3;
                        b20 = b4;
                        b21 = b5;
                        seconds3 = n4;
                        seconds4 = n5;
                        b22 = b6;
                        b23 = b7;
                        if ("immutable".equalsIgnoreCase(trim)) {
                            b8 = true;
                            b23 = b7;
                            b22 = b6;
                            seconds4 = n5;
                            seconds3 = n4;
                            b21 = b5;
                            b20 = b4;
                            b19 = b3;
                            seconds2 = n3;
                            seconds = n2;
                            b18 = b2;
                            b17 = b;
                        }
                    }
                    n11 = skipUntil3;
                    b = b17;
                    b2 = b18;
                    n2 = seconds;
                    n3 = seconds2;
                    b3 = b19;
                    b4 = b20;
                    b5 = b21;
                    n4 = seconds3;
                    n5 = seconds4;
                    b6 = b22;
                    b7 = b23;
                }
            }
            ++i;
            n = n6;
            s = s2;
            b = b9;
            b2 = b10;
            n2 = n7;
            n3 = n8;
            b3 = b11;
            b4 = b12;
            b5 = b13;
            n4 = n9;
            n5 = n10;
            b6 = b14;
            b7 = b15;
            b8 = b16;
        }
        if (n == 0) {
            s = null;
        }
        return new CacheControl(b, b2, n2, n3, b3, b4, b5, n4, n5, b6, b7, b8, s);
    }
    
    public boolean immutable() {
        return this.immutable;
    }
    
    public boolean isPrivate() {
        return this.isPrivate;
    }
    
    public boolean isPublic() {
        return this.isPublic;
    }
    
    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }
    
    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }
    
    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }
    
    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }
    
    public boolean noCache() {
        return this.noCache;
    }
    
    public boolean noStore() {
        return this.noStore;
    }
    
    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }
    
    @Override
    public String toString() {
        final String headerValue = this.headerValue;
        if (headerValue != null) {
            return headerValue;
        }
        return this.headerValue = this.headerValue();
    }
    
    public static final class Builder
    {
        boolean immutable;
        int maxAgeSeconds;
        int maxStaleSeconds;
        int minFreshSeconds;
        boolean noCache;
        boolean noStore;
        boolean noTransform;
        boolean onlyIfCached;
        
        public Builder() {
            this.maxAgeSeconds = -1;
            this.maxStaleSeconds = -1;
            this.minFreshSeconds = -1;
        }
        
        public CacheControl build() {
            return new CacheControl(this);
        }
        
        public Builder maxStale(int maxStaleSeconds, final TimeUnit timeUnit) {
            if (maxStaleSeconds >= 0) {
                final long seconds = timeUnit.toSeconds(maxStaleSeconds);
                if (seconds > 2147483647L) {
                    maxStaleSeconds = Integer.MAX_VALUE;
                }
                else {
                    maxStaleSeconds = (int)seconds;
                }
                this.maxStaleSeconds = maxStaleSeconds;
                return this;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("maxStale < 0: ");
            sb.append(maxStaleSeconds);
            throw new IllegalArgumentException(sb.toString());
        }
        
        public Builder noCache() {
            this.noCache = true;
            return this;
        }
        
        public Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }
    }
}
