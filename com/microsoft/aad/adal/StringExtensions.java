package com.microsoft.aad.adal;

import android.util.*;
import java.security.*;
import java.io.*;
import java.util.*;
import android.net.*;
import java.net.*;

final class StringExtensions
{
    public static final String ENCODING_UTF8 = "UTF_8";
    private static final String TAG;
    private static final String TOKEN_HASH_ALGORITHM = "SHA256";
    
    static {
        TAG = StringExtensions.class.getSimpleName();
    }
    
    private StringExtensions() {
    }
    
    public static String createHash(final String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (!isNullOrBlank(s)) {
            return new String(Base64.encode(MessageDigest.getInstance("SHA256").digest(s.getBytes("UTF_8")), 2), "UTF_8");
        }
        return s;
    }
    
    static String encodeBase64URLSafeString(final byte[] array) throws UnsupportedEncodingException {
        return new String(Base64.encode(array, 11), "UTF_8");
    }
    
    static List<String> getStringTokens(final String s, final String s2) {
        final StringTokenizer stringTokenizer = new StringTokenizer(s, s2);
        final ArrayList<String> list = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()) {
            final String nextToken = stringTokenizer.nextToken();
            if (!isNullOrBlank(nextToken)) {
                list.add(nextToken);
            }
        }
        return list;
    }
    
    static URL getUrl(final String s) {
        try {
            return new URL(s);
        }
        catch (MalformedURLException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append(StringExtensions.TAG);
            sb.append(":getUrl");
            Logger.e(sb.toString(), ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL.getDescription(), ex.getMessage(), ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, ex);
            return null;
        }
    }
    
    static HashMap<String, String> getUrlParameters(final String s) {
        final Uri parse = Uri.parse(s);
        final HashMap<String, String> urlFormDecode = HashMapExtensions.urlFormDecode(parse.getFragment());
        if (urlFormDecode != null) {
            final HashMap<String, String> urlFormDecode2 = urlFormDecode;
            if (!urlFormDecode.isEmpty()) {
                return urlFormDecode2;
            }
        }
        return HashMapExtensions.urlFormDecode(parse.getEncodedQuery());
    }
    
    static boolean hasPrefixInHeader(final String s, final String s2) {
        return s.startsWith(s2) && s.length() > s2.length() + 2 && Character.isWhitespace(s.charAt(s2.length()));
    }
    
    static boolean isNullOrBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }
    
    static String removeQuoteInHeaderValue(final String s) {
        if (!isNullOrBlank(s)) {
            return s.replace("\"", "");
        }
        return null;
    }
    
    static ArrayList<String> splitWithQuotes(String substring, final char c) {
        final ArrayList<String> list = new ArrayList<String>();
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < substring.length()) {
            int n3;
            int n4;
            if (substring.charAt(i) == c && n2 == 0) {
                final String substring2 = substring.substring(n, i);
                if (!isNullOrBlank(substring2.trim())) {
                    list.add(substring2);
                }
                n3 = i + 1;
                n4 = n2;
            }
            else {
                n3 = n;
                n4 = n2;
                if (substring.charAt(i) == '\"') {
                    n4 = (n2 ^ 0x1);
                    n3 = n;
                }
            }
            ++i;
            n = n3;
            n2 = n4;
        }
        substring = substring.substring(n);
        if (!isNullOrBlank(substring.trim())) {
            list.add(substring);
        }
        return list;
    }
    
    static String urlFormDecode(final String s) throws UnsupportedEncodingException {
        return URLDecoder.decode(s, "UTF_8");
    }
    
    static String urlFormEncode(final String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF_8");
    }
}
