package com.microsoft.xbox.toolkit;

import java.text.*;
import java.net.*;
import java.io.*;
import com.microsoft.xboxtcui.*;
import android.text.format.*;
import android.view.*;
import android.graphics.*;
import java.util.*;
import java.lang.reflect.*;

public class JavaUtil
{
    private static final String HEX_PREFIX = "0x";
    private static final NumberFormat INTEGER_FORMATTER;
    private static final Date MIN_DATE;
    private static final NumberFormat PERCENT_FORMATTER;
    
    static {
        MIN_DATE = new Date(100, 1, 1);
        INTEGER_FORMATTER = NumberFormat.getIntegerInstance(Locale.getDefault());
        PERCENT_FORMATTER = NumberFormat.getPercentInstance(Locale.getDefault());
    }
    
    public static <T> boolean DeepCompareArrayList(final ArrayList<T> list, final ArrayList<T> list2) {
        if (list == list2) {
            return true;
        }
        if (list == null) {
            return list2 == null;
        }
        if (list2 == null) {
            return false;
        }
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (!list.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static String EnsureEncode(final String s) {
        if (s != null) {
            if (s.length() == 0) {
                return s;
            }
            try {
                return URLEncoder.encode(URLDecoder.decode(s, "UTF-8"), "UTF-8");
            }
            catch (UnsupportedEncodingException ex) {}
        }
        return s;
    }
    
    public static Date JSONDateToJavaDate(final String s) {
        if (isNullOrEmpty(s)) {
            return null;
        }
        XLEAssert.assertTrue(s.substring(0, 6).equals("/Date("));
        int length = s.length();
        if (s.substring(s.length() - 7, s.length()).equals("+0000)/")) {
            length = s.length() - 7;
        }
        else if (s.substring(s.length() - 2, s.length()).equals(")/")) {
            length = s.length() - 2;
        }
        else {
            XLEAssert.assertTrue(false);
        }
        return new Date(Long.parseLong(s.substring(6, length)));
    }
    
    public static String JavaDateToJSONDate(final Date time) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        gregorianCalendar.setTime(time);
        return String.format("/Date(%d)/", gregorianCalendar.getTimeInMillis());
    }
    
    public static int[] concatIntArrays(final int[]... array) {
        if (array == null) {
            return null;
        }
        final int length = array.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            n += array[i].length;
            ++i;
        }
        final int[] array2 = new int[n];
        final int length2 = array.length;
        int j = 0;
        int n2 = 0;
        while (j < length2) {
            final int[] array3 = array[j];
            System.arraycopy(array3, 0, array2, n2, array3.length);
            n2 += array3.length;
            ++j;
        }
        return array2;
    }
    
    public static String concatenateStringsWithDelimiter(final String s, final String s2, final String s3, final String s4) {
        return concatenateStringsWithDelimiter(s, s2, s3, s4, true);
    }
    
    public static String concatenateStringsWithDelimiter(final String s, final String s2, final String s3, String string, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        String s4;
        if (b) {
            s4 = " ";
        }
        else {
            s4 = "";
        }
        sb.append(s4);
        sb.append(string);
        sb.append(" ");
        string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        if (!isNullOrEmpty(s)) {
            sb2.append(s);
        }
        if (!isNullOrEmpty(s2)) {
            if (sb2.length() > 0) {
                sb2.append(string);
            }
            sb2.append(s2);
        }
        if (!isNullOrEmpty(s3)) {
            if (sb2.length() > 0) {
                sb2.append(string);
            }
            sb2.append(s3);
        }
        return sb2.toString();
    }
    
    public static String concatenateStringsWithDelimiter(String string, final boolean b, final String... array) {
        final StringBuilder sb = new StringBuilder();
        String s;
        if (b) {
            s = " ";
        }
        else {
            s = "";
        }
        sb.append(s);
        sb.append(string);
        sb.append(" ");
        string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        if (array.length == 0) {
            return "";
        }
        for (int i = 0; i < array.length; ++i) {
            if (!isNullOrEmpty(array[i])) {
                if (sb2.length() > 0) {
                    sb2.append(string);
                }
                sb2.append(array[i]);
            }
        }
        return sb2.toString();
    }
    
    public static String concatenateUrlWithLinkAndParam(final String s, final String s2, final String s3) {
        final StringBuffer sb = new StringBuffer();
        if (!isNullOrEmpty(s)) {
            sb.append(s);
        }
        if (!isNullOrEmpty(s2)) {
            if (sb.length() > 0) {
                sb.append(s3);
            }
            sb.append(s2);
        }
        return sb.toString();
    }
    
    public static boolean containsFlag(final int n, final int n2) {
        return (n & n2) == n2;
    }
    
    public static Date convertToUTC(final Date time) {
        if (time != null) {
            final TimeZone default1 = TimeZone.getDefault();
            final Calendar instance = Calendar.getInstance();
            instance.setTime(time);
            instance.add(14, -default1.getOffset(time.getTime()));
            return instance.getTime();
        }
        return null;
    }
    
    public static String formatInteger(final int n) {
        return JavaUtil.INTEGER_FORMATTER.format(n);
    }
    
    public static String formatPercent(final float n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(" is not between 0 and 1");
        XLEAssert.assertTrue(sb.toString(), n >= 0.0f && n <= 1.0f);
        return JavaUtil.PERCENT_FORMATTER.format(n);
    }
    
    public static String getCurrentStackTraceAsString() {
        final StringBuilder sb = new StringBuilder();
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null) {
            for (int length = stackTrace.length, i = 0; i < length; ++i) {
                final StackTraceElement stackTraceElement = stackTrace[i];
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\n\n \t ");
                sb2.append(stackTraceElement.toString());
                sb.append(sb2.toString());
            }
        }
        return sb.toString();
    }
    
    public static String getLocalizedDateString(final Date date) {
        try {
            return DateUtils.formatDateTime(XboxTcuiSdk.getApplicationContext(), date.getTime(), 131088);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static String getShortClassName(final Class clazz) {
        final String[] split = clazz.getName().split("\\.");
        return split[split.length - 1];
    }
    
    public static String getTimeStringMMSS(final long n) {
        return DateUtils.formatElapsedTime(n);
    }
    
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    private static <T> boolean isPositionInRange(final ArrayList<T> list, final int n) {
        return n >= 0 && n < list.size();
    }
    
    public static boolean isTouchPointInsideView(final float n, final float n2, final View view) {
        final int[] array = new int[2];
        view.getLocationOnScreen(array);
        return new Rect(array[0], array[1], array[0] + view.getWidth(), array[1] + view.getHeight()).contains((int)n, (int)n2);
    }
    
    public static <T> List<T> listIteratorToList(final ListIterator<T> listIterator) {
        final ArrayList<T> list = new ArrayList<T>();
        while (listIterator != null && listIterator.hasNext()) {
            list.add(listIterator.next());
        }
        return list;
    }
    
    public static <T> boolean move(final ArrayList<T> list, int i, final int n) {
        if (list != null && isPositionInRange(list, i) && isPositionInRange(list, n)) {
            final T value = list.get(i);
            int j;
            if ((j = i) < n) {
                while (i < n) {
                    final int n2 = i + 1;
                    list.set(i, list.get(n2));
                    i = n2;
                }
            }
            else {
                while (j > n) {
                    i = j - 1;
                    list.set(j, list.get(i));
                    j = i;
                }
            }
            list.set(n, value);
            return true;
        }
        return false;
    }
    
    private static boolean parseBoolean(final String s) {
        try {
            return Boolean.parseBoolean(s);
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static long parseHexLong(final String s) {
        if (s == null) {
            return 0L;
        }
        if (s.startsWith("0x")) {
            return parseHexLongExpectHex(s);
        }
        try {
            return Long.parseLong(s, 16);
        }
        catch (Exception ex) {
            return 0L;
        }
    }
    
    private static long parseHexLongExpectHex(String substring) {
        XLEAssert.assertTrue(substring.startsWith("0x"));
        substring = substring.substring(2);
        try {
            return Long.parseLong(substring, 16);
        }
        catch (Exception ex) {
            return 0L;
        }
    }
    
    public static int parseInteger(final String s) {
        try {
            return Integer.parseInt(s, 10);
        }
        catch (Exception ex) {
            return 0;
        }
    }
    
    public static String pluralize(final int n, final String s, final String s2, final String s3) {
        if (n == 0) {
            return s;
        }
        if (n != 1) {
            return String.format(s3, n);
        }
        return s2;
    }
    
    public static int randInRange(final Random random, final int n, final int n2) {
        XLEAssert.assertTrue(n2 >= n);
        return n + random.nextInt(n2 - n);
    }
    
    public static boolean setFieldValue(final Object o, final String s, final Object o2) {
        try {
            final Field declaredField = o.getClass().getDeclaredField(s);
            declaredField.setAccessible(true);
            declaredField.set(o, o2);
            return true;
        }
        catch (NoSuchFieldException | IllegalAccessException ex) {
            return false;
        }
    }
    
    public static void sleepDebug(final long n) {
    }
    
    public static String stringToLower(final String s) {
        if (s == null) {
            return null;
        }
        return s.toLowerCase();
    }
    
    public static String stringToUpper(final String s) {
        if (s == null) {
            return null;
        }
        return s.toUpperCase();
    }
    
    public static boolean stringsEqual(final String s, final String s2) {
        return (s == null && s2 == null) || s == s2 || stringsEqualNonNull(s, s2);
    }
    
    public static boolean stringsEqualCaseInsensitive(final String s, final String s2) {
        boolean b = true;
        if (s == s2) {
            return true;
        }
        if (s == null) {
            return false;
        }
        if (s2 == null) {
            return false;
        }
        if (s == null || s2 == null) {
            b = false;
        }
        XLEAssert.assertTrue(b);
        return s.equalsIgnoreCase(s2);
    }
    
    public static boolean stringsEqualNonNull(final String s, final String s2) {
        final boolean b = false;
        if (s == null) {
            return false;
        }
        if (s2 == null) {
            return false;
        }
        boolean b2 = b;
        if (s != null) {
            b2 = b;
            if (s2 != null) {
                b2 = true;
            }
        }
        XLEAssert.assertTrue(b2);
        return s.equals(s2);
    }
    
    public static boolean stringsEqualNonNullCaseInsensitive(final String s, final String s2) {
        final boolean b = false;
        if (s == null) {
            return false;
        }
        if (s2 == null) {
            return false;
        }
        if (s == s2) {
            return true;
        }
        boolean b2 = b;
        if (s != null) {
            b2 = b;
            if (s2 != null) {
                b2 = true;
            }
        }
        XLEAssert.assertTrue(b2);
        return s.equalsIgnoreCase(s2);
    }
    
    public static <T> ArrayList<T> sublistShuffle(final ArrayList<T> list, final int n) {
        final Random random = new Random();
        final ArrayList<T> list2 = new ArrayList<T>(n);
        if (list != null) {
            if (list.size() == 0) {
                return list2;
            }
            final int size = list.size();
            final boolean b = false;
            if (size >= n) {
                for (int i = 0; i < n; ++i) {
                    final int randInRange = randInRange(random, i, list.size());
                    final T value = list.get(i);
                    list.set(i, list.get(randInRange));
                    list.set(randInRange, value);
                    list2.add(list.get(i));
                }
            }
            else {
                XLEAssert.assertTrue(list.size() > 0 && list.size() < n);
                for (int j = 0; j < n; ++j) {
                    list2.add(list.get(random.nextInt(list.size())));
                }
            }
            boolean b2 = b;
            if (list2.size() == n) {
                b2 = true;
            }
            XLEAssert.assertTrue(b2);
        }
        return list2;
    }
    
    public static String surroundInQuotes(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(s);
        sb.append("\"");
        return sb.toString();
    }
    
    public static boolean tryParseBoolean(final String s, final boolean b) {
        try {
            return Boolean.parseBoolean(s);
        }
        catch (Exception ex) {
            return b;
        }
    }
    
    public static double tryParseDouble(final String s, final double n) {
        try {
            return Double.parseDouble(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    public static int tryParseInteger(final String s, final int n) {
        try {
            return Integer.parseInt(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    public static long tryParseLong(final String s, final long n) {
        try {
            return Long.parseLong(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    public static String urlDecode(String decode) {
        try {
            decode = URLDecoder.decode(decode, "UTF-8");
            return decode;
        }
        catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    public static String urlEncode(String encode) {
        try {
            encode = URLEncoder.encode(encode, "UTF-8");
            return encode;
        }
        catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
}
