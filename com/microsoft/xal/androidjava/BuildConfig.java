package com.microsoft.xal.androidjava;

public final class BuildConfig
{
    public static final String APPLICATION_ID = "com.microsoft.xal.androidjava";
    public static final String BUILD_TYPE = "debug";
    public static final boolean DEBUG;
    public static final String FLAVOR = "x86";
    public static final int VERSION_CODE = 1;
    public static final String VERSION_NAME = "1.0";
    
    static {
        DEBUG = Boolean.parseBoolean("true");
    }
}
