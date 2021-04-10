package bo.app;

import java.text.*;
import java.util.*;
import com.appboy.support.*;

public final class du
{
    private static final String a;
    private static final TimeZone b;
    private static final EnumSet<u> c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(du.class);
        b = TimeZone.getTimeZone("UTC");
        c = EnumSet.of(u.a, u.b, u.c);
    }
    
    public static long a() {
        return System.currentTimeMillis() / 1000L;
    }
    
    public static long a(final Date date) {
        return date.getTime() / 1000L;
    }
    
    public static String a(final Date date, final u u) {
        u b = u;
        if (!du.c.contains(u)) {
            final String a = du.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported date format: ");
            sb.append(u);
            sb.append(". Defaulting to ");
            sb.append(u.b);
            AppboyLogger.w(a, sb.toString());
            b = u.b;
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(b.a(), Locale.US);
        simpleDateFormat.setTimeZone(du.b);
        return simpleDateFormat.format(date);
    }
    
    public static Date a(final int n, final int n2, final int n3) {
        return a(n, n2, n3, 0, 0, 0);
    }
    
    public static Date a(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(n, n2, n3, n4, n5, n6);
        gregorianCalendar.setTimeZone(du.b);
        return gregorianCalendar.getTime();
    }
    
    public static Date a(final long n) {
        return new Date(n * 1000L);
    }
    
    public static Date a(String a, final u u) {
        if (StringUtils.isNullOrBlank(a)) {
            final String a2 = du.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Null or blank date string received: ");
            sb.append(a);
            AppboyLogger.w(a2, sb.toString());
            return null;
        }
        if (!du.c.contains(u)) {
            a = du.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unsupported date format. Returning null. Got date format: ");
            sb2.append(u);
            AppboyLogger.w(a, sb2.toString());
            return null;
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(u.a(), Locale.US);
        simpleDateFormat.setTimeZone(du.b);
        try {
            return simpleDateFormat.parse(a);
        }
        catch (Exception ex) {
            final String a3 = du.a;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Exception parsing date ");
            sb3.append(a);
            sb3.append(". Returning null");
            AppboyLogger.e(a3, sb3.toString(), ex);
            return null;
        }
    }
    
    public static double b() {
        final double n = (double)System.currentTimeMillis();
        Double.isNaN(n);
        return n / 1000.0;
    }
    
    public static long c() {
        return System.currentTimeMillis();
    }
}
