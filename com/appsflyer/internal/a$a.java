package com.appsflyer.internal;

public enum a
{
    public static final a \u0399;
    public static final a \u03b9;
    public String \u0131;
    
    static {
        a.\u03b9 = new a("HOOKING", 0, "hk");
        a.\u0269 = new a[] { a.\u03b9, a.\u0399 = new a("DEBUGGABLE", 1, "dbg") };
    }
    
    private a(final String \u0131) {
        this.\u0131 = \u0131;
    }
    
    public static a valueOf(final String s) {
        return Enum.valueOf(a.class, s);
    }
}
