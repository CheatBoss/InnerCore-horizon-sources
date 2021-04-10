package com.google.android.vending.expansion.downloader.impl;

import android.text.format.*;
import java.util.regex.*;

public final class HttpDateTime
{
    private static final Pattern HTTP_DATE_ANSIC_PATTERN;
    private static final String HTTP_DATE_ANSIC_REGEXP = "[ ]([A-Za-z]{3,9})[ ]+([0-9]{1,2})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])[ ]([0-9]{2,4})";
    private static final Pattern HTTP_DATE_RFC_PATTERN;
    private static final String HTTP_DATE_RFC_REGEXP = "([0-9]{1,2})[- ]([A-Za-z]{3,9})[- ]([0-9]{2,4})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])";
    
    static {
        HTTP_DATE_RFC_PATTERN = Pattern.compile("([0-9]{1,2})[- ]([A-Za-z]{3,9})[- ]([0-9]{2,4})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])");
        HTTP_DATE_ANSIC_PATTERN = Pattern.compile("[ ]([A-Za-z]{3,9})[ ]+([0-9]{1,2})[ ]([0-9]{1,2}:[0-9][0-9]:[0-9][0-9])[ ]([0-9]{2,4})");
    }
    
    private static int getDate(final String s) {
        if (s.length() == 2) {
            return (s.charAt(0) - '0') * 10 + (s.charAt(1) - '0');
        }
        return s.charAt(0) - '0';
    }
    
    private static int getMonth(final String s) {
        final int n = Character.toLowerCase(s.charAt(0)) + Character.toLowerCase(s.charAt(1)) + Character.toLowerCase(s.charAt(2)) - 291;
        int n2 = 9;
        if (n == 9) {
            return 11;
        }
        if (n == 10) {
            return 1;
        }
        if (n == 22) {
            return 0;
        }
        if (n == 26) {
            return 7;
        }
        if (n == 29) {
            return 2;
        }
        if (n == 32) {
            return 3;
        }
        if (n == 40) {
            return 6;
        }
        if (n == 42) {
            return 5;
        }
        if (n == 48) {
            return 10;
        }
        switch (n) {
            default: {
                throw new IllegalArgumentException();
            }
            case 37: {
                return 8;
            }
            case 36: {
                n2 = 4;
            }
            case 35: {
                return n2;
            }
        }
    }
    
    private static TimeOfDay getTime(final String s) {
        int n = s.charAt(0) - '0';
        int n2;
        if (s.charAt(1) != ':') {
            n2 = 2;
            n = n * 10 + (s.charAt(1) - '0');
        }
        else {
            n2 = 1;
        }
        final int n3 = n2 + 1;
        final int n4 = n3 + 1;
        final char char1 = s.charAt(n3);
        final char char2 = s.charAt(n4);
        final int n5 = n4 + 1 + 1;
        return new TimeOfDay(n, (char1 - '0') * 10 + (char2 - '0'), (s.charAt(n5) - '0') * 10 + (s.charAt(n5 + 1) - '0'));
    }
    
    private static int getYear(final String s) {
        if (s.length() == 2) {
            final int n = (s.charAt(0) - '0') * 10 + (s.charAt(1) - '0');
            if (n >= 70) {
                return n + 1900;
            }
            return n + 2000;
        }
        else {
            if (s.length() == 3) {
                return (s.charAt(0) - '0') * 100 + (s.charAt(1) - '0') * 10 + (s.charAt(2) - '0') + 1900;
            }
            if (s.length() == 4) {
                return (s.charAt(0) - '0') * 1000 + (s.charAt(1) - '0') * 100 + (s.charAt(2) - '0') * 10 + (s.charAt(3) - '0');
            }
            return 1970;
        }
    }
    
    public static long parse(final String s) throws IllegalArgumentException {
        final Matcher matcher = HttpDateTime.HTTP_DATE_RFC_PATTERN.matcher(s);
        int n;
        int n2;
        int n3;
        TimeOfDay timeOfDay;
        if (matcher.find()) {
            n = getDate(matcher.group(1));
            n2 = getMonth(matcher.group(2));
            n3 = getYear(matcher.group(3));
            timeOfDay = getTime(matcher.group(4));
        }
        else {
            final Matcher matcher2 = HttpDateTime.HTTP_DATE_ANSIC_PATTERN.matcher(s);
            if (!matcher2.find()) {
                throw new IllegalArgumentException();
            }
            n2 = getMonth(matcher2.group(1));
            n = getDate(matcher2.group(2));
            timeOfDay = getTime(matcher2.group(3));
            n3 = getYear(matcher2.group(4));
        }
        if (n3 >= 2038) {
            n = 1;
            n2 = 0;
            n3 = 2038;
        }
        final Time time = new Time("UTC");
        time.set(timeOfDay.second, timeOfDay.minute, timeOfDay.hour, n, n2, n3);
        return time.toMillis(false);
    }
    
    private static class TimeOfDay
    {
        int hour;
        int minute;
        int second;
        
        TimeOfDay(final int hour, final int minute, final int second) {
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }
    }
}
