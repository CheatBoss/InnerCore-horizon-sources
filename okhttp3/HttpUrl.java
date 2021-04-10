package okhttp3;

import javax.annotation.*;
import java.nio.charset.*;
import okio.*;
import okhttp3.internal.*;
import java.util.*;
import java.net.*;

public final class HttpUrl
{
    private static final char[] HEX_DIGITS;
    @Nullable
    private final String fragment;
    final String host;
    private final String password;
    private final List<String> pathSegments;
    final int port;
    @Nullable
    private final List<String> queryNamesAndValues;
    final String scheme;
    private final String url;
    private final String username;
    
    static {
        HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
    
    HttpUrl(final Builder builder) {
        this.scheme = builder.scheme;
        this.username = percentDecode(builder.encodedUsername, false);
        this.password = percentDecode(builder.encodedPassword, false);
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.pathSegments = this.percentDecode(builder.encodedPathSegments, false);
        final List<String> encodedQueryNamesAndValues = builder.encodedQueryNamesAndValues;
        final String s = null;
        List<String> percentDecode;
        if (encodedQueryNamesAndValues != null) {
            percentDecode = this.percentDecode(builder.encodedQueryNamesAndValues, true);
        }
        else {
            percentDecode = null;
        }
        this.queryNamesAndValues = percentDecode;
        String percentDecode2 = s;
        if (builder.encodedFragment != null) {
            percentDecode2 = percentDecode(builder.encodedFragment, false);
        }
        this.fragment = percentDecode2;
        this.url = builder.toString();
    }
    
    static String canonicalize(final String s, final int n, final int n2, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final Charset charset) {
        int codePoint;
        for (int i = n; i < n2; i += Character.charCount(codePoint)) {
            codePoint = s.codePointAt(i);
            if (codePoint < 32 || codePoint == 127 || (codePoint >= 128 && b4) || s2.indexOf(codePoint) != -1 || (codePoint == 37 && (!b || (b2 && !percentEncoded(s, i, n2)))) || (codePoint == 43 && b3)) {
                final Buffer buffer = new Buffer();
                buffer.writeUtf8(s, n, i);
                canonicalize(buffer, s, i, n2, s2, b, b2, b3, b4, charset);
                return buffer.readUtf8();
            }
        }
        return s.substring(n, n2);
    }
    
    static String canonicalize(final String s, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        return canonicalize(s, 0, s.length(), s2, b, b2, b3, b4, null);
    }
    
    static void canonicalize(final Buffer buffer, final String s, int i, final int n, final String s2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final Charset charset) {
        Buffer buffer2 = null;
        while (i < n) {
            final int codePoint = s.codePointAt(i);
            Buffer buffer3 = null;
            Label_0318: {
                if (b) {
                    buffer3 = buffer2;
                    if (codePoint == 9) {
                        break Label_0318;
                    }
                    buffer3 = buffer2;
                    if (codePoint == 10) {
                        break Label_0318;
                    }
                    buffer3 = buffer2;
                    if (codePoint == 12) {
                        break Label_0318;
                    }
                    if (codePoint == 13) {
                        buffer3 = buffer2;
                        break Label_0318;
                    }
                }
                if (codePoint == 43 && b3) {
                    String s3;
                    if (b) {
                        s3 = "+";
                    }
                    else {
                        s3 = "%2B";
                    }
                    buffer.writeUtf8(s3);
                    buffer3 = buffer2;
                }
                else {
                    Label_0190: {
                        if (codePoint >= 32 && codePoint != 127 && (codePoint < 128 || !b4) && s2.indexOf(codePoint) == -1) {
                            if (codePoint == 37) {
                                if (!b) {
                                    break Label_0190;
                                }
                                if (b2 && !percentEncoded(s, i, n)) {
                                    break Label_0190;
                                }
                            }
                            buffer.writeUtf8CodePoint(codePoint);
                            buffer3 = buffer2;
                            break Label_0318;
                        }
                    }
                    Buffer buffer4;
                    if ((buffer4 = buffer2) == null) {
                        buffer4 = new Buffer();
                    }
                    if (charset != null && !charset.equals(Util.UTF_8)) {
                        buffer4.writeString(s, i, Character.charCount(codePoint) + i, charset);
                    }
                    else {
                        buffer4.writeUtf8CodePoint(codePoint);
                    }
                    while (true) {
                        buffer3 = buffer4;
                        if (buffer4.exhausted()) {
                            break;
                        }
                        final int n2 = buffer4.readByte() & 0xFF;
                        buffer.writeByte(37);
                        buffer.writeByte((int)HttpUrl.HEX_DIGITS[n2 >> 4 & 0xF]);
                        buffer.writeByte((int)HttpUrl.HEX_DIGITS[n2 & 0xF]);
                    }
                }
            }
            i += Character.charCount(codePoint);
            buffer2 = buffer3;
        }
    }
    
    public static int defaultPort(final String s) {
        if (s.equals("http")) {
            return 80;
        }
        if (s.equals("https")) {
            return 443;
        }
        return -1;
    }
    
    @Nullable
    public static HttpUrl get(final URL url) {
        return parse(url.toString());
    }
    
    static HttpUrl getChecked(final String s) throws MalformedURLException, UnknownHostException {
        final Builder builder = new Builder();
        final ParseResult parse = builder.parse(null, s);
        final int n = HttpUrl$1.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult[parse.ordinal()];
        if (n == 1) {
            return builder.build();
        }
        if (n != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid URL: ");
            sb.append(parse);
            sb.append(" for ");
            sb.append(s);
            throw new MalformedURLException(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Invalid host: ");
        sb2.append(s);
        throw new UnknownHostException(sb2.toString());
    }
    
    static void namesAndValuesToQueryString(final StringBuilder sb, final List<String> list) {
        for (int size = list.size(), i = 0; i < size; i += 2) {
            final String s = list.get(i);
            final String s2 = list.get(i + 1);
            if (i > 0) {
                sb.append('&');
            }
            sb.append(s);
            if (s2 != null) {
                sb.append('=');
                sb.append(s2);
            }
        }
    }
    
    @Nullable
    public static HttpUrl parse(final String s) {
        final Builder builder = new Builder();
        HttpUrl build = null;
        if (builder.parse(null, s) == ParseResult.SUCCESS) {
            build = builder.build();
        }
        return build;
    }
    
    static void pathSegmentsToString(final StringBuilder sb, final List<String> list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            sb.append('/');
            sb.append(list.get(i));
        }
    }
    
    static String percentDecode(final String s, final int n, final int n2, final boolean b) {
        for (int i = n; i < n2; ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '%' || (char1 == '+' && b)) {
                final Buffer buffer = new Buffer();
                buffer.writeUtf8(s, n, i);
                percentDecode(buffer, s, i, n2, b);
                return buffer.readUtf8();
            }
        }
        return s.substring(n, n2);
    }
    
    static String percentDecode(final String s, final boolean b) {
        return percentDecode(s, 0, s.length(), b);
    }
    
    private List<String> percentDecode(final List<String> list, final boolean b) {
        final int size = list.size();
        final ArrayList list2 = new ArrayList<String>(size);
        for (int i = 0; i < size; ++i) {
            final String s = list.get(i);
            String percentDecode;
            if (s != null) {
                percentDecode = percentDecode(s, b);
            }
            else {
                percentDecode = null;
            }
            list2.add(percentDecode);
        }
        return Collections.unmodifiableList((List<? extends String>)list2);
    }
    
    static void percentDecode(final Buffer buffer, final String s, int i, final int n, final boolean b) {
        while (i < n) {
            final int codePoint = s.codePointAt(i);
            Label_0112: {
                Label_0105: {
                    if (codePoint == 37) {
                        final int n2 = i + 2;
                        if (n2 < n) {
                            final int decodeHexDigit = Util.decodeHexDigit(s.charAt(i + 1));
                            final int decodeHexDigit2 = Util.decodeHexDigit(s.charAt(n2));
                            if (decodeHexDigit != -1 && decodeHexDigit2 != -1) {
                                buffer.writeByte((decodeHexDigit << 4) + decodeHexDigit2);
                                i = n2;
                                break Label_0112;
                            }
                            break Label_0105;
                        }
                    }
                    if (codePoint == 43 && b) {
                        buffer.writeByte(32);
                        break Label_0112;
                    }
                }
                buffer.writeUtf8CodePoint(codePoint);
            }
            i += Character.charCount(codePoint);
        }
    }
    
    static boolean percentEncoded(final String s, final int n, final int n2) {
        final int n3 = n + 2;
        return n3 < n2 && s.charAt(n) == '%' && Util.decodeHexDigit(s.charAt(n + 1)) != -1 && Util.decodeHexDigit(s.charAt(n3)) != -1;
    }
    
    static List<String> queryStringToNamesAndValues(final String s) {
        final ArrayList<String> list = new ArrayList<String>();
        int n;
        for (int i = 0; i <= s.length(); i = n + 1) {
            if ((n = s.indexOf(38, i)) == -1) {
                n = s.length();
            }
            final int index = s.indexOf(61, i);
            String substring;
            if (index != -1 && index <= n) {
                list.add(s.substring(i, index));
                substring = s.substring(index + 1, n);
            }
            else {
                list.add(s.substring(i, n));
                substring = null;
            }
            list.add(substring);
        }
        return list;
    }
    
    @Nullable
    public String encodedFragment() {
        if (this.fragment == null) {
            return null;
        }
        return this.url.substring(this.url.indexOf(35) + 1);
    }
    
    public String encodedPassword() {
        if (this.password.isEmpty()) {
            return "";
        }
        return this.url.substring(this.url.indexOf(58, this.scheme.length() + 3) + 1, this.url.indexOf(64));
    }
    
    public String encodedPath() {
        final int index = this.url.indexOf(47, this.scheme.length() + 3);
        final String url = this.url;
        return this.url.substring(index, Util.delimiterOffset(url, index, url.length(), "?#"));
    }
    
    public List<String> encodedPathSegments() {
        int i = this.url.indexOf(47, this.scheme.length() + 3);
        final String url = this.url;
        final int delimiterOffset = Util.delimiterOffset(url, i, url.length(), "?#");
        final ArrayList<String> list = new ArrayList<String>();
        while (i < delimiterOffset) {
            final int n = i + 1;
            i = Util.delimiterOffset(this.url, n, delimiterOffset, '/');
            list.add(this.url.substring(n, i));
        }
        return list;
    }
    
    @Nullable
    public String encodedQuery() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        final int n = this.url.indexOf(63) + 1;
        final String url = this.url;
        return this.url.substring(n, Util.delimiterOffset(url, n, url.length(), '#'));
    }
    
    public String encodedUsername() {
        if (this.username.isEmpty()) {
            return "";
        }
        final int n = this.scheme.length() + 3;
        final String url = this.url;
        return this.url.substring(n, Util.delimiterOffset(url, n, url.length(), ":@"));
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof HttpUrl && ((HttpUrl)o).url.equals(this.url);
    }
    
    @Override
    public int hashCode() {
        return this.url.hashCode();
    }
    
    public String host() {
        return this.host;
    }
    
    public boolean isHttps() {
        return this.scheme.equals("https");
    }
    
    public Builder newBuilder() {
        final Builder builder = new Builder();
        builder.scheme = this.scheme;
        builder.encodedUsername = this.encodedUsername();
        builder.encodedPassword = this.encodedPassword();
        builder.host = this.host;
        int port;
        if (this.port != defaultPort(this.scheme)) {
            port = this.port;
        }
        else {
            port = -1;
        }
        builder.port = port;
        builder.encodedPathSegments.clear();
        builder.encodedPathSegments.addAll(this.encodedPathSegments());
        builder.encodedQuery(this.encodedQuery());
        builder.encodedFragment = this.encodedFragment();
        return builder;
    }
    
    @Nullable
    public Builder newBuilder(final String s) {
        final Builder builder = new Builder();
        if (builder.parse(this, s) == ParseResult.SUCCESS) {
            return builder;
        }
        return null;
    }
    
    public int port() {
        return this.port;
    }
    
    @Nullable
    public String query() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        namesAndValuesToQueryString(sb, this.queryNamesAndValues);
        return sb.toString();
    }
    
    public String redact() {
        return this.newBuilder("/...").username("").password("").build().toString();
    }
    
    @Nullable
    public HttpUrl resolve(final String s) {
        final Builder builder = this.newBuilder(s);
        if (builder != null) {
            return builder.build();
        }
        return null;
    }
    
    public String scheme() {
        return this.scheme;
    }
    
    @Override
    public String toString() {
        return this.url;
    }
    
    public URI uri() {
        final String string = this.newBuilder().reencodeForUri().toString();
        try {
            return new URI(string);
        }
        catch (URISyntaxException ex) {
            try {
                return URI.create(string.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", ""));
            }
            catch (Exception ex2) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    public static final class Builder
    {
        @Nullable
        String encodedFragment;
        String encodedPassword;
        final List<String> encodedPathSegments;
        @Nullable
        List<String> encodedQueryNamesAndValues;
        String encodedUsername;
        @Nullable
        String host;
        int port;
        @Nullable
        String scheme;
        
        public Builder() {
            this.encodedUsername = "";
            this.encodedPassword = "";
            this.port = -1;
            (this.encodedPathSegments = new ArrayList<String>()).add("");
        }
        
        private static String canonicalizeHost(final String s, final int n, final int n2) {
            return Util.canonicalizeHost(HttpUrl.percentDecode(s, n, n2, false));
        }
        
        private boolean isDot(final String s) {
            return s.equals(".") || s.equalsIgnoreCase("%2e");
        }
        
        private boolean isDotDot(final String s) {
            return s.equals("..") || s.equalsIgnoreCase("%2e.") || s.equalsIgnoreCase(".%2e") || s.equalsIgnoreCase("%2e%2e");
        }
        
        private static int parsePort(final String s, int int1, final int n) {
            try {
                int1 = Integer.parseInt(HttpUrl.canonicalize(s, int1, n, "", false, false, false, true, null));
                if (int1 > 0 && int1 <= 65535) {
                    return int1;
                }
                return -1;
            }
            catch (NumberFormatException ex) {
                return -1;
            }
        }
        
        private void pop() {
            final List<String> encodedPathSegments = this.encodedPathSegments;
            if (encodedPathSegments.remove(encodedPathSegments.size() - 1).isEmpty() && !this.encodedPathSegments.isEmpty()) {
                final List<String> encodedPathSegments2 = this.encodedPathSegments;
                encodedPathSegments2.set(encodedPathSegments2.size() - 1, "");
                return;
            }
            this.encodedPathSegments.add("");
        }
        
        private static int portColonOffset(final String s, int i, final int n) {
            while (i < n) {
                final char char1 = s.charAt(i);
                if (char1 == ':') {
                    return i;
                }
                int n2 = i;
                int n3 = 0;
                Label_0058: {
                    if (char1 != '[') {
                        n3 = i;
                    }
                    else {
                        do {
                            i = n2 + 1;
                            if ((n3 = i) >= n) {
                                break Label_0058;
                            }
                            n2 = i;
                        } while (s.charAt(i) != ']');
                        n3 = i;
                    }
                }
                i = n3 + 1;
            }
            return n;
        }
        
        private void push(String canonicalize, final int n, final int n2, final boolean b, final boolean b2) {
            canonicalize = HttpUrl.canonicalize(canonicalize, n, n2, " \"<>^`{}|/\\?#", b2, false, false, true, null);
            if (this.isDot(canonicalize)) {
                return;
            }
            if (this.isDotDot(canonicalize)) {
                this.pop();
                return;
            }
            final List<String> encodedPathSegments = this.encodedPathSegments;
            if (encodedPathSegments.get(encodedPathSegments.size() - 1).isEmpty()) {
                final List<String> encodedPathSegments2 = this.encodedPathSegments;
                encodedPathSegments2.set(encodedPathSegments2.size() - 1, canonicalize);
            }
            else {
                this.encodedPathSegments.add(canonicalize);
            }
            if (b) {
                this.encodedPathSegments.add("");
            }
        }
        
        private void resolvePath(String s, int i, final int n) {
            if (i == n) {
                return;
            }
            final char char1 = s.charAt(i);
            while (true) {
                Builder builder = null;
                Label_0091: {
                    if (char1 == '/' || char1 == '\\') {
                        this.encodedPathSegments.clear();
                        this.encodedPathSegments.add("");
                        builder = this;
                        break Label_0091;
                    }
                    final List<String> encodedPathSegments = this.encodedPathSegments;
                    encodedPathSegments.set(encodedPathSegments.size() - 1, "");
                    final String s2 = s;
                    final Builder builder2 = this;
                    while (i < n) {
                        final int delimiterOffset = Util.delimiterOffset(s2, i, n, "/\\");
                        final boolean b = delimiterOffset < n;
                        builder2.push(s2, i, delimiterOffset, b, true);
                        if (b) {
                            final String s3 = s2;
                            i = delimiterOffset;
                            builder = builder2;
                            s = s3;
                            break Label_0091;
                        }
                        i = delimiterOffset;
                    }
                    return;
                }
                ++i;
                final String s4 = s;
                final Builder builder2 = builder;
                final String s2 = s4;
                continue;
            }
        }
        
        private static int schemeDelimiterOffset(final String s, int n, final int n2) {
            if (n2 - n < 2) {
                return -1;
            }
            final char char1 = s.charAt(n);
            while (true) {
                Label_0032: {
                    if (char1 < 'a') {
                        break Label_0032;
                    }
                    int n3 = n;
                    if (char1 > 'z') {
                        break Label_0032;
                    }
                    while (true) {
                        n = n3 + 1;
                        if (n >= n2) {
                            return -1;
                        }
                        final char char2 = s.charAt(n);
                        if (char2 >= 'a') {
                            n3 = n;
                            if (char2 <= 'z') {
                                continue;
                            }
                        }
                        if (char2 >= 'A') {
                            n3 = n;
                            if (char2 <= 'Z') {
                                continue;
                            }
                        }
                        if (char2 >= '0') {
                            n3 = n;
                            if (char2 <= '9') {
                                continue;
                            }
                        }
                        n3 = n;
                        if (char2 == '+') {
                            continue;
                        }
                        n3 = n;
                        if (char2 == '-') {
                            continue;
                        }
                        if (char2 == '.') {
                            n3 = n;
                        }
                        else {
                            if (char2 == ':') {
                                return n;
                            }
                            return -1;
                        }
                    }
                }
                if (char1 >= 'A') {
                    final int n3 = n;
                    if (char1 > 'Z') {
                        return -1;
                    }
                    continue;
                }
                break;
            }
            return -1;
        }
        
        private static int slashCount(final String s, int i, final int n) {
            int n2 = 0;
            while (i < n) {
                final char char1 = s.charAt(i);
                if (char1 != '\\' && char1 != '/') {
                    break;
                }
                ++n2;
                ++i;
            }
            return n2;
        }
        
        public HttpUrl build() {
            if (this.scheme == null) {
                throw new IllegalStateException("scheme == null");
            }
            if (this.host != null) {
                return new HttpUrl(this);
            }
            throw new IllegalStateException("host == null");
        }
        
        int effectivePort() {
            final int port = this.port;
            if (port != -1) {
                return port;
            }
            return HttpUrl.defaultPort(this.scheme);
        }
        
        public Builder encodedQuery(@Nullable final String s) {
            List<String> queryStringToNamesAndValues;
            if (s != null) {
                queryStringToNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(s, " \"'<>#", true, false, true, true));
            }
            else {
                queryStringToNamesAndValues = null;
            }
            this.encodedQueryNamesAndValues = queryStringToNamesAndValues;
            return this;
        }
        
        public Builder host(final String s) {
            if (s == null) {
                throw new NullPointerException("host == null");
            }
            final String canonicalizeHost = canonicalizeHost(s, 0, s.length());
            if (canonicalizeHost != null) {
                this.host = canonicalizeHost;
                return this;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected host: ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        
        ParseResult parse(@Nullable final HttpUrl httpUrl, final String s) {
            int skipLeadingAsciiWhitespace = Util.skipLeadingAsciiWhitespace(s, 0, s.length());
            final int skipTrailingAsciiWhitespace = Util.skipTrailingAsciiWhitespace(s, skipLeadingAsciiWhitespace, s.length());
            if (schemeDelimiterOffset(s, skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace) != -1) {
                if (s.regionMatches(true, skipLeadingAsciiWhitespace, "https:", 0, 6)) {
                    this.scheme = "https";
                    skipLeadingAsciiWhitespace += 6;
                }
                else {
                    if (!s.regionMatches(true, skipLeadingAsciiWhitespace, "http:", 0, 5)) {
                        return ParseResult.UNSUPPORTED_SCHEME;
                    }
                    this.scheme = "http";
                    skipLeadingAsciiWhitespace += 5;
                }
            }
            else {
                if (httpUrl == null) {
                    return ParseResult.MISSING_SCHEME;
                }
                this.scheme = httpUrl.scheme;
            }
            final int slashCount = slashCount(s, skipLeadingAsciiWhitespace, skipTrailingAsciiWhitespace);
            int delimiterOffset;
            if (slashCount < 2 && httpUrl != null && httpUrl.scheme.equals(this.scheme)) {
                this.encodedUsername = httpUrl.encodedUsername();
                this.encodedPassword = httpUrl.encodedPassword();
                this.host = httpUrl.host;
                this.port = httpUrl.port;
                this.encodedPathSegments.clear();
                this.encodedPathSegments.addAll(httpUrl.encodedPathSegments());
                if (skipLeadingAsciiWhitespace == skipTrailingAsciiWhitespace || s.charAt(skipLeadingAsciiWhitespace) == '#') {
                    this.encodedQuery(httpUrl.encodedQuery());
                }
                delimiterOffset = skipLeadingAsciiWhitespace;
            }
            else {
                int n = skipLeadingAsciiWhitespace + slashCount;
                int n2 = 0;
                int n3 = 0;
                while (true) {
                    delimiterOffset = Util.delimiterOffset(s, n, skipTrailingAsciiWhitespace, "@/\\?#");
                    int char1;
                    if (delimiterOffset != skipTrailingAsciiWhitespace) {
                        char1 = s.charAt(delimiterOffset);
                    }
                    else {
                        char1 = -1;
                    }
                    if (char1 == -1 || char1 == 35 || char1 == 47 || char1 == 92 || char1 == 63) {
                        break;
                    }
                    if (char1 != 64) {
                        continue;
                    }
                    if (n2 == 0) {
                        final int delimiterOffset2 = Util.delimiterOffset(s, n, delimiterOffset, ':');
                        final int n4 = delimiterOffset;
                        String encodedUsername = HttpUrl.canonicalize(s, n, delimiterOffset2, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null);
                        if (n3 != 0) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append(this.encodedUsername);
                            sb.append("%40");
                            sb.append(encodedUsername);
                            encodedUsername = sb.toString();
                        }
                        this.encodedUsername = encodedUsername;
                        if (delimiterOffset2 != n4) {
                            this.encodedPassword = HttpUrl.canonicalize(s, delimiterOffset2 + 1, n4, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null);
                            n2 = 1;
                        }
                        n3 = 1;
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(this.encodedPassword);
                        sb2.append("%40");
                        sb2.append(HttpUrl.canonicalize(s, n, delimiterOffset, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null));
                        this.encodedPassword = sb2.toString();
                    }
                    n = delimiterOffset + 1;
                }
                final int portColonOffset = portColonOffset(s, n, delimiterOffset);
                final int n5 = portColonOffset + 1;
                if (n5 < delimiterOffset) {
                    this.host = canonicalizeHost(s, n, portColonOffset);
                    if ((this.port = parsePort(s, n5, delimiterOffset)) == -1) {
                        return ParseResult.INVALID_PORT;
                    }
                }
                else {
                    this.host = canonicalizeHost(s, n, portColonOffset);
                    this.port = HttpUrl.defaultPort(this.scheme);
                }
                if (this.host == null) {
                    return ParseResult.INVALID_HOST;
                }
            }
            int delimiterOffset3 = Util.delimiterOffset(s, delimiterOffset, skipTrailingAsciiWhitespace, "?#");
            this.resolvePath(s, delimiterOffset, delimiterOffset3);
            if (delimiterOffset3 < skipTrailingAsciiWhitespace && s.charAt(delimiterOffset3) == '?') {
                final int delimiterOffset4 = Util.delimiterOffset(s, delimiterOffset3, skipTrailingAsciiWhitespace, '#');
                this.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(s, delimiterOffset3 + 1, delimiterOffset4, " \"'<>#", true, false, true, true, null));
                delimiterOffset3 = delimiterOffset4;
            }
            if (delimiterOffset3 < skipTrailingAsciiWhitespace && s.charAt(delimiterOffset3) == '#') {
                this.encodedFragment = HttpUrl.canonicalize(s, delimiterOffset3 + 1, skipTrailingAsciiWhitespace, "", true, false, false, false, null);
            }
            return ParseResult.SUCCESS;
        }
        
        public Builder password(final String s) {
            if (s != null) {
                this.encodedPassword = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
                return this;
            }
            throw new NullPointerException("password == null");
        }
        
        public Builder port(final int port) {
            if (port > 0 && port <= 65535) {
                this.port = port;
                return this;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected port: ");
            sb.append(port);
            throw new IllegalArgumentException(sb.toString());
        }
        
        Builder reencodeForUri() {
            final int size = this.encodedPathSegments.size();
            final int n = 0;
            for (int i = 0; i < size; ++i) {
                this.encodedPathSegments.set(i, HttpUrl.canonicalize(this.encodedPathSegments.get(i), "[]", true, true, false, true));
            }
            final List<String> encodedQueryNamesAndValues = this.encodedQueryNamesAndValues;
            if (encodedQueryNamesAndValues != null) {
                for (int size2 = encodedQueryNamesAndValues.size(), j = n; j < size2; ++j) {
                    final String s = this.encodedQueryNamesAndValues.get(j);
                    if (s != null) {
                        this.encodedQueryNamesAndValues.set(j, HttpUrl.canonicalize(s, "\\^`{|}", true, true, true, true));
                    }
                }
            }
            final String encodedFragment = this.encodedFragment;
            if (encodedFragment != null) {
                this.encodedFragment = HttpUrl.canonicalize(encodedFragment, " \"#<>\\^`{|}", true, true, false, false);
            }
            return this;
        }
        
        public Builder scheme(String scheme) {
            if (scheme != null) {
                if (scheme.equalsIgnoreCase("http")) {
                    scheme = "http";
                }
                else {
                    if (!scheme.equalsIgnoreCase("https")) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unexpected scheme: ");
                        sb.append(scheme);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    scheme = "https";
                }
                this.scheme = scheme;
                return this;
            }
            throw new NullPointerException("scheme == null");
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.scheme);
            sb.append("://");
            if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
                sb.append(this.encodedUsername);
                if (!this.encodedPassword.isEmpty()) {
                    sb.append(':');
                    sb.append(this.encodedPassword);
                }
                sb.append('@');
            }
            if (this.host.indexOf(58) != -1) {
                sb.append('[');
                sb.append(this.host);
                sb.append(']');
            }
            else {
                sb.append(this.host);
            }
            final int effectivePort = this.effectivePort();
            if (effectivePort != HttpUrl.defaultPort(this.scheme)) {
                sb.append(':');
                sb.append(effectivePort);
            }
            HttpUrl.pathSegmentsToString(sb, this.encodedPathSegments);
            if (this.encodedQueryNamesAndValues != null) {
                sb.append('?');
                HttpUrl.namesAndValuesToQueryString(sb, this.encodedQueryNamesAndValues);
            }
            if (this.encodedFragment != null) {
                sb.append('#');
                sb.append(this.encodedFragment);
            }
            return sb.toString();
        }
        
        public Builder username(final String s) {
            if (s != null) {
                this.encodedUsername = HttpUrl.canonicalize(s, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
                return this;
            }
            throw new NullPointerException("username == null");
        }
    }
    
    enum ParseResult
    {
        INVALID_HOST, 
        INVALID_PORT, 
        MISSING_SCHEME, 
        SUCCESS, 
        UNSUPPORTED_SCHEME;
    }
}
