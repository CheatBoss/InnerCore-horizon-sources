package org.apache.http.client.utils;

import org.apache.http.*;
import java.net.*;
import java.io.*;
import java.util.*;

@Deprecated
public class URLEncodedUtils
{
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    
    public URLEncodedUtils() {
        throw new RuntimeException("Stub!");
    }
    
    public static String format(final List<? extends NameValuePair> list, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public static boolean isEncoded(final HttpEntity httpEntity) {
        throw new RuntimeException("Stub!");
    }
    
    public static List<NameValuePair> parse(final URI uri, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public static List<NameValuePair> parse(final HttpEntity httpEntity) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public static void parse(final List<NameValuePair> list, final Scanner scanner, final String s) {
        throw new RuntimeException("Stub!");
    }
}
