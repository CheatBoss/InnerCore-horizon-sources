package com.google.android.gms.common.util;

import java.util.regex.*;

public class Strings
{
    private static final Pattern zzaak;
    
    static {
        zzaak = Pattern.compile("\\$\\{(.*?)\\}");
    }
    
    public static boolean isEmptyOrWhitespace(final String s) {
        return s == null || s.trim().isEmpty();
    }
}
