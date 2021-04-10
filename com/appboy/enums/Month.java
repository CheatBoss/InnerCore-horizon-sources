package com.appboy.enums;

import com.appboy.support.*;

public enum Month
{
    APRIL(3), 
    AUGUST(7), 
    DECEMBER(11), 
    FEBRUARY(1), 
    JANUARY(0), 
    JULY(6), 
    JUNE(5), 
    MARCH(2), 
    MAY(4), 
    NOVEMBER(10), 
    OCTOBER(9), 
    SEPTEMBER(8);
    
    private static final String a;
    private final int b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(Month.class);
    }
    
    private Month(final int b) {
        this.b = b;
    }
    
    public static Month getMonth(final int n) {
        final Month[] values = values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final Month month = values[i];
            if (month.getValue() == n) {
                return month;
            }
        }
        final String a = Month.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("No month with value ");
        sb.append(n);
        sb.append(", value must be in (0,11)");
        AppboyLogger.e(a, sb.toString());
        return null;
    }
    
    public int getValue() {
        return this.b;
    }
}
