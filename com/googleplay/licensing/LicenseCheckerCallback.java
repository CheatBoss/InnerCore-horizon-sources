package com.googleplay.licensing;

public interface LicenseCheckerCallback
{
    public static final int ERROR_CHECK_IN_PROGRESS = 4;
    public static final int ERROR_INVALID_PACKAGE_NAME = 1;
    public static final int ERROR_INVALID_PUBLIC_KEY = 5;
    public static final int ERROR_MISSING_PERMISSION = 6;
    public static final int ERROR_NON_MATCHING_UID = 2;
    public static final int ERROR_NOT_MARKET_MANAGED = 3;
    
    void allow(final int p0);
    
    void applicationError(final int p0);
    
    void dontAllow(final int p0);
}
