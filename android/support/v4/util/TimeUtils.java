package android.support.v4.util;

import java.io.*;

public final class TimeUtils
{
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;
    
    static {
        sFormatSync = new Object();
        TimeUtils.sFormatStr = new char[24];
    }
    
    private TimeUtils() {
    }
    
    private static int accumField(final int n, final int n2, final boolean b, final int n3) {
        if (n > 99 || (b && n3 >= 3)) {
            return n2 + 3;
        }
        if (n > 9 || (b && n3 >= 2)) {
            return n2 + 2;
        }
        if (!b && n <= 0) {
            return 0;
        }
        return n2 + 1;
    }
    
    public static void formatDuration(final long n, final long n2, final PrintWriter printWriter) {
        if (n == 0L) {
            printWriter.print("--");
            return;
        }
        formatDuration(n - n2, printWriter, 0);
    }
    
    public static void formatDuration(final long n, final PrintWriter printWriter) {
        formatDuration(n, printWriter, 0);
    }
    
    public static void formatDuration(final long n, final PrintWriter printWriter, int formatDurationLocked) {
        synchronized (TimeUtils.sFormatSync) {
            formatDurationLocked = formatDurationLocked(n, formatDurationLocked);
            printWriter.print(new String(TimeUtils.sFormatStr, 0, formatDurationLocked));
        }
    }
    
    public static void formatDuration(final long n, final StringBuilder sb) {
        synchronized (TimeUtils.sFormatSync) {
            sb.append(TimeUtils.sFormatStr, 0, formatDurationLocked(n, 0));
        }
    }
    
    private static int formatDurationLocked(long n, int printField) {
        if (TimeUtils.sFormatStr.length < printField) {
            TimeUtils.sFormatStr = new char[printField];
        }
        final char[] sFormatStr = TimeUtils.sFormatStr;
        if (n == 0L) {
            while (printField - 1 < 0) {
                sFormatStr[0] = ' ';
            }
            sFormatStr[0] = '0';
            return 1;
        }
        char c;
        if (n > 0L) {
            c = '+';
        }
        else {
            c = '-';
            n = -n;
        }
        final int n2 = (int)(n % 1000L);
        int n3 = (int)Math.floor((double)(n / 1000L));
        int n4;
        if (n3 > 86400) {
            n4 = n3 / 86400;
            n3 -= 86400 * n4;
        }
        else {
            n4 = 0;
        }
        int n5;
        if (n3 > 3600) {
            n5 = n3 / 3600;
            n3 -= n5 * 3600;
        }
        else {
            n5 = 0;
        }
        int n6;
        int n7;
        if (n3 > 60) {
            n6 = n3 / 60;
            n7 = n3 - n6 * 60;
        }
        else {
            n6 = 0;
            n7 = n3;
        }
        int n14;
        if (printField != 0) {
            final int accumField = accumField(n4, 1, false, 0);
            final int n8 = accumField + accumField(n5, 1, accumField > 0, 2);
            final int n9 = n8 + accumField(n6, 1, n8 > 0, 2);
            final int n10 = n9 + accumField(n7, 1, n9 > 0, 2);
            int n11;
            if (n10 > 0) {
                n11 = 3;
            }
            else {
                n11 = 0;
            }
            int n12 = n10 + (accumField(n2, 2, true, n11) + 1);
            int n13 = 0;
            while (true) {
                n14 = n13;
                if (n12 >= printField) {
                    break;
                }
                sFormatStr[n13] = ' ';
                ++n13;
                ++n12;
            }
        }
        else {
            n14 = 0;
        }
        sFormatStr[n14] = c;
        final int n15 = n14 + 1;
        if (printField != 0) {
            printField = 1;
        }
        else {
            printField = 0;
        }
        final int printField2 = printField(sFormatStr, n4, 'd', n15, false, 0);
        final boolean b = printField2 != n15;
        int n16;
        if (printField != 0) {
            n16 = 2;
        }
        else {
            n16 = 0;
        }
        final int printField3 = printField(sFormatStr, n5, 'h', printField2, b, n16);
        final boolean b2 = printField3 != n15;
        int n17;
        if (printField != 0) {
            n17 = 2;
        }
        else {
            n17 = 0;
        }
        final int printField4 = printField(sFormatStr, n6, 'm', printField3, b2, n17);
        final boolean b3 = printField4 != n15;
        int n18;
        if (printField != 0) {
            n18 = 2;
        }
        else {
            n18 = 0;
        }
        final int printField5 = printField(sFormatStr, n7, 's', printField4, b3, n18);
        if (printField != 0 && printField5 != n15) {
            printField = 3;
        }
        else {
            printField = 0;
        }
        printField = printField(sFormatStr, n2, 'm', printField5, true, printField);
        sFormatStr[printField] = 's';
        return printField + 1;
    }
    
    private static int printField(final char[] array, int n, final char c, int n2, final boolean b, int n3) {
        if (!b) {
            final int n4 = n2;
            if (n <= 0) {
                return n4;
            }
        }
        int n6;
        if ((b && n3 >= 3) || n > 99) {
            final int n5 = n / 100;
            array[n2] = (char)(n5 + 48);
            n6 = n2 + 1;
            n -= n5 * 100;
        }
        else {
            n6 = n2;
        }
        int n7 = 0;
        Label_0121: {
            if ((!b || n3 < 2) && n <= 9) {
                n7 = n6;
                n3 = n;
                if (n2 == n6) {
                    break Label_0121;
                }
            }
            n2 = n / 10;
            array[n6] = (char)(n2 + 48);
            n7 = n6 + 1;
            n3 = n - n2 * 10;
        }
        array[n7] = (char)(n3 + 48);
        n = n7 + 1;
        array[n] = c;
        return n + 1;
    }
}
