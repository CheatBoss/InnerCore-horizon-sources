package androidx.core.util;

import androidx.annotation.*;
import java.io.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public final class TimeUtils
{
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
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
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static void formatDuration(final long n, final long n2, final PrintWriter printWriter) {
        if (n == 0L) {
            printWriter.print("--");
            return;
        }
        formatDuration(n - n2, printWriter, 0);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static void formatDuration(final long n, final PrintWriter printWriter) {
        formatDuration(n, printWriter, 0);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static void formatDuration(final long n, final PrintWriter printWriter, int formatDurationLocked) {
        synchronized (TimeUtils.sFormatSync) {
            formatDurationLocked = formatDurationLocked(n, formatDurationLocked);
            printWriter.print(new String(TimeUtils.sFormatStr, 0, formatDurationLocked));
        }
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
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
            return 0 + 1;
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
        final int n3 = (int)Math.floor((double)(n / 1000L));
        int n4 = 0;
        int n5 = n3;
        if (n3 > 86400) {
            n4 = n3 / 86400;
            n5 = n3 - 86400 * n4;
        }
        int n6;
        if (n5 > 3600) {
            n6 = n5 / 3600;
            n5 -= n6 * 3600;
        }
        else {
            n6 = 0;
        }
        int n7;
        int n8;
        if (n5 > 60) {
            n7 = n5 / 60;
            n8 = n5 - n7 * 60;
        }
        else {
            n7 = 0;
            n8 = n5;
        }
        int n9 = 0;
        final int n10 = 0;
        final int n11 = 3;
        boolean b = false;
        if (printField != 0) {
            final int accumField = accumField(n4, 1, false, 0);
            if (accumField > 0) {
                b = true;
            }
            final int n12 = accumField + accumField(n6, 1, b, 2);
            final int n13 = n12 + accumField(n7, 1, n12 > 0, 2);
            final int n14 = n13 + accumField(n8, 1, n13 > 0, 2);
            int n15;
            if (n14 > 0) {
                n15 = 3;
            }
            else {
                n15 = 0;
            }
            int n16 = n14 + (accumField(n2, 2, true, n15) + 1);
            int n17 = n10;
            while (true) {
                n9 = n17;
                if (n16 >= printField) {
                    break;
                }
                sFormatStr[n17] = ' ';
                ++n17;
                ++n16;
            }
        }
        sFormatStr[n9] = c;
        final int n18 = n9 + 1;
        if (printField != 0) {
            printField = 1;
        }
        else {
            printField = 0;
        }
        final boolean b2 = true;
        final int n19 = 2;
        final int printField2 = printField(sFormatStr, n4, 'd', n18, false, 0);
        final boolean b3 = printField2 != n18;
        int n20;
        if (printField != 0) {
            n20 = 2;
        }
        else {
            n20 = 0;
        }
        final int printField3 = printField(sFormatStr, n6, 'h', printField2, b3, n20);
        final boolean b4 = printField3 != n18;
        int n21;
        if (printField != 0) {
            n21 = 2;
        }
        else {
            n21 = 0;
        }
        final int printField4 = printField(sFormatStr, n7, 'm', printField3, b4, n21);
        final boolean b5 = printField4 != n18 && b2;
        int n22;
        if (printField != 0) {
            n22 = n19;
        }
        else {
            n22 = 0;
        }
        final int printField5 = printField(sFormatStr, n8, 's', printField4, b5, n22);
        if (printField != 0 && printField5 != n18) {
            printField = n11;
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
        int n5 = 0;
        int n6 = 0;
        Label_0064: {
            if (!b || n3 < 3) {
                n5 = n;
                n6 = n2;
                if (n <= 99) {
                    break Label_0064;
                }
            }
            final int n7 = n / 100;
            array[n2] = (char)(n7 + 48);
            n6 = n2 + 1;
            n5 = n - n7 * 100;
        }
        Label_0124: {
            if ((!b || n3 < 2) && n5 <= 9) {
                n3 = n5;
                if (n2 == (n = n6)) {
                    break Label_0124;
                }
            }
            n2 = n5 / 10;
            array[n6] = (char)(n2 + 48);
            n = n6 + 1;
            n3 = n5 - n2 * 10;
        }
        array[n] = (char)(n3 + 48);
        ++n;
        array[n] = c;
        return n + 1;
    }
}
