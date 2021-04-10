package okhttp3;

import okhttp3.internal.*;
import okhttp3.internal.publicsuffix.*;
import javax.annotation.*;
import java.util.regex.*;
import java.util.*;
import okhttp3.internal.http.*;

public final class Cookie
{
    private static final Pattern DAY_OF_MONTH_PATTERN;
    private static final Pattern MONTH_PATTERN;
    private static final Pattern TIME_PATTERN;
    private static final Pattern YEAR_PATTERN;
    private final String domain;
    private final long expiresAt;
    private final boolean hostOnly;
    private final boolean httpOnly;
    private final String name;
    private final String path;
    private final boolean persistent;
    private final boolean secure;
    private final String value;
    
    static {
        YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
        MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
        DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
        TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
    }
    
    private Cookie(final String name, final String value, final long expiresAt, final String domain, final String path, final boolean secure, final boolean httpOnly, final boolean hostOnly, final boolean persistent) {
        this.name = name;
        this.value = value;
        this.expiresAt = expiresAt;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.httpOnly = httpOnly;
        this.hostOnly = hostOnly;
        this.persistent = persistent;
    }
    
    private static int dateCharacterOffset(final String s, int i, final int n, final boolean b) {
        while (i < n) {
            final char char1 = s.charAt(i);
            if (((char1 < ' ' && char1 != '\t') || char1 >= '\u007f' || (char1 >= '0' && char1 <= '9') || (char1 >= 'a' && char1 <= 'z') || (char1 >= 'A' && char1 <= 'Z') || char1 == ':') == (b ^ true)) {
                return i;
            }
            ++i;
        }
        return n;
    }
    
    private static boolean domainMatch(final String s, final String s2) {
        return s.equals(s2) || (s.endsWith(s2) && s.charAt(s.length() - s2.length() - 1) == '.' && !Util.verifyAsIpAddress(s));
    }
    
    @Nullable
    static Cookie parse(long n, final HttpUrl httpUrl, String s) {
        final int length = s.length();
        final int delimiterOffset = Util.delimiterOffset(s, 0, length, ';');
        final int delimiterOffset2 = Util.delimiterOffset(s, 0, delimiterOffset, '=');
        if (delimiterOffset2 == delimiterOffset) {
            return null;
        }
        final String trimSubstring = Util.trimSubstring(s, 0, delimiterOffset2);
        if (trimSubstring.isEmpty() || Util.indexOfControlOrNonAscii(trimSubstring) != -1) {
            return null;
        }
        final String trimSubstring2 = Util.trimSubstring(s, delimiterOffset2 + 1, delimiterOffset);
        if (Util.indexOfControlOrNonAscii(trimSubstring2) != -1) {
            return null;
        }
        int i = delimiterOffset + 1;
        String s3;
        String s2 = s3 = null;
        long maxAge = -1L;
        long expires = 253402300799999L;
        boolean b = false;
        boolean b2 = true;
        boolean b3 = false;
        boolean b4 = false;
        while (i < length) {
            final int delimiterOffset3 = Util.delimiterOffset(s, i, length, ';');
            final int delimiterOffset4 = Util.delimiterOffset(s, i, delimiterOffset3, '=');
            final String trimSubstring3 = Util.trimSubstring(s, i, delimiterOffset4);
            String trimSubstring4;
            if (delimiterOffset4 < delimiterOffset3) {
                trimSubstring4 = Util.trimSubstring(s, delimiterOffset4 + 1, delimiterOffset3);
            }
            else {
                trimSubstring4 = "";
            }
            Label_0275: {
                while (true) {
                    if (!trimSubstring3.equalsIgnoreCase("expires")) {
                        break Block_26;
                    }
                    try {
                        expires = parseExpires(trimSubstring4, 0, trimSubstring4.length());
                        break Label_0275;
                        // iftrue(Label_0305:, !trimSubstring3.equalsIgnoreCase("max-age"))
                        continue;
                    }
                    catch (IllegalArgumentException ex) {}
                    break;
                }
                maxAge = parseMaxAge(trimSubstring4);
            }
            boolean b5 = true;
            String domain = s2;
            long n2 = maxAge;
            long n3 = expires;
            boolean b6 = b;
            String s4 = s3;
            boolean b7 = b2;
            Label_0498: {
                break Label_0498;
                Label_0305: {
                    if (trimSubstring3.equalsIgnoreCase("domain")) {
                        domain = parseDomain(trimSubstring4);
                        b7 = false;
                        n2 = maxAge;
                        n3 = expires;
                        b6 = b;
                        s4 = s3;
                        b5 = b3;
                    }
                    else if (trimSubstring3.equalsIgnoreCase("path")) {
                        s4 = trimSubstring4;
                        domain = s2;
                        n2 = maxAge;
                        n3 = expires;
                        b6 = b;
                        b7 = b2;
                        b5 = b3;
                    }
                    else if (trimSubstring3.equalsIgnoreCase("secure")) {
                        b6 = true;
                        domain = s2;
                        n2 = maxAge;
                        n3 = expires;
                        s4 = s3;
                        b7 = b2;
                        b5 = b3;
                    }
                    else {
                        domain = s2;
                        n2 = maxAge;
                        n3 = expires;
                        b6 = b;
                        s4 = s3;
                        b7 = b2;
                        b5 = b3;
                        if (trimSubstring3.equalsIgnoreCase("httponly")) {
                            b4 = true;
                            b5 = b3;
                            b7 = b2;
                            s4 = s3;
                            b6 = b;
                            n3 = expires;
                            n2 = maxAge;
                            domain = s2;
                        }
                    }
                }
            }
            i = delimiterOffset3 + 1;
            s2 = domain;
            maxAge = n2;
            expires = n3;
            b = b6;
            s3 = s4;
            b2 = b7;
            b3 = b5;
        }
        final long n4 = Long.MIN_VALUE;
        Label_0617: {
            if (maxAge == Long.MIN_VALUE) {
                n = n4;
            }
            else if (maxAge != -1L) {
                long n5;
                if (maxAge <= 9223372036854775L) {
                    n5 = maxAge * 1000L;
                }
                else {
                    n5 = Long.MAX_VALUE;
                }
                final long n6 = n + n5;
                if (n6 >= n) {
                    n = n6;
                    if (n6 <= 253402300799999L) {
                        break Label_0617;
                    }
                }
                n = 253402300799999L;
            }
            else {
                n = expires;
            }
        }
        final String host = httpUrl.host();
        if (s2 == null) {
            s = host;
        }
        else {
            if (!domainMatch(host, s2)) {
                return null;
            }
            s = s2;
        }
        if (host.length() != s.length() && PublicSuffixDatabase.get().getEffectiveTldPlusOne(s) == null) {
            return null;
        }
        final String s5 = "/";
        String substring;
        if (s3 != null && s3.startsWith("/")) {
            substring = s3;
        }
        else {
            final String encodedPath = httpUrl.encodedPath();
            final int lastIndex = encodedPath.lastIndexOf(47);
            substring = s5;
            if (lastIndex != 0) {
                substring = encodedPath.substring(0, lastIndex);
            }
        }
        return new Cookie(trimSubstring, trimSubstring2, n, s, substring, b, b4, b2, b3);
    }
    
    @Nullable
    public static Cookie parse(final HttpUrl httpUrl, final String s) {
        return parse(System.currentTimeMillis(), httpUrl, s);
    }
    
    public static List<Cookie> parseAll(final HttpUrl httpUrl, final Headers headers) {
        final List<String> values = headers.values("Set-Cookie");
        final int size = values.size();
        List<Cookie> list = null;
        for (int i = 0; i < size; ++i) {
            final Cookie parse = parse(httpUrl, values.get(i));
            if (parse != null) {
                List<Cookie> list2;
                if ((list2 = list) == null) {
                    list2 = new ArrayList<Cookie>();
                }
                list2.add(parse);
                list = list2;
            }
        }
        if (list != null) {
            return (List<Cookie>)Collections.unmodifiableList((List<?>)list);
        }
        return Collections.emptyList();
    }
    
    private static String parseDomain(String canonicalizeHost) {
        if (canonicalizeHost.endsWith(".")) {
            throw new IllegalArgumentException();
        }
        String substring = canonicalizeHost;
        if (canonicalizeHost.startsWith(".")) {
            substring = canonicalizeHost.substring(1);
        }
        canonicalizeHost = Util.canonicalizeHost(substring);
        if (canonicalizeHost != null) {
            return canonicalizeHost;
        }
        throw new IllegalArgumentException();
    }
    
    private static long parseExpires(final String s, int n, int n2) {
        int i = dateCharacterOffset(s, n, n2, false);
        final Matcher matcher = Cookie.TIME_PATTERN.matcher(s);
        int n3 = -1;
        n = -1;
        int n4 = -1;
        int n5 = -1;
        int n6 = -1;
        int n7 = -1;
        while (i < n2) {
            final int dateCharacterOffset = dateCharacterOffset(s, i + 1, n2, true);
            matcher.region(i, dateCharacterOffset);
            int int1;
            int int2;
            int int3;
            int int4;
            int int5;
            int n8;
            if (n3 == -1 && matcher.usePattern(Cookie.TIME_PATTERN).matches()) {
                int1 = Integer.parseInt(matcher.group(1));
                int2 = Integer.parseInt(matcher.group(2));
                int3 = Integer.parseInt(matcher.group(3));
                int4 = n;
                int5 = n4;
                n8 = n5;
            }
            else if (n4 == -1 && matcher.usePattern(Cookie.DAY_OF_MONTH_PATTERN).matches()) {
                int5 = Integer.parseInt(matcher.group(1));
                int1 = n3;
                int4 = n;
                n8 = n5;
                int2 = n6;
                int3 = n7;
            }
            else if (n5 == -1 && matcher.usePattern(Cookie.MONTH_PATTERN).matches()) {
                n8 = Cookie.MONTH_PATTERN.pattern().indexOf(matcher.group(1).toLowerCase(Locale.US)) / 4;
                int1 = n3;
                int4 = n;
                int5 = n4;
                int2 = n6;
                int3 = n7;
            }
            else {
                int1 = n3;
                int4 = n;
                int5 = n4;
                n8 = n5;
                int2 = n6;
                int3 = n7;
                if (n == -1) {
                    int1 = n3;
                    int4 = n;
                    int5 = n4;
                    n8 = n5;
                    int2 = n6;
                    int3 = n7;
                    if (matcher.usePattern(Cookie.YEAR_PATTERN).matches()) {
                        int4 = Integer.parseInt(matcher.group(1));
                        int3 = n7;
                        int2 = n6;
                        n8 = n5;
                        int5 = n4;
                        int1 = n3;
                    }
                }
            }
            final int dateCharacterOffset2 = dateCharacterOffset(s, dateCharacterOffset + 1, n2, false);
            n3 = int1;
            n = int4;
            n4 = int5;
            n5 = n8;
            n6 = int2;
            n7 = int3;
            i = dateCharacterOffset2;
        }
        n2 = n;
        if (n >= 70 && (n2 = n) <= 99) {
            n2 = n + 1900;
        }
        if ((n = n2) >= 0 && (n = n2) <= 69) {
            n = n2 + 2000;
        }
        if (n < 1601) {
            throw new IllegalArgumentException();
        }
        if (n5 == -1) {
            throw new IllegalArgumentException();
        }
        if (n4 < 1 || n4 > 31) {
            throw new IllegalArgumentException();
        }
        if (n3 < 0 || n3 > 23) {
            throw new IllegalArgumentException();
        }
        if (n6 < 0 || n6 > 59) {
            throw new IllegalArgumentException();
        }
        if (n7 >= 0 && n7 <= 59) {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar(Util.UTC);
            gregorianCalendar.setLenient(false);
            gregorianCalendar.set(1, n);
            gregorianCalendar.set(2, n5 - 1);
            gregorianCalendar.set(5, n4);
            gregorianCalendar.set(11, n3);
            gregorianCalendar.set(12, n6);
            gregorianCalendar.set(13, n7);
            gregorianCalendar.set(14, 0);
            return gregorianCalendar.getTimeInMillis();
        }
        throw new IllegalArgumentException();
    }
    
    private static long parseMaxAge(final String s) {
        try {
            final long long1 = Long.parseLong(s);
            if (long1 <= 0L) {
                return Long.MIN_VALUE;
            }
            return long1;
        }
        catch (NumberFormatException ex) {
            if (!s.matches("-?\\d+")) {
                throw ex;
            }
            if (s.startsWith("-")) {
                return Long.MIN_VALUE;
            }
            return Long.MAX_VALUE;
        }
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        final boolean b = o instanceof Cookie;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final Cookie cookie = (Cookie)o;
        boolean b3 = b2;
        if (cookie.name.equals(this.name)) {
            b3 = b2;
            if (cookie.value.equals(this.value)) {
                b3 = b2;
                if (cookie.domain.equals(this.domain)) {
                    b3 = b2;
                    if (cookie.path.equals(this.path)) {
                        b3 = b2;
                        if (cookie.expiresAt == this.expiresAt) {
                            b3 = b2;
                            if (cookie.secure == this.secure) {
                                b3 = b2;
                                if (cookie.httpOnly == this.httpOnly) {
                                    b3 = b2;
                                    if (cookie.persistent == this.persistent) {
                                        b3 = b2;
                                        if (cookie.hostOnly == this.hostOnly) {
                                            b3 = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.name.hashCode();
        final int hashCode2 = this.value.hashCode();
        final int hashCode3 = this.domain.hashCode();
        final int hashCode4 = this.path.hashCode();
        final long expiresAt = this.expiresAt;
        return ((((((((hashCode + 527) * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + (int)(expiresAt ^ expiresAt >>> 32)) * 31 + ((this.secure ^ true) ? 1 : 0)) * 31 + ((this.httpOnly ^ true) ? 1 : 0)) * 31 + ((this.persistent ^ true) ? 1 : 0)) * 31 + ((this.hostOnly ^ true) ? 1 : 0);
    }
    
    public String name() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.toString(false);
    }
    
    String toString(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append('=');
        sb.append(this.value);
        if (this.persistent) {
            String format;
            if (this.expiresAt == Long.MIN_VALUE) {
                format = "; max-age=0";
            }
            else {
                sb.append("; expires=");
                format = HttpDate.format(new Date(this.expiresAt));
            }
            sb.append(format);
        }
        if (!this.hostOnly) {
            sb.append("; domain=");
            if (b) {
                sb.append(".");
            }
            sb.append(this.domain);
        }
        sb.append("; path=");
        sb.append(this.path);
        if (this.secure) {
            sb.append("; secure");
        }
        if (this.httpOnly) {
            sb.append("; httponly");
        }
        return sb.toString();
    }
    
    public String value() {
        return this.value;
    }
}
