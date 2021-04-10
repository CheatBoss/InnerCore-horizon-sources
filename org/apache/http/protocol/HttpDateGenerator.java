package org.apache.http.protocol;

import java.util.*;

@Deprecated
public class HttpDateGenerator
{
    public static final TimeZone GMT;
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    
    public HttpDateGenerator() {
        throw new RuntimeException("Stub!");
    }
    
    public String getCurrentDate() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
}
