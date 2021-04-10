package okhttp3.internal.http;

import okhttp3.internal.*;
import java.util.*;
import java.text.*;

public final class HttpDate
{
    private static final DateFormat[] BROWSER_COMPATIBLE_DATE_FORMATS;
    private static final String[] BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS;
    private static final ThreadLocal<DateFormat> STANDARD_DATE_FORMAT;
    
    static {
        STANDARD_DATE_FORMAT = new ThreadLocal<DateFormat>() {
            @Override
            protected DateFormat initialValue() {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
                simpleDateFormat.setLenient(false);
                simpleDateFormat.setTimeZone(Util.UTC);
                return simpleDateFormat;
            }
        };
        BROWSER_COMPATIBLE_DATE_FORMATS = new DateFormat[(BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM d yyyy HH:mm:ss z" }).length];
    }
    
    public static String format(final Date date) {
        return HttpDate.STANDARD_DATE_FORMAT.get().format(date);
    }
    
    public static Date parse(final String s) {
        if (s.length() == 0) {
            return null;
        }
        final ParsePosition parsePosition = new ParsePosition(0);
        final Date parse = HttpDate.STANDARD_DATE_FORMAT.get().parse(s, parsePosition);
        if (parsePosition.getIndex() == s.length()) {
            return parse;
        }
        while (true) {
            while (true) {
                int n;
                synchronized (HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS) {
                    final int length = HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length;
                    n = 0;
                    if (n >= length) {
                        return null;
                    }
                    DateFormat dateFormat;
                    if ((dateFormat = HttpDate.BROWSER_COMPATIBLE_DATE_FORMATS[n]) == null) {
                        dateFormat = new SimpleDateFormat(HttpDate.BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS[n], Locale.US);
                        dateFormat.setTimeZone(Util.UTC);
                        HttpDate.BROWSER_COMPATIBLE_DATE_FORMATS[n] = dateFormat;
                    }
                    parsePosition.setIndex(0);
                    final Date parse2 = dateFormat.parse(s, parsePosition);
                    if (parsePosition.getIndex() != 0) {
                        return parse2;
                    }
                }
                ++n;
                continue;
            }
        }
    }
}
