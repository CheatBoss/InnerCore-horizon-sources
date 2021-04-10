package com.appsflyer.internal;

public enum e
{
    public static final e \u0131;
    public static final e \u01c3;
    public static final e \u0269;
    public String \u0399;
    
    static {
        e.\u01c3 = new e("NULL", 0, "null");
        e.\u0269 = new e("COM_ANDROID_VENDING", 1, "cav");
        e.\u03b9 = new e[] { e.\u01c3, e.\u0269, e.\u0131 = new e("OTHER", 2, "other") };
    }
    
    private e(final String \u03b9) {
        this.\u0399 = \u03b9;
    }
    
    public static e valueOf(final String s) {
        return Enum.valueOf(e.class, s);
    }
}
