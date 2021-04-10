package com.appboy.support;

import java.util.*;

public class CustomAttributeValidationUtils
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(CustomAttributeValidationUtils.class);
    }
    
    public static String[] ensureCustomAttributeArrayValues(final String[] array) {
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; ++i) {
                array[i] = ValidationUtils.ensureAppboyFieldLength(array[i]);
            }
        }
        return array;
    }
    
    public static boolean isValidCustomAttributeKey(final String s, final Set<String> set) {
        if (s == null) {
            AppboyLogger.w(CustomAttributeValidationUtils.a, "Custom attribute key cannot be null.");
            return false;
        }
        if (set.contains(s)) {
            final String a = CustomAttributeValidationUtils.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Custom attribute key cannot be blacklisted attribute: ");
            sb.append(s);
            sb.append(".");
            AppboyLogger.w(a, sb.toString());
            return false;
        }
        return true;
    }
    
    public static boolean isValidCustomAttributeValue(final String s) {
        if (s == null) {
            AppboyLogger.w(CustomAttributeValidationUtils.a, "Custom attribute value cannot be null.");
            return false;
        }
        return true;
    }
}
