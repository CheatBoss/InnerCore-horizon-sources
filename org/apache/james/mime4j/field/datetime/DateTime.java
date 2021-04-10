package org.apache.james.mime4j.field.datetime;

import java.util.*;

public class DateTime
{
    private final Date date;
    private final int day;
    private final int hour;
    private final int minute;
    private final int month;
    private final int second;
    private final int timeZone;
    private final int year;
    
    public DateTime(final String s, final int month, final int day, final int hour, final int minute, final int second, final int timeZone) {
        final int convertToYear = this.convertToYear(s);
        this.year = convertToYear;
        this.date = convertToDate(convertToYear, month, day, hour, minute, second, timeZone);
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.timeZone = timeZone;
    }
    
    public static Date convertToDate(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+0"));
        gregorianCalendar.set(n, n2 - 1, n3, n4, n5, n6);
        gregorianCalendar.set(14, 0);
        if (n7 != Integer.MIN_VALUE) {
            gregorianCalendar.add(12, (n7 / 100 * 60 + n7 % 100) * -1);
        }
        return gregorianCalendar.getTime();
    }
    
    private int convertToYear(final String s) {
        final int int1 = Integer.parseInt(s);
        final int length = s.length();
        if (length != 1 && length != 2) {
            if (length != 3) {
                return int1;
            }
            return int1 + 1900;
        }
        else {
            if (int1 >= 0 && int1 < 50) {
                return int1 + 2000;
            }
            return int1 + 1900;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final DateTime dateTime = (DateTime)o;
        final Date date = this.date;
        if (date == null) {
            if (dateTime.date != null) {
                return false;
            }
        }
        else if (!date.equals(dateTime.date)) {
            return false;
        }
        return this.day == dateTime.day && this.hour == dateTime.hour && this.minute == dateTime.minute && this.month == dateTime.month && this.second == dateTime.second && this.timeZone == dateTime.timeZone && this.year == dateTime.year;
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public int getDay() {
        return this.day;
    }
    
    public int getHour() {
        return this.hour;
    }
    
    public int getMinute() {
        return this.minute;
    }
    
    public int getMonth() {
        return this.month;
    }
    
    public int getSecond() {
        return this.second;
    }
    
    public int getTimeZone() {
        return this.timeZone;
    }
    
    public int getYear() {
        return this.year;
    }
    
    @Override
    public int hashCode() {
        final Date date = this.date;
        int hashCode;
        if (date == null) {
            hashCode = 0;
        }
        else {
            hashCode = date.hashCode();
        }
        return (((((((hashCode + 31) * 31 + this.day) * 31 + this.hour) * 31 + this.minute) * 31 + this.month) * 31 + this.second) * 31 + this.timeZone) * 31 + this.year;
    }
    
    public void print() {
        System.out.println(this.toString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getYear());
        sb.append(" ");
        sb.append(this.getMonth());
        sb.append(" ");
        sb.append(this.getDay());
        sb.append("; ");
        sb.append(this.getHour());
        sb.append(" ");
        sb.append(this.getMinute());
        sb.append(" ");
        sb.append(this.getSecond());
        sb.append(" ");
        sb.append(this.getTimeZone());
        return sb.toString();
    }
}
