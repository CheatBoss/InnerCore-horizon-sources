package org.apache.http.impl.cookie;

import java.util.*;

@Deprecated
public final class DateUtils
{
    public static final TimeZone GMT;
    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
    public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    
    DateUtils() {
        throw new RuntimeException("Stub!");
    }
    
    public static String formatDate(final Date date) {
        throw new RuntimeException("Stub!");
    }
    
    public static String formatDate(final Date date, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public static Date parseDate(final String s) throws DateParseException {
        throw new RuntimeException("Stub!");
    }
    
    public static Date parseDate(final String s, final String[] array) throws DateParseException {
        throw new RuntimeException("Stub!");
    }
    
    public static Date parseDate(final String s, final String[] array, final Date date) throws DateParseException {
        throw new RuntimeException("Stub!");
    }
}
