package com.appsflyer.internal;

public enum d
{
    public static final d \u0131;
    public static final d \u03b9;
    public String \u0269;
    
    static {
        d.\u0131 = new d("XPOSED", 0, "xps");
        d.\u01c3 = new d[] { d.\u0131, d.\u03b9 = new d("FRIDA", 1, "frd") };
    }
    
    private d(final String \u0269) {
        this.\u0269 = \u0269;
    }
    
    public static d valueOf(final String s) {
        return Enum.valueOf(d.class, s);
    }
}
