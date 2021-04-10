package com.google.android.gms.common.util;

import java.util.regex.*;

public final class GmsVersionParser
{
    private static Pattern zzzy;
    
    public static int parseBuildVersion(final int n) {
        if (n == -1) {
            return -1;
        }
        return n / 1000;
    }
}
