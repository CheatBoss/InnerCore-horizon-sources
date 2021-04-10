package org.mozilla.javascript;

import java.util.*;
import java.text.*;

final class NativeDate extends IdScriptableObject
{
    private static final int ConstructorId_UTC = -1;
    private static final int ConstructorId_now = -3;
    private static final int ConstructorId_parse = -2;
    private static final Object DATE_TAG;
    private static final double HalfTimeDomain = 8.64E15;
    private static final double HoursPerDay = 24.0;
    private static final int Id_constructor = 1;
    private static final int Id_getDate = 17;
    private static final int Id_getDay = 19;
    private static final int Id_getFullYear = 13;
    private static final int Id_getHours = 21;
    private static final int Id_getMilliseconds = 27;
    private static final int Id_getMinutes = 23;
    private static final int Id_getMonth = 15;
    private static final int Id_getSeconds = 25;
    private static final int Id_getTime = 11;
    private static final int Id_getTimezoneOffset = 29;
    private static final int Id_getUTCDate = 18;
    private static final int Id_getUTCDay = 20;
    private static final int Id_getUTCFullYear = 14;
    private static final int Id_getUTCHours = 22;
    private static final int Id_getUTCMilliseconds = 28;
    private static final int Id_getUTCMinutes = 24;
    private static final int Id_getUTCMonth = 16;
    private static final int Id_getUTCSeconds = 26;
    private static final int Id_getYear = 12;
    private static final int Id_setDate = 39;
    private static final int Id_setFullYear = 43;
    private static final int Id_setHours = 37;
    private static final int Id_setMilliseconds = 31;
    private static final int Id_setMinutes = 35;
    private static final int Id_setMonth = 41;
    private static final int Id_setSeconds = 33;
    private static final int Id_setTime = 30;
    private static final int Id_setUTCDate = 40;
    private static final int Id_setUTCFullYear = 44;
    private static final int Id_setUTCHours = 38;
    private static final int Id_setUTCMilliseconds = 32;
    private static final int Id_setUTCMinutes = 36;
    private static final int Id_setUTCMonth = 42;
    private static final int Id_setUTCSeconds = 34;
    private static final int Id_setYear = 45;
    private static final int Id_toDateString = 4;
    private static final int Id_toGMTString = 8;
    private static final int Id_toISOString = 46;
    private static final int Id_toJSON = 47;
    private static final int Id_toLocaleDateString = 7;
    private static final int Id_toLocaleString = 5;
    private static final int Id_toLocaleTimeString = 6;
    private static final int Id_toSource = 9;
    private static final int Id_toString = 2;
    private static final int Id_toTimeString = 3;
    private static final int Id_toUTCString = 8;
    private static final int Id_valueOf = 10;
    private static double LocalTZA = 0.0;
    private static final int MAXARGS = 7;
    private static final int MAX_PROTOTYPE_ID = 47;
    private static final double MinutesPerDay = 1440.0;
    private static final double MinutesPerHour = 60.0;
    private static final double SecondsPerDay = 86400.0;
    private static final double SecondsPerHour = 3600.0;
    private static final double SecondsPerMinute = 60.0;
    private static final String js_NaN_date_str = "Invalid Date";
    private static DateFormat localeDateFormatter;
    private static DateFormat localeDateTimeFormatter;
    private static DateFormat localeTimeFormatter;
    private static final double msPerDay = 8.64E7;
    private static final double msPerHour = 3600000.0;
    private static final double msPerMinute = 60000.0;
    private static final double msPerSecond = 1000.0;
    static final long serialVersionUID = -8307438915861678966L;
    private static TimeZone thisTimeZone;
    private static DateFormat timeZoneFormatter;
    private double date;
    
    static {
        DATE_TAG = "Date";
    }
    
    private NativeDate() {
        if (NativeDate.thisTimeZone == null) {
            NativeDate.thisTimeZone = TimeZone.getDefault();
            NativeDate.LocalTZA = NativeDate.thisTimeZone.getRawOffset();
        }
    }
    
    private static int DateFromTime(final double n) {
        final int yearFromTime = YearFromTime(n);
        final int n2 = (int)(Day(n) - DayFromYear(yearFromTime)) - 59;
        if (n2 < 0) {
            int n3;
            if (n2 < -28) {
                n3 = n2 + 31 + 28;
            }
            else {
                n3 = n2 + 28;
            }
            return n3 + 1;
        }
        int n4 = n2;
        if (IsLeapYear(yearFromTime)) {
            if (n2 == 0) {
                return 29;
            }
            n4 = n2 - 1;
        }
        int n5 = 0;
        int n6 = 0;
        switch (n4 / 30) {
            default: {
                throw Kit.codeBug();
            }
            case 10: {
                return n4 - 275 + 1;
            }
            case 9: {
                n5 = 30;
                n6 = 275;
                break;
            }
            case 8: {
                n5 = 31;
                n6 = 245;
                break;
            }
            case 7: {
                n5 = 30;
                n6 = 214;
                break;
            }
            case 6: {
                n5 = 31;
                n6 = 184;
                break;
            }
            case 5: {
                n5 = 31;
                n6 = 153;
                break;
            }
            case 4: {
                n5 = 30;
                n6 = 122;
                break;
            }
            case 3: {
                n5 = 31;
                n6 = 92;
                break;
            }
            case 2: {
                n5 = 30;
                n6 = 61;
                break;
            }
            case 1: {
                n5 = 31;
                n6 = 31;
                break;
            }
            case 0: {
                return n4 + 1;
            }
        }
        int n8;
        final int n7 = n8 = n4 - n6;
        if (n7 < 0) {
            n8 = n7 + n5;
        }
        return n8 + 1;
    }
    
    private static double Day(final double n) {
        return Math.floor(n / 8.64E7);
    }
    
    private static double DayFromMonth(final int n, final int n2) {
        final int n3 = n * 30;
        int n4;
        if (n >= 7) {
            n4 = n3 + (n / 2 - 1);
        }
        else if (n >= 2) {
            n4 = n3 + ((n - 1) / 2 - 1);
        }
        else {
            n4 = n3 + n;
        }
        int n5 = n4;
        if (n >= 2) {
            n5 = n4;
            if (IsLeapYear(n2)) {
                n5 = n4 + 1;
            }
        }
        return n5;
    }
    
    private static double DayFromYear(final double n) {
        return (n - 1970.0) * 365.0 + Math.floor((n - 1969.0) / 4.0) - Math.floor((n - 1901.0) / 100.0) + Math.floor((n - 1601.0) / 400.0);
    }
    
    private static double DaylightSavingTA(final double n) {
        double makeDate = n;
        if (n < 0.0) {
            makeDate = MakeDate(MakeDay(EquivalentYear(YearFromTime(n)), MonthFromTime(n), DateFromTime(n)), TimeWithinDay(n));
        }
        if (NativeDate.thisTimeZone.inDaylightTime(new Date((long)makeDate))) {
            return 3600000.0;
        }
        return 0.0;
    }
    
    private static int DaysInMonth(final int n, final int n2) {
        if (n2 == 2) {
            if (IsLeapYear(n)) {
                return 29;
            }
            return 28;
        }
        else {
            if (n2 >= 8) {
                return 31 - (n2 & 0x1);
            }
            return (n2 & 0x1) + 30;
        }
    }
    
    private static int EquivalentYear(final int n) {
        int n3;
        final int n2 = n3 = ((int)DayFromYear(n) + 4) % 7;
        if (n2 < 0) {
            n3 = n2 + 7;
        }
        if (IsLeapYear(n)) {
            switch (n3) {
                case 6: {
                    return 1972;
                }
                case 5: {
                    return 1988;
                }
                case 4: {
                    return 1976;
                }
                case 3: {
                    return 1992;
                }
                case 2: {
                    return 1980;
                }
                case 1: {
                    return 1996;
                }
                case 0: {
                    return 1984;
                }
            }
        }
        else {
            switch (n3) {
                case 6: {
                    return 1977;
                }
                case 5: {
                    return 1971;
                }
                case 4: {
                    return 1981;
                }
                case 3: {
                    return 1986;
                }
                case 2: {
                    return 1985;
                }
                case 1: {
                    return 1973;
                }
                case 0: {
                    return 1978;
                }
            }
        }
        throw Kit.codeBug();
    }
    
    private static int HourFromTime(double n) {
        final double n2 = n = Math.floor(n / 3600000.0) % 24.0;
        if (n2 < 0.0) {
            n = n2 + 24.0;
        }
        return (int)n;
    }
    
    private static boolean IsLeapYear(final int n) {
        return n % 4 == 0 && (n % 100 != 0 || n % 400 == 0);
    }
    
    private static double LocalTime(final double n) {
        return NativeDate.LocalTZA + n + DaylightSavingTA(n);
    }
    
    private static double MakeDate(final double n, final double n2) {
        return 8.64E7 * n + n2;
    }
    
    private static double MakeDay(double n, double n2, final double n3) {
        final double n4 = n + Math.floor(n2 / 12.0);
        n2 = (n = n2 % 12.0);
        if (n2 < 0.0) {
            n = n2 + 12.0;
        }
        return Math.floor(TimeFromYear(n4) / 8.64E7) + DayFromMonth((int)n, (int)n4) + n3 - 1.0;
    }
    
    private static double MakeTime(final double n, final double n2, final double n3, final double n4) {
        return ((n * 60.0 + n2) * 60.0 + n3) * 1000.0 + n4;
    }
    
    private static int MinFromTime(double n) {
        final double n2 = n = Math.floor(n / 60000.0) % 60.0;
        if (n2 < 0.0) {
            n = n2 + 60.0;
        }
        return (int)n;
    }
    
    private static int MonthFromTime(final double n) {
        final int yearFromTime = YearFromTime(n);
        final int n2 = (int)(Day(n) - DayFromYear(yearFromTime)) - 59;
        int n3 = 1;
        if (n2 < 0) {
            if (n2 < -28) {
                n3 = 0;
            }
            return n3;
        }
        int n4 = n2;
        if (IsLeapYear(yearFromTime)) {
            if (n2 == 0) {
                return 1;
            }
            n4 = n2 - 1;
        }
        final int n5 = n4 / 30;
        int n6 = 0;
        switch (n5) {
            default: {
                throw Kit.codeBug();
            }
            case 10: {
                return 11;
            }
            case 9: {
                n6 = 275;
                break;
            }
            case 8: {
                n6 = 245;
                break;
            }
            case 7: {
                n6 = 214;
                break;
            }
            case 6: {
                n6 = 184;
                break;
            }
            case 5: {
                n6 = 153;
                break;
            }
            case 4: {
                n6 = 122;
                break;
            }
            case 3: {
                n6 = 92;
                break;
            }
            case 2: {
                n6 = 61;
                break;
            }
            case 1: {
                n6 = 31;
                break;
            }
            case 0: {
                return 2;
            }
        }
        if (n4 >= n6) {
            return n5 + 2;
        }
        return n5 + 1;
    }
    
    private static int SecFromTime(double n) {
        final double n2 = n = Math.floor(n / 1000.0) % 60.0;
        if (n2 < 0.0) {
            n = n2 + 60.0;
        }
        return (int)n;
    }
    
    private static double TimeClip(final double n) {
        if (n != n || n == Double.POSITIVE_INFINITY || n == Double.NEGATIVE_INFINITY || Math.abs(n) > 8.64E15) {
            return ScriptRuntime.NaN;
        }
        if (n > 0.0) {
            return Math.floor(0.0 + n);
        }
        return Math.ceil(0.0 + n);
    }
    
    private static double TimeFromYear(final double n) {
        return DayFromYear(n) * 8.64E7;
    }
    
    private static double TimeWithinDay(double n) {
        final double n2 = n %= 8.64E7;
        if (n2 < 0.0) {
            n = n2 + 8.64E7;
        }
        return n;
    }
    
    private static int WeekDay(double n) {
        final double n2 = n = (Day(n) + 4.0) % 7.0;
        if (n2 < 0.0) {
            n = n2 + 7.0;
        }
        return (int)n;
    }
    
    private static int YearFromTime(final double n) {
        final int n2 = (int)Math.floor(n / 8.64E7 / 366.0) + 1970;
        int i;
        final int n3 = i = (int)Math.floor(n / 8.64E7 / 365.0) + 1970;
        int n4 = n2;
        if (n3 < n2) {
            n4 = n3;
            i = n2;
        }
        while (i > n4) {
            final int n5 = (i + n4) / 2;
            if (TimeFromYear(n5) > n) {
                i = n5 - 1;
            }
            else {
                if (TimeFromYear(n4 = n5 + 1) > n) {
                    return n5;
                }
                continue;
            }
        }
        return n4;
    }
    
    private static void append0PaddedUint(final StringBuilder sb, final int n, int n2) {
        if (n < 0) {
            Kit.codeBug();
        }
        final int n3 = 1;
        final int n4 = 1;
        final int n5 = n2 - 1;
        n2 = n3;
        int n6 = n5;
        if (n >= 10) {
            if (n < 1000000000) {
                n6 = n5;
                n2 = n4;
                while (true) {
                    final int n7 = n2 * 10;
                    if (n < n7) {
                        break;
                    }
                    --n6;
                    n2 = n7;
                }
            }
            else {
                n6 = n5 - 9;
                n2 = 1000000000;
            }
        }
        int i;
        int n8;
        while (true) {
            i = n2;
            n8 = n;
            if (n6 <= 0) {
                break;
            }
            sb.append('0');
            --n6;
        }
        while (i != 1) {
            sb.append((char)(n8 / i + 48));
            n8 %= i;
            i /= 10;
        }
        sb.append((char)(n8 + 48));
    }
    
    private static void appendMonthName(final StringBuilder sb, final int n) {
        for (int i = 0; i != 3; ++i) {
            sb.append("JanFebMarAprMayJunJulAugSepOctNovDec".charAt(n * 3 + i));
        }
    }
    
    private static void appendWeekDayName(final StringBuilder sb, final int n) {
        for (int i = 0; i != 3; ++i) {
            sb.append("SunMonTueWedThuFriSat".charAt(n * 3 + i));
        }
    }
    
    private static String date_format(final double n, int n2) {
        final StringBuilder sb = new StringBuilder(60);
        final double localTime = LocalTime(n);
        if (n2 != 3) {
            appendWeekDayName(sb, WeekDay(localTime));
            sb.append(' ');
            appendMonthName(sb, MonthFromTime(localTime));
            sb.append(' ');
            append0PaddedUint(sb, DateFromTime(localTime), 2);
            sb.append(' ');
            final int yearFromTime = YearFromTime(localTime);
            int n3;
            if ((n3 = yearFromTime) < 0) {
                sb.append('-');
                n3 = -yearFromTime;
            }
            append0PaddedUint(sb, n3, 4);
            if (n2 != 4) {
                sb.append(' ');
            }
        }
        if (n2 != 4) {
            append0PaddedUint(sb, HourFromTime(localTime), 2);
            sb.append(':');
            append0PaddedUint(sb, MinFromTime(localTime), 2);
            sb.append(':');
            append0PaddedUint(sb, SecFromTime(localTime), 2);
            n2 = (int)Math.floor((NativeDate.LocalTZA + DaylightSavingTA(n)) / 60000.0);
            n2 = n2 / 60 * 100 + n2 % 60;
            if (n2 > 0) {
                sb.append(" GMT+");
            }
            else {
                sb.append(" GMT-");
                n2 = -n2;
            }
            append0PaddedUint(sb, n2, 4);
            if (NativeDate.timeZoneFormatter == null) {
                NativeDate.timeZoneFormatter = new SimpleDateFormat("zzz");
            }
            double makeDate = n;
            if (n < 0.0) {
                makeDate = MakeDate(MakeDay(EquivalentYear(YearFromTime(localTime)), MonthFromTime(n), DateFromTime(n)), TimeWithinDay(n));
            }
            sb.append(" (");
            final Date date = new Date((long)makeDate);
            synchronized (NativeDate.timeZoneFormatter) {
                sb.append(NativeDate.timeZoneFormatter.format(date));
                // monitorexit(NativeDate.timeZoneFormatter)
                sb.append(')');
            }
        }
        return sb.toString();
    }
    
    private static double date_msecFromArgs(final Object[] array) {
        final double[] array2 = new double[7];
        for (int i = 0; i < 7; ++i) {
            if (i < array.length) {
                final double number = ScriptRuntime.toNumber(array[i]);
                if (number != number || Double.isInfinite(number)) {
                    return ScriptRuntime.NaN;
                }
                array2[i] = ScriptRuntime.toInteger(array[i]);
            }
            else if (i == 2) {
                array2[i] = 1.0;
            }
            else {
                array2[i] = 0.0;
            }
        }
        if (array2[0] >= 0.0 && array2[0] <= 99.0) {
            array2[0] += 1900.0;
        }
        return date_msecFromDate(array2[0], array2[1], array2[2], array2[3], array2[4], array2[5], array2[6]);
    }
    
    private static double date_msecFromDate(final double n, final double n2, final double n3, final double n4, final double n5, final double n6, final double n7) {
        return MakeDate(MakeDay(n, n2, n3), MakeTime(n4, n5, n6, n7));
    }
    
    private static double date_parseString(final String s) {
        double isoString = parseISOString(s);
        if (isoString == isoString) {
            return isoString;
        }
        int i = 0;
        int n = 0;
        int length = s.length();
        int n2 = -1;
        int n3 = -1;
        int n4 = -1;
        double n5 = -1.0;
        int n6 = -1;
        int n7 = -1;
        int n8 = -1;
        int n9 = 0;
        while (i < length) {
            final char char1;
            final char c = char1 = s.charAt(i);
            ++i;
            int n14 = 0;
            int n35 = 0;
            Label_1318: {
                int n13 = 0;
                int n15 = 0;
                int n16 = 0;
                Label_1302: {
                    Label_1260: {
                        if (char1 <= ' ' || char1 == ',' || char1 == '-') {
                            final double n10 = isoString;
                            final int n11 = n9;
                            final int n12 = length;
                            if (i < n12) {
                                final char char2 = s.charAt(i);
                                if (char1 == '-' && '0' <= char2 && char2 <= '9') {
                                    break Label_1260;
                                }
                            }
                            isoString = n10;
                            n13 = n11;
                            n14 = n3;
                            n15 = n4;
                            n16 = n12;
                            break Label_1302;
                        }
                        if (char1 == '(') {
                            int n17 = 1;
                            int n18 = i;
                            while ((i = n18) < length) {
                                final char char3 = s.charAt(n18);
                                i = n18 + 1;
                                if (char3 == '(') {
                                    ++n17;
                                    n18 = i;
                                }
                                else {
                                    n18 = i;
                                    if (char3 != ')') {
                                        continue;
                                    }
                                    final int n19 = --n17;
                                    n18 = i;
                                    if (n19 <= 0) {
                                        break;
                                    }
                                    continue;
                                }
                            }
                            continue;
                        }
                        if ('0' <= char1 && char1 <= '9') {
                            final int n20 = char1 - '0';
                            int n21 = char1;
                            int n22 = n20;
                            while (i < length) {
                                final char char4 = s.charAt(i);
                                final int n23 = n21 = char4;
                                if ('0' > char4 || (n21 = n23) > 57) {
                                    break;
                                }
                                n22 = n22 * 10 + n23 - 48;
                                ++i;
                                n21 = n23;
                            }
                            Label_0680: {
                                if (n9 != 43 && n9 != 45) {
                                    if (n22 < 70 && (n9 != 47 || n3 < 0 || n6 < 0 || n7 >= 0)) {
                                        Label_0379: {
                                            if (n21 != 58) {
                                                if (n21 == 47) {
                                                    if (n3 < 0) {
                                                        n3 = n22 - 1;
                                                        break Label_0680;
                                                    }
                                                    if (n6 >= 0) {
                                                        return ScriptRuntime.NaN;
                                                    }
                                                }
                                                else {
                                                    if (i < length && n21 != 44 && n21 > 32 && n21 != 45) {
                                                        return ScriptRuntime.NaN;
                                                    }
                                                    if (n != 0 && n22 < 60) {
                                                        if (n5 < 0.0) {
                                                            n5 -= n22;
                                                        }
                                                        else {
                                                            n5 += n22;
                                                        }
                                                        break Label_0680;
                                                    }
                                                    if (n4 >= 0 && n2 < 0) {
                                                        break Label_0379;
                                                    }
                                                    if (n2 >= 0 && n8 < 0) {
                                                        n8 = n22;
                                                        break Label_0680;
                                                    }
                                                    if (n6 >= 0) {
                                                        return ScriptRuntime.NaN;
                                                    }
                                                }
                                                n6 = n22;
                                                break Label_0680;
                                            }
                                            if (n4 < 0) {
                                                n4 = n22;
                                                break Label_0680;
                                            }
                                            if (n2 >= 0) {
                                                return ScriptRuntime.NaN;
                                            }
                                        }
                                        n2 = n22;
                                    }
                                    else {
                                        if (n7 >= 0) {
                                            return ScriptRuntime.NaN;
                                        }
                                        if (n21 > 32 && n21 != 44 && n21 != 47 && i < length) {
                                            return ScriptRuntime.NaN;
                                        }
                                        if (n22 < 100) {
                                            n22 += 1900;
                                        }
                                        n7 = n22;
                                    }
                                }
                                else {
                                    int n24;
                                    if (n22 < 24) {
                                        n24 = n22 * 60;
                                    }
                                    else {
                                        n24 = n22 % 100 + n22 / 100 * 60;
                                    }
                                    int n25 = n24;
                                    if (n9 == 43) {
                                        n25 = -n24;
                                    }
                                    if (n5 != 0.0 && n5 != -1.0) {
                                        return ScriptRuntime.NaN;
                                    }
                                    n5 = n25;
                                    n = 1;
                                }
                            }
                            n9 = 0;
                            continue;
                        }
                        if (char1 != '/' && char1 != ':' && char1 != '+' && char1 != '-') {
                            final int n26 = i - 1;
                            while (i < length) {
                                final char char5 = s.charAt(i);
                                if ('A' > char5 || char5 > 'Z') {
                                    final char c2;
                                    if ('a' > (c2 = char5)) {
                                        break;
                                    }
                                    if (char5 > 'z') {
                                        break;
                                    }
                                }
                                ++i;
                            }
                            final int n27 = i - n26;
                            if (n27 < 2) {
                                return ScriptRuntime.NaN;
                            }
                            final String s2 = "am;pm;monday;tuesday;wednesday;thursday;friday;saturday;sunday;january;february;march;april;may;june;july;august;september;october;november;december;gmt;ut;utc;est;edt;cst;cdt;mst;mdt;pst;pdt;";
                            int n28 = 0;
                            int n29 = 0;
                            int n30 = n9;
                            final int n31 = length;
                            while (true) {
                                final int index = s2.indexOf(59, n29);
                                if (index < 0) {
                                    return ScriptRuntime.NaN;
                                }
                                n13 = n30;
                                if (s2.regionMatches(true, n29, s, n26, n27)) {
                                    double n32 = 0.0;
                                    Label_1177: {
                                        if (n28 < 2) {
                                            if (n4 > 12 || n4 < 0) {
                                                return ScriptRuntime.NaN;
                                            }
                                            if (n28 == 0) {
                                                n15 = n4;
                                                n14 = n3;
                                                n32 = n5;
                                                if (n4 == 12) {
                                                    n15 = 0;
                                                    n14 = n3;
                                                    n32 = n5;
                                                }
                                            }
                                            else {
                                                n15 = n4;
                                                n14 = n3;
                                                n32 = n5;
                                                if (n4 != 12) {
                                                    n15 = n4 + 12;
                                                    n14 = n3;
                                                    n32 = n5;
                                                }
                                            }
                                        }
                                        else {
                                            n15 = n4;
                                            final int n33 = n28 - 2;
                                            if (n33 < 7) {
                                                n14 = n3;
                                                n32 = n5;
                                            }
                                            else {
                                                n14 = n33 - 7;
                                                if (n14 < 12) {
                                                    if (n3 >= 0) {
                                                        return ScriptRuntime.NaN;
                                                    }
                                                    n32 = n5;
                                                }
                                                else {
                                                    double n34 = 0.0;
                                                    switch (n14 - 12) {
                                                        default: {
                                                            Kit.codeBug();
                                                            n14 = n3;
                                                            n32 = n5;
                                                            break Label_1177;
                                                        }
                                                        case 10: {
                                                            n34 = 420.0;
                                                            break;
                                                        }
                                                        case 9: {
                                                            n34 = 480.0;
                                                            break;
                                                        }
                                                        case 8: {
                                                            n34 = 360.0;
                                                            break;
                                                        }
                                                        case 7: {
                                                            n34 = 420.0;
                                                            break;
                                                        }
                                                        case 6: {
                                                            n34 = 300.0;
                                                            break;
                                                        }
                                                        case 5: {
                                                            n34 = 360.0;
                                                            break;
                                                        }
                                                        case 4: {
                                                            n34 = 240.0;
                                                            break;
                                                        }
                                                        case 3: {
                                                            n34 = 300.0;
                                                            break;
                                                        }
                                                        case 2: {
                                                            n34 = 0.0;
                                                            break;
                                                        }
                                                        case 1: {
                                                            n34 = 0.0;
                                                            break;
                                                        }
                                                        case 0: {
                                                            n34 = 0.0;
                                                            break;
                                                        }
                                                    }
                                                    n32 = n34;
                                                    n14 = n3;
                                                }
                                            }
                                        }
                                    }
                                    n16 = n31;
                                    n5 = n32;
                                    break Label_1302;
                                }
                                n29 = index + 1;
                                ++n28;
                                n30 = n13;
                            }
                        }
                    }
                    n9 = c;
                    n35 = n4;
                    n14 = n3;
                    break Label_1318;
                }
                n9 = n13;
                n35 = n15;
                length = n16;
            }
            n4 = n35;
            n3 = n14;
        }
        if (n7 < 0 || n3 < 0 || n6 < 0) {
            return ScriptRuntime.NaN;
        }
        int n36;
        if ((n36 = n8) < 0) {
            n36 = 0;
        }
        int n37;
        if ((n37 = n2) < 0) {
            n37 = 0;
        }
        int n38;
        if ((n38 = n4) < 0) {
            n38 = 0;
        }
        final double date_msecFromDate = date_msecFromDate(n7, n3, n6, n38, n37, n36, 0.0);
        if (n5 == -1.0) {
            return internalUTC(date_msecFromDate);
        }
        return 60000.0 * n5 + date_msecFromDate;
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        final NativeDate nativeDate = new NativeDate();
        nativeDate.date = ScriptRuntime.NaN;
        nativeDate.exportAsJSClass(47, scriptable, b);
    }
    
    private static double internalUTC(final double n) {
        return n - NativeDate.LocalTZA - DaylightSavingTA(n - NativeDate.LocalTZA);
    }
    
    private static Object jsConstructor(final Object[] array) {
        final NativeDate nativeDate = new NativeDate();
        if (array.length == 0) {
            nativeDate.date = now();
            return nativeDate;
        }
        if (array.length == 1) {
            Object defaultValue;
            final Object o = defaultValue = array[0];
            if (o instanceof Scriptable) {
                defaultValue = ((Scriptable)o).getDefaultValue(null);
            }
            double n;
            if (defaultValue instanceof CharSequence) {
                n = date_parseString(defaultValue.toString());
            }
            else {
                n = ScriptRuntime.toNumber(defaultValue);
            }
            nativeDate.date = TimeClip(n);
            return nativeDate;
        }
        double date;
        final double n2 = date = date_msecFromArgs(array);
        if (!Double.isNaN(n2)) {
            date = n2;
            if (!Double.isInfinite(n2)) {
                date = TimeClip(internalUTC(n2));
            }
        }
        nativeDate.date = date;
        return nativeDate;
    }
    
    private static double jsStaticFunction_UTC(final Object[] array) {
        return TimeClip(date_msecFromArgs(array));
    }
    
    private static String js_toISOString(final double n) {
        final StringBuilder sb = new StringBuilder(27);
        final int yearFromTime = YearFromTime(n);
        if (yearFromTime < 0) {
            sb.append('-');
            append0PaddedUint(sb, -yearFromTime, 6);
        }
        else if (yearFromTime > 9999) {
            append0PaddedUint(sb, yearFromTime, 6);
        }
        else {
            append0PaddedUint(sb, yearFromTime, 4);
        }
        sb.append('-');
        append0PaddedUint(sb, MonthFromTime(n) + 1, 2);
        sb.append('-');
        append0PaddedUint(sb, DateFromTime(n), 2);
        sb.append('T');
        append0PaddedUint(sb, HourFromTime(n), 2);
        sb.append(':');
        append0PaddedUint(sb, MinFromTime(n), 2);
        sb.append(':');
        append0PaddedUint(sb, SecFromTime(n), 2);
        sb.append('.');
        append0PaddedUint(sb, msFromTime(n), 3);
        sb.append('Z');
        return sb.toString();
    }
    
    private static String js_toUTCString(final double n) {
        final StringBuilder sb = new StringBuilder(60);
        appendWeekDayName(sb, WeekDay(n));
        sb.append(", ");
        append0PaddedUint(sb, DateFromTime(n), 2);
        sb.append(' ');
        appendMonthName(sb, MonthFromTime(n));
        sb.append(' ');
        int yearFromTime;
        final int n2 = yearFromTime = YearFromTime(n);
        if (n2 < 0) {
            sb.append('-');
            yearFromTime = -n2;
        }
        append0PaddedUint(sb, yearFromTime, 4);
        sb.append(' ');
        append0PaddedUint(sb, HourFromTime(n), 2);
        sb.append(':');
        append0PaddedUint(sb, MinFromTime(n), 2);
        sb.append(':');
        append0PaddedUint(sb, SecFromTime(n), 2);
        sb.append(" GMT");
        return sb.toString();
    }
    
    private static double makeDate(double n, final Object[] array, int n2) {
        if (array.length == 0) {
            return ScriptRuntime.NaN;
        }
        boolean b = true;
        boolean b2 = true;
        boolean b3 = true;
        switch (n2) {
            default: {
                throw Kit.codeBug();
            }
            case 44: {
                b3 = false;
            }
            case 43: {
                n2 = 3;
                break;
            }
            case 42: {
                b = false;
            }
            case 41: {
                n2 = 2;
                b3 = b;
                break;
            }
            case 40: {
                b2 = false;
            }
            case 39: {
                n2 = 1;
                b3 = b2;
                break;
            }
        }
        boolean b4 = false;
        int length;
        if (array.length < n2) {
            length = array.length;
        }
        else {
            length = n2;
        }
        final double[] array2 = new double[3];
        for (int i = 0; i < length; ++i) {
            final double number = ScriptRuntime.toNumber(array[i]);
            if (number == number && !Double.isInfinite(number)) {
                array2[i] = ScriptRuntime.toInteger(number);
            }
            else {
                b4 = true;
            }
        }
        if (b4) {
            return ScriptRuntime.NaN;
        }
        if (n != n) {
            if (n2 < 3) {
                return ScriptRuntime.NaN;
            }
            n = 0.0;
        }
        else if (b3) {
            n = LocalTime(n);
        }
        int n3;
        double n4;
        if (n2 >= 3 && length < 0) {
            n3 = 0 + 1;
            n4 = array2[0];
        }
        else {
            n4 = YearFromTime(n);
            n3 = 0;
        }
        double n5;
        if (n2 >= 2 && n3 < length) {
            n5 = array2[n3];
            ++n3;
        }
        else {
            n5 = MonthFromTime(n);
        }
        double n6;
        if (n2 >= 1 && n3 < length) {
            n6 = array2[n3];
        }
        else {
            n6 = DateFromTime(n);
        }
        final double n7 = n = MakeDate(MakeDay(n4, n5, n6), TimeWithinDay(n));
        if (b3) {
            n = internalUTC(n7);
        }
        return TimeClip(n);
    }
    
    private static double makeTime(double n, final Object[] array, int n2) {
        if (array.length == 0) {
            return ScriptRuntime.NaN;
        }
        boolean b = true;
        boolean b2 = true;
        boolean b3 = true;
        boolean b4 = true;
        switch (n2) {
            default: {
                throw Kit.codeBug();
            }
            case 38: {
                b4 = false;
            }
            case 37: {
                n2 = 4;
                break;
            }
            case 36: {
                b = false;
            }
            case 35: {
                n2 = 3;
                b4 = b;
                break;
            }
            case 34: {
                b2 = false;
            }
            case 33: {
                n2 = 2;
                b4 = b2;
                break;
            }
            case 32: {
                b3 = false;
            }
            case 31: {
                n2 = 1;
                b4 = b3;
                break;
            }
        }
        boolean b5 = false;
        int length;
        if (array.length < n2) {
            length = array.length;
        }
        else {
            length = n2;
        }
        final double[] array2 = new double[4];
        for (int i = 0; i < length; ++i) {
            final double number = ScriptRuntime.toNumber(array[i]);
            if (number == number && !Double.isInfinite(number)) {
                array2[i] = ScriptRuntime.toInteger(number);
            }
            else {
                b5 = true;
            }
        }
        if (!b5 && n == n) {
            int n3 = 0;
            if (b4) {
                n = LocalTime(n);
            }
            double n4;
            if (n2 >= 4 && length < 0) {
                n4 = array2[0];
                n3 = 0 + 1;
            }
            else {
                n4 = HourFromTime(n);
            }
            double n5;
            if (n2 >= 3 && n3 < length) {
                n5 = array2[n3];
                ++n3;
            }
            else {
                n5 = MinFromTime(n);
            }
            double n6;
            if (n2 >= 2 && n3 < length) {
                n6 = array2[n3];
                ++n3;
            }
            else {
                n6 = SecFromTime(n);
            }
            double n7;
            if (n2 >= 1 && n3 < length) {
                n7 = array2[n3];
            }
            else {
                n7 = msFromTime(n);
            }
            final double n8 = n = MakeDate(Day(n), MakeTime(n4, n5, n6, n7));
            if (b4) {
                n = internalUTC(n8);
            }
            return TimeClip(n);
        }
        return ScriptRuntime.NaN;
    }
    
    private static int msFromTime(double n) {
        final double n2 = n %= 1000.0;
        if (n2 < 0.0) {
            n = n2 + 1000.0;
        }
        return (int)n;
    }
    
    private static double now() {
        return (double)System.currentTimeMillis();
    }
    
    private static double parseISOString(final String s) {
        final int n = 0;
        final int[] array2;
        final int[] array = array2 = new int[9];
        array2[0] = 1970;
        array2[2] = (array2[1] = 1);
        array2[4] = (array2[3] = 0);
        array2[6] = (array2[5] = 0);
        array2[8] = (array2[7] = -1);
        final int n2 = 4;
        final int n3 = 1;
        final boolean b = true;
        int n4 = 0;
        final int length = s.length();
        int i;
        int n5;
        int n6;
        int n7;
        if (length != 0) {
            final char char1 = s.charAt(0);
            if (char1 != '+' && char1 != '-') {
                i = n;
                n5 = n2;
                n6 = n3;
                n7 = (b ? 1 : 0);
                if (char1 == 'T') {
                    n4 = 0 + 1;
                    i = 3;
                    n5 = n2;
                    n6 = n3;
                    n7 = (b ? 1 : 0);
                }
            }
            else {
                n4 = 0 + 1;
                n5 = 6;
                int n8;
                if (char1 == '-') {
                    n8 = -1;
                }
                else {
                    n8 = 1;
                }
                n6 = n8;
                i = n;
                n7 = (b ? 1 : 0);
            }
        }
        else {
            n7 = (b ? 1 : 0);
            n6 = n3;
            n5 = n2;
            i = n;
        }
    Label_0735:
        while (i != -1) {
            int n9;
            if (i == 0) {
                n9 = n5;
            }
            else if (i == 6) {
                n9 = 3;
            }
            else {
                n9 = 2;
            }
            final int n10 = n4 + n9;
            if (n10 > length) {
                i = -1;
                break;
            }
            int j = n4;
            int n11 = 0;
            while (j < n10) {
                final char char2 = s.charAt(j);
                if (char2 < '0' || char2 > '9') {
                    i = -1;
                    n4 = j;
                    break Label_0735;
                }
                n11 = n11 * 10 + (char2 - '0');
                ++j;
            }
            array[i] = n11;
            if (j == length) {
                if (i == 3 || i == 7) {
                    i = -1;
                }
                n4 = j;
                break;
            }
            n4 = j + 1;
            final char char3 = s.charAt(j);
            if (char3 == 'Z') {
                array[8] = (array[7] = 0);
                switch (i) {
                    default: {
                        i = -1;
                        break;
                    }
                    case 4:
                    case 5:
                    case 6: {
                        break;
                    }
                }
                break;
            }
            switch (i) {
                case 8: {
                    i = -1;
                    break;
                }
                case 7: {
                    int n12 = n4;
                    if (char3 != ':') {
                        n12 = n4 - 1;
                    }
                    final int n13 = 8;
                    n4 = n12;
                    i = n13;
                    break;
                }
                case 6: {
                    if (char3 != '+' && char3 != '-') {
                        i = -1;
                    }
                    else {
                        i = 7;
                    }
                    break;
                }
                case 5: {
                    if (char3 == '.') {
                        i = 6;
                    }
                    else if (char3 != '+' && char3 != '-') {
                        i = -1;
                    }
                    else {
                        i = 7;
                    }
                    break;
                }
                case 4: {
                    if (char3 == ':') {
                        i = 5;
                    }
                    else if (char3 != '+' && char3 != '-') {
                        i = -1;
                    }
                    else {
                        i = 7;
                    }
                    break;
                }
                case 3: {
                    if (char3 == ':') {
                        i = 4;
                    }
                    else {
                        i = -1;
                    }
                    break;
                }
                case 2: {
                    if (char3 == 'T') {
                        i = 3;
                        break;
                    }
                    i = -1;
                    break;
                }
                case 0:
                case 1: {
                    if (char3 == '-') {
                        ++i;
                        break;
                    }
                    if (char3 == 'T') {
                        i = 3;
                        break;
                    }
                    i = -1;
                    break;
                }
            }
            if (i != 7) {
                continue;
            }
            int n14;
            if (char3 == '-') {
                n14 = -1;
            }
            else {
                n14 = 1;
            }
            n7 = n14;
        }
        if (i != -1) {
            if (n4 == length) {
                final int n15 = array[0];
                final int n16 = array[1];
                final int n17 = array[2];
                final int n18 = array[3];
                final int n19 = array[4];
                final int n20 = array[5];
                final int n21 = array[6];
                final int n22 = array[7];
                final int n23 = array[8];
                if (n15 <= 275943 && n16 >= 1 && n16 <= 12 && n17 >= 1 && n17 <= DaysInMonth(n15, n16) && n18 <= 24) {
                    if (n18 != 24 || (n19 <= 0 && n20 <= 0 && n21 <= 0)) {
                        if (n19 <= 59 && n20 <= 59 && n22 <= 23) {
                            if (n23 <= 59) {
                                double date_msecFromDate = date_msecFromDate(n15 * n6, n16 - 1, n17, n18, n19, n20, n21);
                                if (n22 != -1) {
                                    date_msecFromDate -= (n22 * 60 + n23) * 60000.0 * n7;
                                }
                                if (date_msecFromDate >= -8.64E15) {
                                    if (date_msecFromDate <= 8.64E15) {
                                        return date_msecFromDate;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ScriptRuntime.NaN;
    }
    
    private static String toLocale_helper(final double n, final int n2) {
        DateFormat dateFormat = null;
        switch (n2) {
            default: {
                throw new AssertionError();
            }
            case 7: {
                if (NativeDate.localeDateFormatter == null) {
                    NativeDate.localeDateFormatter = DateFormat.getDateInstance(1);
                }
                dateFormat = NativeDate.localeDateFormatter;
                break;
            }
            case 6: {
                if (NativeDate.localeTimeFormatter == null) {
                    NativeDate.localeTimeFormatter = DateFormat.getTimeInstance(1);
                }
                dateFormat = NativeDate.localeTimeFormatter;
                break;
            }
            case 5: {
                if (NativeDate.localeDateTimeFormatter == null) {
                    NativeDate.localeDateTimeFormatter = DateFormat.getDateTimeInstance(1, 1);
                }
                dateFormat = NativeDate.localeDateTimeFormatter;
                break;
            }
        }
        synchronized (dateFormat) {
            return dateFormat.format(new Date((long)n));
        }
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeDate.DATE_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        if (methodId != 1) {
            if (methodId != 47) {
                switch (methodId) {
                    default: {
                        if (!(scriptable2 instanceof NativeDate)) {
                            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
                        }
                        final NativeDate nativeDate = (NativeDate)scriptable2;
                        final double date = nativeDate.date;
                        switch (methodId) {
                            default: {
                                throw new IllegalArgumentException(String.valueOf(methodId));
                            }
                            case 46: {
                                if (date == date) {
                                    return js_toISOString(date);
                                }
                                throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage0("msg.invalid.date"));
                            }
                            case 45: {
                                final double number = ScriptRuntime.toNumber(array, 0);
                                double date2;
                                if (number == number && !Double.isInfinite(number)) {
                                    double localTime;
                                    if (date != date) {
                                        localTime = 0.0;
                                    }
                                    else {
                                        localTime = LocalTime(date);
                                    }
                                    double n = number;
                                    if (number >= 0.0) {
                                        n = number;
                                        if (number <= 99.0) {
                                            n = number + 1900.0;
                                        }
                                    }
                                    date2 = TimeClip(internalUTC(MakeDate(MakeDay(n, MonthFromTime(localTime), DateFromTime(localTime)), TimeWithinDay(localTime))));
                                }
                                else {
                                    date2 = ScriptRuntime.NaN;
                                }
                                nativeDate.date = date2;
                                return ScriptRuntime.wrapNumber(date2);
                            }
                            case 39:
                            case 40:
                            case 41:
                            case 42:
                            case 43:
                            case 44: {
                                final double date3 = makeDate(date, array, methodId);
                                nativeDate.date = date3;
                                return ScriptRuntime.wrapNumber(date3);
                            }
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 38: {
                                final double time = makeTime(date, array, methodId);
                                nativeDate.date = time;
                                return ScriptRuntime.wrapNumber(time);
                            }
                            case 30: {
                                final double timeClip = TimeClip(ScriptRuntime.toNumber(array, 0));
                                nativeDate.date = timeClip;
                                return ScriptRuntime.wrapNumber(timeClip);
                            }
                            case 29: {
                                double n2 = date;
                                if (date == date) {
                                    n2 = (date - LocalTime(date)) / 60000.0;
                                }
                                return ScriptRuntime.wrapNumber(n2);
                            }
                            case 27:
                            case 28: {
                                double n3 = date;
                                if (date == date) {
                                    double localTime2 = date;
                                    if (methodId == 27) {
                                        localTime2 = LocalTime(date);
                                    }
                                    n3 = msFromTime(localTime2);
                                }
                                return ScriptRuntime.wrapNumber(n3);
                            }
                            case 25:
                            case 26: {
                                double n4 = date;
                                if (date == date) {
                                    double localTime3 = date;
                                    if (methodId == 25) {
                                        localTime3 = LocalTime(date);
                                    }
                                    n4 = SecFromTime(localTime3);
                                }
                                return ScriptRuntime.wrapNumber(n4);
                            }
                            case 23:
                            case 24: {
                                double n5 = date;
                                if (date == date) {
                                    double localTime4 = date;
                                    if (methodId == 23) {
                                        localTime4 = LocalTime(date);
                                    }
                                    n5 = MinFromTime(localTime4);
                                }
                                return ScriptRuntime.wrapNumber(n5);
                            }
                            case 21:
                            case 22: {
                                double n6 = date;
                                if (date == date) {
                                    double localTime5 = date;
                                    if (methodId == 21) {
                                        localTime5 = LocalTime(date);
                                    }
                                    n6 = HourFromTime(localTime5);
                                }
                                return ScriptRuntime.wrapNumber(n6);
                            }
                            case 19:
                            case 20: {
                                double n7 = date;
                                if (date == date) {
                                    double localTime6 = date;
                                    if (methodId == 19) {
                                        localTime6 = LocalTime(date);
                                    }
                                    n7 = WeekDay(localTime6);
                                }
                                return ScriptRuntime.wrapNumber(n7);
                            }
                            case 17:
                            case 18: {
                                double n8 = date;
                                if (date == date) {
                                    double localTime7 = date;
                                    if (methodId == 17) {
                                        localTime7 = LocalTime(date);
                                    }
                                    n8 = DateFromTime(localTime7);
                                }
                                return ScriptRuntime.wrapNumber(n8);
                            }
                            case 15:
                            case 16: {
                                double n9 = date;
                                if (date == date) {
                                    double localTime8 = date;
                                    if (methodId == 15) {
                                        localTime8 = LocalTime(date);
                                    }
                                    n9 = MonthFromTime(localTime8);
                                }
                                return ScriptRuntime.wrapNumber(n9);
                            }
                            case 12:
                            case 13:
                            case 14: {
                                double n10 = date;
                                if (date == date) {
                                    double localTime9 = date;
                                    if (methodId != 14) {
                                        localTime9 = LocalTime(date);
                                    }
                                    final double n11 = n10 = YearFromTime(localTime9);
                                    if (methodId == 12) {
                                        if (context.hasFeature(1)) {
                                            n10 = n11;
                                            if (1900.0 <= n11) {
                                                n10 = n11;
                                                if (n11 < 2000.0) {
                                                    n10 = n11 - 1900.0;
                                                }
                                            }
                                        }
                                        else {
                                            n10 = n11 - 1900.0;
                                        }
                                    }
                                }
                                return ScriptRuntime.wrapNumber(n10);
                            }
                            case 10:
                            case 11: {
                                return ScriptRuntime.wrapNumber(date);
                            }
                            case 9: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("(new Date(");
                                sb.append(ScriptRuntime.toString(date));
                                sb.append("))");
                                return sb.toString();
                            }
                            case 8: {
                                if (date == date) {
                                    return js_toUTCString(date);
                                }
                                return "Invalid Date";
                            }
                            case 5:
                            case 6:
                            case 7: {
                                if (date == date) {
                                    return toLocale_helper(date, methodId);
                                }
                                return "Invalid Date";
                            }
                            case 2:
                            case 3:
                            case 4: {
                                if (date == date) {
                                    return date_format(date, methodId);
                                }
                                return "Invalid Date";
                            }
                        }
                        break;
                    }
                    case -1: {
                        return ScriptRuntime.wrapNumber(jsStaticFunction_UTC(array));
                    }
                    case -2: {
                        return ScriptRuntime.wrapNumber(date_parseString(ScriptRuntime.toString(array, 0)));
                    }
                    case -3: {
                        return ScriptRuntime.wrapNumber(now());
                    }
                }
            }
            else {
                final Scriptable object = ScriptRuntime.toObject(context, scriptable, scriptable2);
                final Object primitive = ScriptRuntime.toPrimitive(object, ScriptRuntime.NumberClass);
                if (primitive instanceof Number) {
                    final double doubleValue = ((Number)primitive).doubleValue();
                    if (doubleValue != doubleValue || Double.isInfinite(doubleValue)) {
                        return null;
                    }
                }
                final Object property = ScriptableObject.getProperty(object, "toISOString");
                if (property == NativeDate.NOT_FOUND) {
                    throw ScriptRuntime.typeError2("msg.function.not.found.in", "toISOString", ScriptRuntime.toString(object));
                }
                if (!(property instanceof Callable)) {
                    throw ScriptRuntime.typeError3("msg.isnt.function.in", "toISOString", ScriptRuntime.toString(object), ScriptRuntime.toString(property));
                }
                final Object call = ((Callable)property).call(context, scriptable, object, ScriptRuntime.emptyArgs);
                if (!ScriptRuntime.isPrimitive(call)) {
                    throw ScriptRuntime.typeError1("msg.toisostring.must.return.primitive", ScriptRuntime.toString(call));
                }
                return call;
            }
        }
        else {
            if (scriptable2 != null) {
                return date_format(now(), 2);
            }
            return jsConstructor(array);
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        this.addIdFunctionProperty(idFunctionObject, NativeDate.DATE_TAG, -3, "now", 0);
        this.addIdFunctionProperty(idFunctionObject, NativeDate.DATE_TAG, -2, "parse", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeDate.DATE_TAG, -1, "UTC", 7);
        super.fillConstructorProperties(idFunctionObject);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        final boolean b = false;
        final String s2 = null;
        int n = 0;
        String s3 = null;
        Label_1364: {
            switch (s.length()) {
                default: {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    break;
                }
                case 18: {
                    final char char1 = s.charAt(0);
                    if (char1 == 'g') {
                        s3 = "getUTCMilliseconds";
                        n = 28;
                        break;
                    }
                    if (char1 == 's') {
                        s3 = "setUTCMilliseconds";
                        n = 32;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char1 != 't') {
                        break;
                    }
                    final char char2 = s.charAt(8);
                    if (char2 == 'D') {
                        s3 = "toLocaleDateString";
                        n = 7;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char2 == 'T') {
                        s3 = "toLocaleTimeString";
                        n = 6;
                        break;
                    }
                    break;
                }
                case 17: {
                    s3 = "getTimezoneOffset";
                    n = 29;
                    break;
                }
                case 15: {
                    final char char3 = s.charAt(0);
                    if (char3 == 'g') {
                        s3 = "getMilliseconds";
                        n = 27;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char3 == 's') {
                        s3 = "setMilliseconds";
                        n = 31;
                        break;
                    }
                    break;
                }
                case 14: {
                    final char char4 = s.charAt(0);
                    if (char4 == 'g') {
                        s3 = "getUTCFullYear";
                        n = 14;
                        break;
                    }
                    if (char4 == 's') {
                        s3 = "setUTCFullYear";
                        n = 44;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char4 == 't') {
                        s3 = "toLocaleString";
                        n = 5;
                        break;
                    }
                    break;
                }
                case 13: {
                    final char char5 = s.charAt(0);
                    if (char5 == 'g') {
                        final char char6 = s.charAt(6);
                        if (char6 == 'M') {
                            s3 = "getUTCMinutes";
                            n = 24;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char6 == 'S') {
                            s3 = "getUTCSeconds";
                            n = 26;
                            break;
                        }
                        break;
                    }
                    else {
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char5 != 's') {
                            break;
                        }
                        final char char7 = s.charAt(6);
                        if (char7 == 'M') {
                            s3 = "setUTCMinutes";
                            n = 36;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char7 == 'S') {
                            s3 = "setUTCSeconds";
                            n = 34;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 12: {
                    final char char8 = s.charAt(2);
                    if (char8 == 'D') {
                        s3 = "toDateString";
                        n = 4;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char8 == 'T') {
                        s3 = "toTimeString";
                        n = 3;
                        break;
                    }
                    break;
                }
                case 11: {
                    final char char9 = s.charAt(3);
                    if (char9 != 'F') {
                        if (char9 == 'M') {
                            s3 = "toGMTString";
                            n = 8;
                            break;
                        }
                        if (char9 == 's') {
                            s3 = "constructor";
                            n = 1;
                            break;
                        }
                        switch (char9) {
                            default: {
                                n = (b ? 1 : 0);
                                s3 = s2;
                                break Label_1364;
                            }
                            case 85: {
                                final char char10 = s.charAt(0);
                                if (char10 == 'g') {
                                    final char char11 = s.charAt(9);
                                    if (char11 == 'r') {
                                        s3 = "getUTCHours";
                                        n = 22;
                                        break Label_1364;
                                    }
                                    n = (b ? 1 : 0);
                                    s3 = s2;
                                    if (char11 == 't') {
                                        s3 = "getUTCMonth";
                                        n = 16;
                                        break Label_1364;
                                    }
                                    break Label_1364;
                                }
                                else {
                                    n = (b ? 1 : 0);
                                    s3 = s2;
                                    if (char10 != 's') {
                                        break Label_1364;
                                    }
                                    final char char12 = s.charAt(9);
                                    if (char12 == 'r') {
                                        s3 = "setUTCHours";
                                        n = 38;
                                        break Label_1364;
                                    }
                                    n = (b ? 1 : 0);
                                    s3 = s2;
                                    if (char12 == 't') {
                                        s3 = "setUTCMonth";
                                        n = 42;
                                        break Label_1364;
                                    }
                                    break Label_1364;
                                }
                                break;
                            }
                            case 84: {
                                s3 = "toUTCString";
                                n = 8;
                                break Label_1364;
                            }
                            case 83: {
                                s3 = "toISOString";
                                n = 46;
                                break Label_1364;
                            }
                        }
                    }
                    else {
                        final char char13 = s.charAt(0);
                        if (char13 == 'g') {
                            s3 = "getFullYear";
                            n = 13;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char13 == 's') {
                            s3 = "setFullYear";
                            n = 43;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 10: {
                    final char char14 = s.charAt(3);
                    if (char14 == 'M') {
                        final char char15 = s.charAt(0);
                        if (char15 == 'g') {
                            s3 = "getMinutes";
                            n = 23;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char15 == 's') {
                            s3 = "setMinutes";
                            n = 35;
                            break;
                        }
                        break;
                    }
                    else if (char14 == 'S') {
                        final char char16 = s.charAt(0);
                        if (char16 == 'g') {
                            s3 = "getSeconds";
                            n = 25;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char16 == 's') {
                            s3 = "setSeconds";
                            n = 33;
                            break;
                        }
                        break;
                    }
                    else {
                        if (char14 != 'U') {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            break;
                        }
                        final char char17 = s.charAt(0);
                        if (char17 == 'g') {
                            s3 = "getUTCDate";
                            n = 18;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char17 == 's') {
                            s3 = "setUTCDate";
                            n = 40;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 9: {
                    s3 = "getUTCDay";
                    n = 20;
                    break;
                }
                case 8: {
                    final char char18 = s.charAt(3);
                    if (char18 != 'H') {
                        if (char18 != 'M') {
                            if (char18 == 'o') {
                                s3 = "toSource";
                                n = 9;
                                break;
                            }
                            if (char18 != 't') {
                                n = (b ? 1 : 0);
                                s3 = s2;
                                break;
                            }
                            s3 = "toString";
                            n = 2;
                            break;
                        }
                        else {
                            final char char19 = s.charAt(0);
                            if (char19 == 'g') {
                                s3 = "getMonth";
                                n = 15;
                                break;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (char19 == 's') {
                                s3 = "setMonth";
                                n = 41;
                                break;
                            }
                            break;
                        }
                    }
                    else {
                        final char char20 = s.charAt(0);
                        if (char20 == 'g') {
                            s3 = "getHours";
                            n = 21;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char20 == 's') {
                            s3 = "setHours";
                            n = 37;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 7: {
                    final char char21 = s.charAt(3);
                    if (char21 != 'D') {
                        if (char21 != 'T') {
                            if (char21 != 'Y') {
                                if (char21 != 'u') {
                                    n = (b ? 1 : 0);
                                    s3 = s2;
                                    break;
                                }
                                s3 = "valueOf";
                                n = 10;
                                break;
                            }
                            else {
                                final char char22 = s.charAt(0);
                                if (char22 == 'g') {
                                    s3 = "getYear";
                                    n = 12;
                                    break;
                                }
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (char22 == 's') {
                                    s3 = "setYear";
                                    n = 45;
                                    break;
                                }
                                break;
                            }
                        }
                        else {
                            final char char23 = s.charAt(0);
                            if (char23 == 'g') {
                                s3 = "getTime";
                                n = 11;
                                break;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (char23 == 's') {
                                s3 = "setTime";
                                n = 30;
                                break;
                            }
                            break;
                        }
                    }
                    else {
                        final char char24 = s.charAt(0);
                        if (char24 == 'g') {
                            s3 = "getDate";
                            n = 17;
                            break;
                        }
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (char24 == 's') {
                            s3 = "setDate";
                            n = 39;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 6: {
                    final char char25 = s.charAt(0);
                    if (char25 == 'g') {
                        s3 = "getDay";
                        n = 19;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char25 == 't') {
                        s3 = "toJSON";
                        n = 47;
                        break;
                    }
                    break;
                }
            }
        }
        int n2 = n;
        if (s3 != null) {
            n2 = n;
            if (s3 != s) {
                n2 = n;
                if (!s3.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    public String getClassName() {
        return "Date";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        Class<?> stringClass = clazz;
        if (clazz == null) {
            stringClass = ScriptRuntime.StringClass;
        }
        return super.getDefaultValue(stringClass);
    }
    
    double getJSTimeValue() {
        return this.date;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 47: {
                n2 = 1;
                s = "toJSON";
                break;
            }
            case 46: {
                n2 = 0;
                s = "toISOString";
                break;
            }
            case 45: {
                n2 = 1;
                s = "setYear";
                break;
            }
            case 44: {
                n2 = 3;
                s = "setUTCFullYear";
                break;
            }
            case 43: {
                n2 = 3;
                s = "setFullYear";
                break;
            }
            case 42: {
                n2 = 2;
                s = "setUTCMonth";
                break;
            }
            case 41: {
                n2 = 2;
                s = "setMonth";
                break;
            }
            case 40: {
                n2 = 1;
                s = "setUTCDate";
                break;
            }
            case 39: {
                n2 = 1;
                s = "setDate";
                break;
            }
            case 38: {
                n2 = 4;
                s = "setUTCHours";
                break;
            }
            case 37: {
                n2 = 4;
                s = "setHours";
                break;
            }
            case 36: {
                n2 = 3;
                s = "setUTCMinutes";
                break;
            }
            case 35: {
                n2 = 3;
                s = "setMinutes";
                break;
            }
            case 34: {
                n2 = 2;
                s = "setUTCSeconds";
                break;
            }
            case 33: {
                n2 = 2;
                s = "setSeconds";
                break;
            }
            case 32: {
                n2 = 1;
                s = "setUTCMilliseconds";
                break;
            }
            case 31: {
                n2 = 1;
                s = "setMilliseconds";
                break;
            }
            case 30: {
                n2 = 1;
                s = "setTime";
                break;
            }
            case 29: {
                n2 = 0;
                s = "getTimezoneOffset";
                break;
            }
            case 28: {
                n2 = 0;
                s = "getUTCMilliseconds";
                break;
            }
            case 27: {
                n2 = 0;
                s = "getMilliseconds";
                break;
            }
            case 26: {
                n2 = 0;
                s = "getUTCSeconds";
                break;
            }
            case 25: {
                n2 = 0;
                s = "getSeconds";
                break;
            }
            case 24: {
                n2 = 0;
                s = "getUTCMinutes";
                break;
            }
            case 23: {
                n2 = 0;
                s = "getMinutes";
                break;
            }
            case 22: {
                n2 = 0;
                s = "getUTCHours";
                break;
            }
            case 21: {
                n2 = 0;
                s = "getHours";
                break;
            }
            case 20: {
                n2 = 0;
                s = "getUTCDay";
                break;
            }
            case 19: {
                n2 = 0;
                s = "getDay";
                break;
            }
            case 18: {
                n2 = 0;
                s = "getUTCDate";
                break;
            }
            case 17: {
                n2 = 0;
                s = "getDate";
                break;
            }
            case 16: {
                n2 = 0;
                s = "getUTCMonth";
                break;
            }
            case 15: {
                n2 = 0;
                s = "getMonth";
                break;
            }
            case 14: {
                n2 = 0;
                s = "getUTCFullYear";
                break;
            }
            case 13: {
                n2 = 0;
                s = "getFullYear";
                break;
            }
            case 12: {
                n2 = 0;
                s = "getYear";
                break;
            }
            case 11: {
                n2 = 0;
                s = "getTime";
                break;
            }
            case 10: {
                n2 = 0;
                s = "valueOf";
                break;
            }
            case 9: {
                n2 = 0;
                s = "toSource";
                break;
            }
            case 8: {
                n2 = 0;
                s = "toUTCString";
                break;
            }
            case 7: {
                n2 = 0;
                s = "toLocaleDateString";
                break;
            }
            case 6: {
                n2 = 0;
                s = "toLocaleTimeString";
                break;
            }
            case 5: {
                n2 = 0;
                s = "toLocaleString";
                break;
            }
            case 4: {
                n2 = 0;
                s = "toDateString";
                break;
            }
            case 3: {
                n2 = 0;
                s = "toTimeString";
                break;
            }
            case 2: {
                n2 = 0;
                s = "toString";
                break;
            }
            case 1: {
                n2 = 7;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(NativeDate.DATE_TAG, n, s, n2);
    }
}
