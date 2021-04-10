package okhttp3.internal.http;

import okhttp3.internal.*;
import java.util.*;
import java.util.regex.*;
import okhttp3.*;

public final class HttpHeaders
{
    private static final Pattern PARAMETER;
    
    static {
        PARAMETER = Pattern.compile(" +([^ \"=]*)=(:?\"([^\"]*)\"|([^ \"=]*)) *(:?,|$)");
    }
    
    public static long contentLength(final Headers headers) {
        return stringToLong(headers.get("Content-Length"));
    }
    
    public static long contentLength(final Response response) {
        return contentLength(response.headers());
    }
    
    public static boolean hasBody(final Response response) {
        if (response.request().method().equals("HEAD")) {
            return false;
        }
        final int code = response.code();
        return ((code < 100 || code >= 200) && code != 204 && code != 304) || contentLength(response) != -1L || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"));
    }
    
    public static List<Challenge> parseChallenges(final Headers headers, String group) {
        final ArrayList<Challenge> list = new ArrayList<Challenge>();
        for (final String s : headers.values(group)) {
            int n = s.indexOf(32);
            if (n == -1) {
                continue;
            }
            final String substring = s.substring(0, n);
            final Matcher matcher = HttpHeaders.PARAMETER.matcher(s);
            String s2 = group = null;
            String s3;
            String s4;
            while (true) {
                s3 = s2;
                s4 = group;
                if (!matcher.find(n)) {
                    break;
                }
                String group2;
                if (s.regionMatches(true, matcher.start(1), "realm", 0, 5)) {
                    group2 = matcher.group(3);
                }
                else {
                    group2 = s2;
                    if (s.regionMatches(true, matcher.start(1), "charset", 0, 7)) {
                        group = matcher.group(3);
                        group2 = s2;
                    }
                }
                if (group2 != null && group != null) {
                    s3 = group2;
                    s4 = group;
                    break;
                }
                n = matcher.end();
                s2 = group2;
            }
            if (s3 == null) {
                continue;
            }
            Challenge withCharset = new Challenge(substring, s3);
            if (s4 != null) {
                if (!s4.equalsIgnoreCase("UTF-8")) {
                    continue;
                }
                withCharset = withCharset.withCharset(Util.UTF_8);
            }
            list.add(withCharset);
        }
        return list;
    }
    
    public static int parseSeconds(final String s, final int n) {
        try {
            final long long1 = Long.parseLong(s);
            if (long1 > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            if (long1 < 0L) {
                return 0;
            }
            return (int)long1;
        }
        catch (NumberFormatException ex) {
            return n;
        }
    }
    
    public static void receiveHeaders(final CookieJar cookieJar, final HttpUrl httpUrl, final Headers headers) {
        if (cookieJar == CookieJar.NO_COOKIES) {
            return;
        }
        final List<Cookie> all = Cookie.parseAll(httpUrl, headers);
        if (all.isEmpty()) {
            return;
        }
        cookieJar.saveFromResponse(httpUrl, all);
    }
    
    public static int skipUntil(final String s, int i, final String s2) {
        while (i < s.length()) {
            if (s2.indexOf(s.charAt(i)) != -1) {
                return i;
            }
            ++i;
        }
        return i;
    }
    
    public static int skipWhitespace(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (char1 != ' ' && char1 != '\t') {
                return i;
            }
            ++i;
        }
        return i;
    }
    
    private static long stringToLong(final String s) {
        if (s == null) {
            return -1L;
        }
        try {
            return Long.parseLong(s);
        }
        catch (NumberFormatException ex) {
            return -1L;
        }
    }
}
