package com.appboy.support;

import bo.app.*;
import java.math.*;
import java.util.*;

public final class ValidationUtils
{
    public static final int APPBOY_STRING_MAX_LENGTH = 255;
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ValidationUtils.class);
    }
    
    public static String ensureAppboyFieldLength(String s) {
        final String s2 = s = s.trim();
        if (s2.length() > 255) {
            s = ValidationUtils.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Provided string field is too long [");
            sb.append(s2.length());
            sb.append("]. The max length is ");
            sb.append(255);
            sb.append(", truncating provided field.");
            AppboyLogger.w(s, sb.toString());
            s = s2.substring(0, 255);
        }
        return s;
    }
    
    public static boolean isValidEmailAddress(final String s) {
        return s != null && s.toLowerCase(Locale.US).matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }
    
    public static boolean isValidLocation(final double n, final double n2) {
        return n < 90.0 && n > -90.0 && n2 < 180.0 && n2 > -180.0;
    }
    
    public static boolean isValidLogCustomEventInput(final String s, final dr dr) {
        if (StringUtils.isNullOrBlank(s)) {
            AppboyLogger.w(ValidationUtils.a, "The custom event name cannot be null or contain only whitespaces. Invalid custom event.");
            return false;
        }
        if (dr.l().contains(s)) {
            final String a = ValidationUtils.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("The custom event is a blacklisted custom event: ");
            sb.append(s);
            sb.append(". Invalid custom event.");
            AppboyLogger.w(a, sb.toString());
            return false;
        }
        return true;
    }
    
    public static boolean isValidLogPurchaseInput(String s, String a, final BigDecimal bigDecimal, final int n, final dr dr, final Set<String> set) {
        if (StringUtils.isNullOrBlank(s)) {
            AppboyLogger.w(ValidationUtils.a, "The productId is empty, not logging in-app purchase to Appboy.");
            return false;
        }
        if (dr.n().contains(s)) {
            a = ValidationUtils.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("The productId is a blacklisted productId: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString());
            return false;
        }
        if (a == null) {
            s = ValidationUtils.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("The currencyCode is null. Expected one of ");
            sb2.append(set);
            AppboyLogger.w(s, sb2.toString());
            return false;
        }
        if (!set.contains(a)) {
            s = ValidationUtils.a;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("The currencyCode ");
            sb3.append(a);
            sb3.append(" is invalid. Expected one of ");
            sb3.append(set);
            AppboyLogger.w(s, sb3.toString());
            return false;
        }
        if (bigDecimal == null) {
            AppboyLogger.w(ValidationUtils.a, "The price is null.");
            return false;
        }
        if (n <= 0) {
            s = ValidationUtils.a;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("The requested purchase quantity of ");
            sb4.append(n);
            sb4.append(" is less than zero.");
            AppboyLogger.w(s, sb4.toString());
            return false;
        }
        if (n > 100) {
            s = ValidationUtils.a;
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("The requested purchase quantity of ");
            sb5.append(n);
            sb5.append(" is greater than the maximum of ");
            sb5.append(100);
            AppboyLogger.w(s, sb5.toString());
            return false;
        }
        return true;
    }
    
    public static boolean isValidPhoneNumber(final String s) {
        return s != null && s.matches("^[0-9 .\\(\\)\\+\\-]+$");
    }
    
    public static boolean isValidPushStoryClickInput(String s, String s2) {
        if (StringUtils.isNullOrBlank(s)) {
            s = ValidationUtils.a;
            s2 = "Campaign ID cannot be null or blank";
        }
        else {
            if (!StringUtils.isNullOrBlank(s2)) {
                return true;
            }
            s = ValidationUtils.a;
            s2 = "Push story page ID cannot be null or blank";
        }
        AppboyLogger.w(s, s2);
        return false;
    }
}
