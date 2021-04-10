package com.appsflyer.internal;

public enum b
{
    public static final b \u0131;
    public static final b \u0196;
    public static final b \u01c3;
    public static final b \u0269;
    public static final b \u0399;
    public static final b \u03b9;
    
    static {
        b.\u0269 = new b("EMPTY_ARRAY", 0);
        b.\u0399 = new b("NONEMPTY_ARRAY", 1);
        b.\u03b9 = new b("EMPTY_OBJECT", 2);
        b.\u01c3 = new b("DANGLING_KEY", 3);
        b.\u0131 = new b("NONEMPTY_OBJECT", 4);
        b.\u04c0 = new b[] { b.\u0269, b.\u0399, b.\u03b9, b.\u01c3, b.\u0131, b.\u0196 = new b("NULL", 5) };
    }
    
    public static b valueOf(final String s) {
        return Enum.valueOf(b.class, s);
    }
}
